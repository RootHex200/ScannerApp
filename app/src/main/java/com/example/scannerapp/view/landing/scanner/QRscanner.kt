package com.example.scannerapp.view.landing.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.example.scannerapp.R
import com.example.scannerapp.view.details.DetailsActivity
import com.google.common.util.concurrent.ListenableFuture

import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
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

    // Define crop area (adjust based on screen size)
    private val cropRectWidth = 500  // Width of crop area in pixels
    private val cropRectHeight = 500 // Height of crop area in pixels
    private var zoomValue=1;
    private lateinit var zoomSeekBar:SeekBar
    @SuppressLint("MissingInflatedId")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_q_rscanner, container, false)
        previewView = view.findViewById(R.id.previewCamera)

         zoomSeekBar = view.findViewById<SeekBar>(R.id.zoomSeekbar)

        cameraExecutor = Executors.newSingleThreadExecutor()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep)

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



        if (allPermissionsGranted()) {
            startCamera(zoomSeekBar)
        } else {
            requestPermissions()
        }
        return view
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
                            processImageProxy(imageProxy)
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

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
                                    lastScannedValue = scannedValue

                                    playBeepSound()
                                    handleSuccessfulScan(scannedValue)
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

    private fun handleSuccessfulScan(scannedData: String) {
        isScanningEnabled = false // Disable further scanning

        // Delay before returning
        handler.postDelayed({
            var intent=Intent(activity,DetailsActivity::class.java)
            intent.putExtra("value",scannedData)
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
