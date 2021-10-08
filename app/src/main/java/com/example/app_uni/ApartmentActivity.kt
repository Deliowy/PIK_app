package com.example.app_uni

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.io.InputStream
import java.net.URL

@Suppress("DEPRECATION")
class ApartmentActivity : AppCompatActivity() {
    private lateinit var bluePrint:ImageView
    var arguments:Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apartment)
        arguments=getIntent().getBundleExtra("Apartment")
        var currentApartment=arguments.fromBundle()
        bluePrint = findViewById(R.id.imageView)
        var apartmentLabel:TextView=findViewById(R.id.apartment_label)
        DownloadBluePrintTask(bluePrint).execute(currentApartment!!.bluePrintURL)
        apartmentLabel.text=currentApartment?.apartmentStreet+", д."+currentApartment?.apartmentBuilding+", кв."+currentApartment?.apartmentNumber+"\nЗавершено на: "+currentApartment?.apartmentCompletitionStatus+"%"
    }

    fun testRaport(view:View){
        val intent = Intent(this, RaportActivity::class.java)
        intent.putExtra("Apartment", arguments)
        startActivity(intent)
    }

    inner class DownloadBluePrintTask(var bmImage: ImageView) :
        AsyncTask<String?, Void?, Bitmap?>() {

        @SuppressLint("LongLogTag")
        override fun doInBackground(vararg params: String?): Bitmap? {
            val urldisplay = params[0]
            var mIcon11: Bitmap? = null
            try {
                val `in`: InputStream = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Ошибка передачи изображения", e.message)
                e.printStackTrace()
            }
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
            bmImage.adjustViewBounds=true
        }

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
}
