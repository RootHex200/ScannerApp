package com.example.scannerapp.view.landing.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.example.scannerapp.R
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

    // Define crop area (adjust based on screen size)
    private val cropRectWidth = 500  // Width of crop area in pixels
    private val cropRectHeight = 500 // Height of crop area in pixels

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_q_rscanner, container, false)
        previewView = view.findViewById(R.id.previewCamera)
       // tvResult = view.findViewById(R.id.takePhoto)

        cameraExecutor = Executors.newSingleThreadExecutor()
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep) // Load beep sound

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
        return view
    }

    private fun startCamera() {
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
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
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
                            if (isWithinCropArea(box)) { // Check if barcode is within center area
                                barcode.rawValue?.let { scannedValue ->
                                    if (scannedValue != lastScannedValue) {
                                        lastScannedValue = scannedValue
                                        tvResult.text = scannedValue
                                        playBeepSound()
                                        handleSuccessfulScan(scannedValue)
                                    }
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

    private fun isWithinCropArea(box: android.graphics.Rect): Boolean {
        val centerX = previewView.width / 2
        val centerY = previewView.height / 2

        val cropLeft = centerX - (cropRectWidth / 2)
        val cropTop = centerY - (cropRectHeight / 2)
        val cropRight = centerX + (cropRectWidth / 2)
        val cropBottom = centerY + (cropRectHeight / 2)

        return box.left >= cropLeft && box.right <= cropRight &&
                box.top >= cropTop && box.bottom <= cropBottom
    }

    private fun playBeepSound() {
        mediaPlayer.start() // Play beep sound
    }

    private fun handleSuccessfulScan(scannedData: String) {
        isScanningEnabled = false // Disable further scanning

        // Delay before returning
        handler.postDelayed({

//            findNavController().previousBackStackEntry?.savedStateHandle?.set("scannedResult", scannedData)
//            findNavController().popBackStack()
        }, 2000) // 2 seconds delay
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        requestPermissionsLauncher.launch(Manifest.permission.CAMERA)
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
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
