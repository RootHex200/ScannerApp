package com.example.scannerapp.view.landing.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.room.Room

import com.example.scannerapp.R
import com.example.scannerapp.db.AppDatabase
import com.example.scannerapp.db.QRHistoryInfo
import com.example.scannerapp.db.QRHistoryType
import com.example.scannerapp.service.QRData
import com.example.scannerapp.service.QRGeneratorService
import com.example.scannerapp.view.details.DetailsActivity
import com.google.common.util.concurrent.ListenableFuture

import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.time.Duration
class QRscanner : Fragment() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: androidx.camera.view.PreviewView
    private lateinit var tvResult: TextView
    private var lastScannedValue: String? = null // Prevents duplicate scanning
    private var isScanningEnabled = true // Controls scanning state
    private val handler = Handler(Looper.getMainLooper()) // Handles timeout
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var cameraControl: CameraControl
    private lateinit var uploadImage:ImageView
    // Define crop area (adjust based on screen size)
    private val cropRectWidth = 500  // Width of crop area in pixels
    private val cropRectHeight = 500 // Height of crop area in pixels
    private var zoomValue=1;
    private lateinit var zoomSeekBar:SeekBar
    private lateinit var db:AppDatabase
    private lateinit var qrCodeImagePreview:ImageView
    private lateinit var cameraRotation:ImageView
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    @SuppressLint("MissingInflatedId", "NewApi")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db=AppDatabase.getInstance(container!!.context)

        val view: View = inflater.inflate(R.layout.fragment_q_rscanner, container, false)
        previewView = view.findViewById(R.id.previewCamera)
        uploadImage=view.findViewById(R.id.uploadImage)
         zoomSeekBar = view.findViewById<SeekBar>(R.id.zoomSeekbar)
        qrCodeImagePreview=view.findViewById<ImageView>(R.id.qrcodePreviewImage)
        cameraExecutor = Executors.newSingleThreadExecutor()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep)
        cameraRotation=view.findViewById(R.id.cameraRotate)
        zoomSeekBar.max = 100 // CameraX zoom range is from 0 to 1, so map 0-100

        zoomSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { // Ensure only user interactions update zoom
                    val zoomRatio = progress / 100f // Convert progress to 0-1 range
                    cameraControl.setLinearZoom(zoomRatio)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

         var pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageUri=result.data!!.data
             var bitmap=uriToBitmap(imageUri!!)
             qrCodeImagePreview.setImageBitmap(bitmap)
             if (bitmap != null) {
                 scanQRCodeFromBitmap(bitmap)
             }
        }

        uploadImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            // here item is type of image
            galleryIntent.type = "image/*"
            // ActivityResultLauncher callback

            pickImageLauncher.launch(galleryIntent)
            //pickImageLauncher.launch("image/*")
        }


        cameraRotation.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            startCamera(zoomSeekBar)
        }

        if (allPermissionsGranted()) {
            startCamera(zoomSeekBar)
        } else {
            requestPermissions()
        }
        return view
    }


    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val drawable = Drawable.createFromStream(inputStream, uri.toString())
            drawable?.toBitmap()
        } catch (e: Exception) {
            Log.e("QRScanner", "Error converting URI to Bitmap", e)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scanQRCodeFromBitmap(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { scannedValue ->
                        if (scannedValue != lastScannedValue) {
                            lastScannedValue = QRGeneratorService().formatBarcode(barcode).formattedData
                            playBeepSound()
                            handleSuccessfulScan(QRGeneratorService().formatBarcode(barcode))
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.e("QRScanner", "Failed to scan from image", it)
                Toast.makeText(requireContext(), "Failed to scan QR code from image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun startCamera(zoomSeekBar: SeekBar) {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        if (isScanningEnabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                processImageProxy(imageProxy)
                            }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            //val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
                )
                cameraControl = camera.cameraControl

                // Update SeekBar when zoom changes from pinch gesture
                camera.cameraInfo.zoomState.observe(viewLifecycleOwner) { zoomState ->
                    val zoomRatio = zoomState.linearZoom
                    zoomSeekBar.progress = (zoomRatio * 100).toInt() // Sync SeekBar with pinch zoom
                }

                setupPinchToZoom()
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }



    private fun setupPinchToZoom() {
        val scaleGestureDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val zoomRatio = detector.scaleFactor.coerceIn(0f, 1f) // Fix scaling range
                    cameraControl.setLinearZoom(zoomRatio)
                    return true
                }
            })

        previewView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()

            scanner.process(image)
                .addOnSuccessListener { barcodes ->

                    for (barcode in barcodes) {
                        barcode.boundingBox?.let { box ->

                            barcode.rawValue?.let { scannedValue ->
                                if (scannedValue != lastScannedValue) {
                                    lastScannedValue = QRGeneratorService().formatBarcode(barcode).formattedData

                                    playBeepSound()
                                    handleSuccessfulScan(QRGeneratorService().formatBarcode(barcode))
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("QRScanner", "Failed to scan barcode", it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }


    private fun playBeepSound() {
        mediaPlayer.start() // Play beep sound
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSuccessfulScan(scannedData: QRData) {
        isScanningEnabled = false // Disable further scanning

        //history create
        var qrHistoryDao=db.qrHistoryDao()
        qrHistoryDao.insertQRInfo(QRHistoryInfo(historyType = QRHistoryType.SCAN_HISTORY, value = scannedData.formattedData, type = scannedData.type, createAt = LocalDateTime.now().toString() ))


        handler.postDelayed({
            var intent=Intent(activity,DetailsActivity::class.java)
            intent.putExtra("value",scannedData.formattedData)
            startActivity(intent)
        }, 1000) // 2 seconds delay
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        requestPermissionsLauncher.launch(Manifest.permission.CAMERA)
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera(zoomSeekBar)
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        mediaPlayer.release() // Release media player resources
        handler.removeCallbacksAndMessages(null)
    }
}
