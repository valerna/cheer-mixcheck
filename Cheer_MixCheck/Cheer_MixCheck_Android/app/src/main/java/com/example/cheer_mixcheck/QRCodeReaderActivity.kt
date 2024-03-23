package com.example.cheer_mixcheck

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class QRCodeReaderActivity : AppCompatActivity() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private val cameraPermission = Manifest.permission.CAMERA
    private val cameraRequestId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_reader)

        barcodeView = findViewById(R.id.barcode_scanner)
        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        logoImageView.setImageResource(R.drawable.mixcheck)

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, cameraPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(cameraPermission), cameraRequestId)
        } else {
            setupBarcodeView()
        }
    }

    private fun setupBarcodeView() {
        val formats: Collection<BarcodeFormat> = listOf(BarcodeFormat.QR_CODE)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(intent)
        barcodeView.decodeContinuous { result ->
            handleResult(result)
        }
        barcodeView.resume()
    }

    private fun handleResult(rawResult: Result?) {
        rawResult?.let { result ->
            if (result.text.startsWith("https://mixcheck.app/")) {
                Toast.makeText(this, "Valid QR Code: ${result.text}", Toast.LENGTH_LONG).show()
                Log.d("QRCodeReaderActivity", "Valid QR Code scanned: ${result.text}")
                displayScanSuccessOverlay()
                val intent = Intent(this@QRCodeReaderActivity, WebViewActivity::class.java)
                intent.putExtra("url", result.text)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show()
                Log.d("QRCodeReaderActivity", "Invalid QR Code scanned.")
            }
        } ?: run {
            Log.e("QRCodeReaderActivity", "Scanned QR code result is null.")
        }
        // Resume scanning
        barcodeView.resume()
    }

    private fun displayScanSuccessOverlay() {
        val overlayView = View(this).apply {
            setBackgroundColor(Color.GREEN)
            alpha = 0.3f
        }
        addContentView(overlayView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        overlayView.animate().alpha(0f).setDuration(1000).withEndAction {
            (overlayView.parent as ViewGroup).removeView(overlayView)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestId && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupBarcodeView()
        } else {
            Toast.makeText(this, "Camera permission is required to use QR scanner", Toast.LENGTH_LONG).show()
            Log.d("QRCodeReaderActivity", "Camera permission denied.")
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}