package com.example.app_uni

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_request.*
import kotlinx.android.synthetic.main.content_request.*
import java.io.File


@Suppress("DEPRECATION")
class RequestActivity : AppCompatActivity() {
    lateinit var arguments:Bundle
    var currentApartment:ApartmentClass? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
        arguments=getIntent().getBundleExtra("Apartment")
        currentApartment=arguments.fromBundle()
    }

    fun scanBarCode(view: View){
        message(getString(R.string.scan_process_label))
        //showScan(true)
    }

    fun send_request(view: View){
        showProgress(true)
        SendRequestTask().execute(request_text_field.text.toString())
    }

    inner class SendRequestTask():AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?):String {
            try
            {
                Thread.sleep(2000)
                if (isExternalStorageWritable()) {
                    val externalStorageVolumes: Array<out File> =
                        ContextCompat.getExternalFilesDirs(applicationContext, null)
                    val primaryExternalStorage = externalStorageVolumes[0]
                    val filename: String = currentApartment!!.apartmentStreet + ", д. " + currentApartment!!.apartmentBuilding + ", кв. " + currentApartment!!.apartmentNumber + "_request.txt"
                    val writer = File(primaryExternalStorage, filename).bufferedWriter()
                    writer.write(params[0])
                    writer.newLine()
                    writer.close()
                    return "Done"
                } else return "External storage is not available"
            } catch (e: InterruptedException) {
                Thread.sleep(2000)
                return "Something's wrong"
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            showProgress(false)
            message(result)
        }
    }

    /*@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showScan(show: Boolean){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            request_text_field.visibility = if (show) View.GONE else View.VISIBLE
            request_text_field.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        cameraPreview.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            cameraPreview.visibility = if (show) View.VISIBLE else View.GONE
            cameraPreview.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        cameraPreview.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            cameraPreview.visibility = if (show) View.VISIBLE else View.GONE
            request_text_field.visibility = if (show) View.GONE else View.VISIBLE
        }
    }*/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()


            request_progress.visibility = if (show) View.VISIBLE else View.GONE
            request_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        request_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            request_progress.visibility = if (show) View.VISIBLE else View.GONE

        }
    }

    fun message(message:String?){
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun Bundle?.fromBundle(): ApartmentClass? {
        var raport= listOf(
            RaportActivity.RaportItem("Отделка окна", this!!.getBoolean("Отделка окна")),
            RaportActivity.RaportItem(
                "Установка розеток",
                this!!.getBoolean("Установка розеток")
            ),
            RaportActivity.RaportItem("Монтаж обоев", this!!.getBoolean("Монтаж обоев")),
            RaportActivity.RaportItem("Монтаж потолка", this!!.getBoolean("Монтаж потолка")),
            RaportActivity.RaportItem("Монтаж освещения", this!!.getBoolean("Монтаж освещения")),
            RaportActivity.RaportItem(
                "Установка плиток в санузле",
                this!!.getBoolean("Установка плиток в санузле")
            ),
            RaportActivity.RaportItem("Укладка ломината", this!!.getBoolean("Укладка ломината"))
        )
        return if (this==null) null
        else ApartmentClass(
            getInt("apartmentNumber"),
            getInt("apartmentBuilding"),
            getString("apartmentStreet"),
            getString("responsibleForQA"),
            getInt("apartmentCompletitionStatus"),
            raport,
            getString("bluePrintURL")
        )
    }

    // Checks if a volume containing external storage is available
// for read and write.
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

}
