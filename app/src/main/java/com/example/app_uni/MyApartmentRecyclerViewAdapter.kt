package com.example.app_uni

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.example.app_uni.ApartmentFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_apartment.view.*

/**
 * [RecyclerView.Adapter] that can display a [ApartmentClass] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyApartmentRecyclerViewAdapter(
    private var mValues: MutableList<ApartmentClass>,
    private val mListener: OnItemClickListener?
) : RecyclerView.Adapter<MyApartmentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_apartment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item, mListener)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }

        fun bind(apartment:ApartmentClass, clickListener: OnItemClickListener?){
            mIdView.text = apartment.apartmentStreet+", д. "+apartment.apartmentBuilding+", кв. "+apartment.apartmentNumber
            mContentView.text = "Завершена на: "+apartment.apartmentCompletitionStatus+" %"

            itemView.setOnClickListener {
                clickListener?.onItemClicked(apartment)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClicked(apartment: ApartmentClass)
    }
}


