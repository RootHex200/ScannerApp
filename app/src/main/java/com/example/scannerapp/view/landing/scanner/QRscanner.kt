package com.example.scannerapp.view.landing.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

import com.example.scannerapp.R
import com.example.scannerapp.core.base.BaseFragment
import com.example.scannerapp.core.db.AppDatabase
import com.example.scannerapp.domain.model.QrCode
import com.example.scannerapp.domain.model.QrCodeType
import com.example.scannerapp.view.details.DetailsActivity
import com.google.common.util.concurrent.ListenableFuture

import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QRscanner() : BaseFragment<ScannerViewModel>(ScannerViewModel::class.java) {
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: androidx.camera.view.PreviewView
    private var lastScannedValue: String? = null // Prevents duplicate scanning
    private var isScanningEnabled = true // Controls scanning state
    private val handler = Handler(Looper.getMainLooper()) // Handles timeout
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var cameraControl: CameraControl
    private lateinit var uploadImage:ImageView
    private lateinit var zoomSeekBar:SeekBar
    private lateinit var qrCodeImagePreview:ImageView
    private lateinit var cameraRotation:ImageView
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraFlash:ImageView
    private var isTorch=false;


    @SuppressLint("MissingInflatedId", "NewApi")

    override fun getLayout(): Int {
        return R.layout.fragment_q_rscanner
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {

        initializeUiComponent()

        cameraZoomBarControl()

        openImageFromGallery()

        cameraRotationChange()
        cameraFlash()
        if (allPermissionsGranted()) {
            startCamera(zoomSeekBar)
        } else {
            requestPermissions()
        }
        viewModelObserve()
    }

    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun viewModelObserve(){
        viewModel.scanQrSuccess.subscribe{value->
            playBeepSound()
            handleSuccessfulScan(value)
        }
    }

    private fun cameraFlash(){
        cameraFlash.setOnClickListener {
            if(isTorch==false){
                isTorch=true
                cameraControl.enableTorch(true)
            }else{
                isTorch=false
                cameraControl.enableTorch(false)
            }
        }
    }
    private fun initializeUiComponent(){
        previewView = rootView.findViewById(R.id.previewCamera)
        uploadImage=rootView.findViewById(R.id.uploadImage)
        zoomSeekBar = rootView.findViewById<SeekBar>(R.id.zoomSeekbar)
        qrCodeImagePreview=rootView.findViewById<ImageView>(R.id.qrcodePreviewImage)
        cameraFlash=rootView.findViewById<ImageView>(R.id.cameraFlash)
        cameraExecutor = Executors.newSingleThreadExecutor()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep)
        cameraRotation=rootView.findViewById(R.id.cameraRotate)
    }

    private fun cameraRotationChange(){
        cameraRotation.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            startCamera(zoomSeekBar)
        }
    }
    private fun cameraZoomBarControl(){
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
    }

    private fun openImageFromGallery(){
        var pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageUri=result.data!!.data
            var bitmap=uriToBitmap(imageUri!!)
            qrCodeImagePreview.setImageBitmap(bitmap)
            if (bitmap != null) {
                viewModel.qrScanFromImage(InputImage.fromBitmap(bitmap,0))
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


    @SuppressLint("CheckResult")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            viewModel.qrScanFromImage(image)

        }
    }


    private fun playBeepSound() {
        mediaPlayer.start() // Play beep sound
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSuccessfulScan(scannedData: QrCode) {
        isScanningEnabled = false // Disable further scanning

        handler.postDelayed({
            var intent=Intent(activity,DetailsActivity::class.java)
            intent.putExtra("value",scannedData.content)
            startActivity(intent)
            qrCodeImagePreview.setImageResource(R.drawable.qr_camera)
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
