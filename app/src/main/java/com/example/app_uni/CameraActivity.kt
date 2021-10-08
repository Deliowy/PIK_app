@file:Suppress("DEPRECATION")

package com.example.app_uni

import android.app.Activity
import android.content.pm.ActivityInfo
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import net.sourceforge.zbar.Config
import net.sourceforge.zbar.Image
import net.sourceforge.zbar.ImageScanner
import android.hardware.Camera.PreviewCallback

class CameraActivity : Activity() {


    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var autoFocusHandler: Handler? = null
    private var preview: FrameLayout? = null
    private var scanText: TextView? = null
    private var scanner: ImageScanner? = null
    private var barcodeScanned = false
    private var previewing = true
    private var lastScannedCode: String? = null
    private var codeImage: Image? = null

    companion object {
        private const val WHITE = -0x1
        private const val BLACK = -0x1000000//
        /**
         * A safe way to get an instance of the Camera object.
         */
        val cameraInstance: Camera?
            get() {
                var c: Camera? = null
                try {
                    c = Camera.open()
                } catch (e: Exception) { //
                }
                return c
            }

        private fun guessAppropriateEncoding(contents: CharSequence): String? { // Very crude at the moment
            for (i in 0 until contents.length) {
                if (contents[i].toInt() > 0xFF) {
                    return "UTF-8"
                }
            }
            return null
        }

        init {
            System.loadLibrary("iconv")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        autoFocusHandler = Handler()
        preview = findViewById<View>(R.id.cameraPreview) as FrameLayout
        /* Instance barcode scanner */
        scanner = ImageScanner()
        scanner!!.setConfig(0, Config.X_DENSITY, 3)
        scanner!!.setConfig(0, Config.Y_DENSITY, 3)
        scanText = findViewById<View>(R.id.request_text_field) as TextView
    }

    override fun onResume() {
        super.onResume()
        resumeCamera()
    }

    public override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    private fun releaseCamera() {
        if (mCamera != null) {
            previewing = false
            mCamera!!.cancelAutoFocus()
            mCamera!!.setPreviewCallback(null)
            mCamera!!.stopPreview()
            mCamera!!.release()
            mCamera = null
        }
    }

    private fun resumeCamera() {
        Toast.makeText(this, getString(R.string.scan_process_label), Toast.LENGTH_LONG)
            .show()
        //scanText!!.text = getString(R.string.scan_process_label)
        mCamera = cameraInstance
        mPreview = CameraPreview(this, mCamera, previewCb, autoFocusCB)
        preview!!.removeAllViews()
        preview!!.addView(mPreview)
        if (mCamera != null) {
            val parameters = mCamera!!.parameters
            val size = parameters.previewSize
            codeImage = Image(size.width, size.height, "Y800")
            previewing = true
            mPreview!!.refreshDrawableState()
        }
    }

    private val doAutoFocus = Runnable {
        if (previewing && mCamera != null) {
            mCamera!!.autoFocus(autoFocusCB)
        }
    }
    var previewCb = PreviewCallback { data, camera ->
        codeImage!!.data = data
        val result = scanner!!.scanImage(codeImage)
        if (result != 0) {
            val syms = scanner!!.results
            for (sym in syms) {
                lastScannedCode = sym.data
                if (lastScannedCode != null) {
                    scanText!!.text =scanText!!.text.toString()+"\n"+
                            getString(R.string.scan_result_label).toString() + lastScannedCode
                    barcodeScanned = true

                }
            }
        }
        camera.addCallbackBuffer(data)
    }
    // Mimic continuous auto-focusing
    val autoFocusCB: Camera.AutoFocusCallback =
        Camera.AutoFocusCallback { success, camera ->
            autoFocusHandler!!.postDelayed(
                doAutoFocus,
                1000
            )
        }

    fun message(message:String?){
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }

}
