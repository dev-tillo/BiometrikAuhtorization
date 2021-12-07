package com.example.biometrikauhtorization

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.biometrikauhtorization.utils.*
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Intent
import kotlinx.coroutines.Dispatchers.Main


private const val CAMERA_PERMISSION = 7762

@SuppressLint("UnsafeExperimentalUsageError")
class Login : AppCompatActivity() {
    private val useCase =
        ExtractDataUseCase(TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        cameraPermissions()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun cameraPermissions() {
        if (hasPermission(Manifest.permission.CAMERA)
        ) {
            launchWhenResumed {
                bindUseCases(getCameraProvider())
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
            }
        }
    }

    @ExperimentalGetImage
    private fun bindUseCases(cameraProvider: ProcessCameraProvider) {
        val preview = buildPreview()
        val takePicture = buildTakePicture()
        val cameraSelector = buildCameraSelector()

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, takePicture)

        button.setOnClickListener {
            launchWhenResumed {
                val imageProxy = takePicture.takePicture(executor)
                val cardDetails = useCase(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
                bindCardDetails(cardDetails)
            }
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    private fun buildPreview(): Preview = Preview.Builder()
        .build()
        .apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

    private fun buildCameraSelector(): CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    private fun buildTakePicture(): ImageCapture = ImageCapture.Builder()
        .setTargetRotation(previewView.display.rotation)
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    @SuppressLint("SetTextI18n")
    private fun bindCardDetails(card: CardDetails) {
        owner.text = card.owner
        number.text = card.number
        date.text = "${card.expirationMonth}/${card.expirationYear}"
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchWhenResumed {
                bindUseCases(getCameraProvider())
            }
        }
    }
}