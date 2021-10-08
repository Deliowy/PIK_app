package com.example.app_uni

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_apartment_list.*

import kotlinx.android.synthetic.main.activity_raport.*
import kotlinx.android.synthetic.main.fragment_raport.*
import kotlinx.android.synthetic.main.raport_recycler_view_item.*
import java.io.File

class RaportActivity : AppCompatActivity(), MyRaportRecyclerViewAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var arguments:Bundle
    lateinit var raportDataset: List<RaportItem>
    var currentApartment:ApartmentClass? =null

   object RaportDataset {
        var raportDataset: List<RaportItem> = listOf(
            RaportItem("Отделка окна", false),
            RaportItem("Установка розеток", false),
            RaportItem("Монтаж обоев", false),
            RaportItem("Монтаж потолка", false),
            RaportItem("Монтаж освещения", false),
            RaportItem("Установка плиток в санузле", false),
            RaportItem("Укладка ломината", false)
        )
    }

    override fun onItemClicked(raport: RaportItem, checkBox: CheckBox) {
        raport.Status=checkBox.isChecked
        Log.i("Task_",raport.get())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raport)
        arguments=getIntent().getBundleExtra("Apartment")
        raportDataset= listOf(
            RaportItem("Отделка окна", arguments.getBoolean("Отделка окна")),
            RaportItem("Установка розеток", arguments.getBoolean("Установка розеток")),
            RaportItem("Монтаж обоев", arguments.getBoolean("Монтаж обоев")),
            RaportItem("Монтаж потолка", arguments.getBoolean("Монтаж потолка")),
            RaportItem("Монтаж освещения", arguments.getBoolean("Монтаж освещения")),
            RaportItem("Установка плиток в санузле", arguments.getBoolean("Установка плиток в санузле")),
            RaportItem("Укладка ломината", arguments.getBoolean("Укладка ломината"))
        )
        currentApartment=arguments.fromBundle()

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyRaportRecyclerViewAdapter(raportDataset, this)

        recyclerView = findViewById<RecyclerView>(R.id.raport_list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
    class RaportItem(var Task:String, var Status:Boolean){
        fun get(): String {
            return Task + " Status:" + if(Status) "Complete" else " Incomplete"
        }
        fun set(Task: String, Status: Boolean){
            this.Task=Task
            this.Status=Status
        }
    }

    fun testRequestActivity(view: View){
        val intent = Intent(this, RequestActivity::class.java)
        intent.putExtra("Apartment", arguments)
        startActivity(intent)
    }

    fun testSendRaport(view:View){
        showProgress(true)
        SendRaport().execute(raportDataset)
    }

    fun raportDataset():List<RaportItem>{
        return raportDataset
    }

    fun message(message:String?){
        Toast.makeText(this, message,Toast.LENGTH_LONG)
            .show()
    }

    inner class SendRaport:AsyncTask<List<RaportItem>, Void, String> (){

        override fun doInBackground(vararg params: List<RaportItem>?):String {
            try
            {
                Thread.sleep(2000)
                if (isExternalStorageWritable()) {
                    val externalStorageVolumes: Array<out File> =
                        ContextCompat.getExternalFilesDirs(applicationContext, null)
                    val primaryExternalStorage = externalStorageVolumes[0]
                    val filename: String =currentApartment!!.apartmentStreet + ", д. " + currentApartment!!.apartmentBuilding + ", кв. " + currentApartment!!.apartmentNumber + "_raport.txt"
                    val writer = File(primaryExternalStorage, filename).bufferedWriter()
                    for (task in params[0]!!) {
                        writer.write(task.get())
                        writer.newLine()
                    }
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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            raport_progress.visibility = if (show) View.VISIBLE else View.GONE
            raport_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        raport_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            raport_progress.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    private fun Bundle?.fromBundle(): ApartmentClass? {
        return if (this==null) null
        else ApartmentClass(
            getInt("apartmentNumber"),
            getInt("apartmentBuilding"),
            getString("apartmentStreet"),
            getString("responsibleForQA"),
            getInt("apartmentCompletitionStatus"),
            raportDataset,
            getString("bluePrintURL")
        )
    }

    // Checks if a volume containing external storage is available
// for read and write.
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}
