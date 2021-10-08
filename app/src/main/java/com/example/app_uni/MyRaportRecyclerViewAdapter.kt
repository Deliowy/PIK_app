package com.example.app_uni

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView


import com.example.app_uni.RaportFragment.OnListFragmentInteractionListener
import com.example.app_uni.RaportActivity.RaportItem

import kotlinx.android.synthetic.main.fragment_raport.view.*
import kotlinx.android.synthetic.main.raport_recycler_view_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [RaportItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyRaportRecyclerViewAdapter(
    private val mValues: List<RaportItem>,
    private val mListener: OnItemClickListener?
) : RecyclerView.Adapter<MyRaportRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_raport, parent, false)
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
        var mCheckBox:CheckBox=mView.raport_item_checkbox

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }

        fun bind(raportTask:RaportItem, clickListener: OnItemClickListener?){
            mIdView.text = raportTask.Task
            mContentView.text = if(raportTask.Status) "Complete" else "Incomplete"
            mCheckBox.isChecked=raportTask.Status
            mCheckBox.setOnClickListener{
                clickListener?.onItemClicked(raportTask, mCheckBox)
            }
            //itemView.setOnClickListener {
            //    clickListener?.onItemClicked(raportTask)
            //}
        }
    }
    interface OnItemClickListener{
        fun onItemClicked(raportTask: RaportItem, checkBox: CheckBox)
    }
}
