package com.example.app_uni

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.hardware.Camera.PreviewCallback
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

/*
 * Barebones implementation of displaying camera preview.
 *
 * Created by lisah0 on 2012-02-24
 */


/**
 * A basic Camera preview class
 */
class CameraPreview(
    context: Context?, private val mCamera: Camera?,
    private val previewCallback: PreviewCallback,
    private val autoFocusCallback: AutoFocusCallback
) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder
    private lateinit var cameraBuffer: ByteArray
    override fun surfaceCreated(holder: SurfaceHolder) { // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera?.setPreviewDisplay(holder)
        } catch (e: IOException) {
            Log.d("DBG", "Error setting camera preview: " + e.message)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) { // Camera preview released in activity
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) { /*
         * If your preview can change or rotate, take care of those events here.
         * Make sure to stop the preview before resizing or reformatting it.
         */
        if (mHolder.surface == null) { // preview surface does not exist
            return
        }
        if (mCamera != null) { // stop preview before making changes
            try {
                mCamera.stopPreview()
            } catch (e: Exception) { // ignore: tried to stop a non-existent preview
            }
            try {
                val parameters = mCamera.parameters
                val previewSize = parameters.previewSize
                val imageFormat = parameters.previewFormat
                val bufferSize =
                    previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(imageFormat) / 8
                cameraBuffer = ByteArray(bufferSize)
                // Hard code camera surface rotation 90 degs to match Activity view in portrait
                mCamera.setDisplayOrientation(90)
                mCamera.setPreviewDisplay(mHolder)
                mCamera.setPreviewCallbackWithBuffer(null)
                mCamera.setPreviewCallbackWithBuffer(previewCallback)
                mCamera.addCallbackBuffer(cameraBuffer)
                mCamera.startPreview()
                mCamera.autoFocus(autoFocusCallback)
            } catch (e: Exception) {
                Log.d("DBG", "Error starting camera preview: " + e.message)
            }
        }
    }

    init {
        /*
         * Set camera to continuous focus if supported, otherwise use
         * software auto-focus. Only works for API level >=9.
         */
/*
        Camera.Parameters parameters = camera.getParameters();
        for (String f : parameters.getSupportedFocusModes()) {
            if (f == Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                mCamera.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                autoFocusCallback = null;
                break;
            }
        }
        */
// Install a SurfaceHolder.Callback so we get notified when the
// underlying surface is created and destroyed.
        mHolder = holder
        mHolder.addCallback(this)
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }
}

