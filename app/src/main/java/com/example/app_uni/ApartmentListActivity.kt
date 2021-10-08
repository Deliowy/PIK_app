package com.example.app_uni

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.InputDevice
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_apartment_list.*

class ApartmentListActivity : AppCompatActivity(),
    MyApartmentRecyclerViewAdapter.OnItemClickListener
    {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var currentDataset:MutableList<ApartmentClass>

    object testApartmentDataset {
        var apartmentDataset: MutableList<ApartmentClass> = mutableListOf(
            ApartmentClass(1, 1, "S1", "Pr1", 10, bluePrintURL = "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fzapadnyj-gorod.nashdom123.ru%2Fuserfiles%2Fflats%2F8a646e85aea99474f49a87c76c5cd838.jpg&f=1&nofb=1"),
            ApartmentClass(2, 1, "S1", "Pr1", 0, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fru-static.z-dn.net%2Ffiles%2Fda1%2F976a757a965611734904784ff1c28aa1.jpg&f=1&nofb=1"),
            ApartmentClass(1, 2, "S1", "Pr3", 30, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fremplanner.ru%2Fimages%2Ffront%2Fportfolio%2F5%2Fplans%2Ffullsize%2F1522304564.png&f=1&nofb=1"),
            ApartmentClass(2, 2, "S1", "Pr2", 20, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdngeneral.rentcafe.com%2Fdmslivecafe%2F3%2F439663%2F3_439663_2664863.jpg%3Fquality%3D85&f=1&nofb=1"),
            ApartmentClass(1, 1, "S2", "Pr4", 40, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fproekt-sam.ru%2Fwp-content%2Fuploads%2Fplanirovka-trehkomnatnoj-kvartiry2-434x300.jpg&f=1&nofb=1"),
            ApartmentClass(2, 1, "S2", "Pr2", 50, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.artisanhill.net%2Fwp-content%2Fuploads%2F2018%2F07%2Fs1-dimensions.jpg&f=1&nofb=1"),
            ApartmentClass(1, 2, "S2", "Pr3", 60, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.art-designs.ru%2Fassets%2Fimages%2Fportfolio%2Fflat-10%2F3-obmer-chistova-16.jpg&f=1&nofb=1"),
            ApartmentClass(2, 2, "S2", "Pr4", 70, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.adsttc.com%2Fmedia%2Fimages%2F512d%2F1a83%2Fb3fc%2F4b81%2F4d00%2F0022%2Flarge_jpg%2FDuplex_Floor_Plan_01.jpg%3F1361910394&f=1&nofb=1"),
            ApartmentClass(1, 1, "S3", "Pr4", 80, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn-nus-1.pinme.ru%2Ftumb%2F600%2Fphoto%2Fc7%2F2d2c%2Fc72d2c2470c30527b37c6efec2ea41a6.jpeg&f=1&nofb=1"),
            ApartmentClass(1, 2, "S3", "Pr4", 90, bluePrintURL ="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fthehamletsatvernon.ca%2Fwp-content%2Fuploads%2F2017%2F01%2Ffloorplan_02.jpg&f=1&nofb=1")
        )
    }

        override fun onItemClicked(apartment: ApartmentClass) {
            val intent = Intent(this, ApartmentActivity::class.java)
            intent.putExtra("Apartment", apartment.toBundle())
            Log.i("Apartment_",apartment.getApartmentInfo().toString())
            startActivity(intent)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apartment_list)
        currentDataset=testApartmentDataset.apartmentDataset
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyApartmentRecyclerViewAdapter(currentDataset, this)

        recyclerView = findViewById<RecyclerView>(R.id.apartment_list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }



    fun testSearch(view:View){
        search_field_apartment.clearFocus()
        search_field_street.clearFocus()
        search_field_building.clearFocus()
        //showProgress(true)
        SearchApartmentsTask().execute(search_field_apartment.text.toString(), search_field_building.text.toString(), search_field_street.text.toString())
        viewAdapter = MyApartmentRecyclerViewAdapter(currentDataset, this)
        recyclerView = findViewById<RecyclerView>(R.id.apartment_list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

        fun message(message:String?){
            Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show()
        }

        inner class SearchApartmentsTask:AsyncTask<String, Void, String>(){

            override fun doInBackground(vararg params: String?):String? {
                try
                {
                    Thread.sleep(2000)
                    if (params[0]!!.isEmpty()&&params[1]!!.isEmpty()&&params[2]!!.isEmpty()){
                        currentDataset=testApartmentDataset.apartmentDataset
                    } else{
                        var tempApartmentDataset: MutableList<ApartmentClass> = mutableListOf()
                        if (params[0]!!.isEmpty()){
                            if (params[1]!!.isEmpty()){
                                for (apartment in testApartmentDataset.apartmentDataset) {
                                    if (apartment.apartmentStreet == params[2]!!) tempApartmentDataset.add(apartment)
                                }
                            } else{
                                if (params[2]!!.isEmpty()){
                                    for (apartment in testApartmentDataset.apartmentDataset) {
                                        if (apartment.apartmentBuilding == params[1]!!.toInt()) tempApartmentDataset.add(apartment)
                                    }
                                } else {
                                    for (apartment in testApartmentDataset.apartmentDataset) {
                                        if ((apartment.apartmentNumber == params[1]!!.toInt())&&(apartment.apartmentStreet == params[2]!!)) tempApartmentDataset.add(apartment)
                                    }
                                }
                            }
                        } else {
                            if (params[1]!!.isEmpty()){
                                if (params[2]!!.isEmpty()){
                                    for (apartment in testApartmentDataset.apartmentDataset) {
                                        if (apartment.apartmentNumber == params[0]!!.toInt()) tempApartmentDataset.add(apartment)
                                    }
                                } else {
                                    for (apartment in testApartmentDataset.apartmentDataset) {
                                        if ((apartment.apartmentNumber == params[0]!!.toInt())&&(apartment.apartmentStreet == params[2]!!)) tempApartmentDataset.add(apartment)
                                    }
                                }
                            } else{
                                if (params[2]!!.isEmpty()){
                                    for (apartment in testApartmentDataset.apartmentDataset) {
                                        if ((apartment.apartmentNumber == params[0]!!.toInt())&&(apartment.apartmentBuilding == params[1]!!.toInt())) tempApartmentDataset.add(apartment)
                                    }
                                } else {
                                    for (apartment in testApartmentDataset.apartmentDataset) {
                                        if ((apartment.apartmentNumber == params[0]!!.toInt())&&(apartment.apartmentStreet == params[2]!!)&&(apartment.apartmentBuilding == params[1]!!.toInt())) tempApartmentDataset.add(apartment)
                                    }
                                }
                            }
                        }
                        if (tempApartmentDataset.isEmpty()) {
                            currentDataset=testApartmentDataset.apartmentDataset
                            return "Не найдены квартиры, соответствующие критериям поиска"
                        }
                        else currentDataset=tempApartmentDataset
                    }
                    return null
                } catch (e: InterruptedException) {
                    Thread.sleep(2000)
                    return "Something's wrong"
                }

            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                showProgress(false)
                if (result!=null){
                message(result)
                }
            }
        }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            searchBar.visibility = if (show) View.GONE else View.VISIBLE
            searchBar.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        searchBar.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })
            include.visibility = if (show) View.GONE else View.VISIBLE
            include.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        include.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })
            search_button.visibility = if (show) View.GONE else View.VISIBLE
            search_button.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        search_button.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            search_progress.visibility = if (show) View.VISIBLE else View.GONE
            search_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        search_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            search_progress.visibility = if (show) View.VISIBLE else View.GONE
            searchBar.visibility = if (show) View.GONE else View.VISIBLE
            include.visibility = if (show) View.GONE else View.VISIBLE
            search_button.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
    }
