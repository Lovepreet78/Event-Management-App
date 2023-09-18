package com.example.eventmanagement.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagement.EventDetail
import com.example.eventmanagement.R
import com.example.eventmanagement.eventmodel.Content
import com.example.eventmanagement.eventmodel.EventModel

class EventsRecyclerView(val events:List<Content>, val context: Context): RecyclerView.Adapter<EventsRecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = events[position]
        holder.eventItemTitle.text = currentItem.title
        holder.eventItemDescription.text = currentItem.content

        holder.itemView.setOnClickListener {
            val intentToEventDetail = Intent(context,EventDetail::class.java)
            intentToEventDetail.putExtra("title",holder.eventItemTitle.text.toString())
            intentToEventDetail.putExtra("content",holder.eventItemDescription.text.toString())
            intentToEventDetail.putExtra("startDay",currentItem.startDay)
            intentToEventDetail.putExtra("endDay",currentItem.endDay)
            intentToEventDetail.putExtra("location",currentItem.location)

            context.startActivity(intentToEventDetail)



        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventItemTitle = itemView.findViewById<TextView>(R.id.eventItemTitle)
        val eventItemDescription = itemView.findViewById<TextView>(R.id.eventItemDescription)

    }
}









//
//class EventsRecyclerView(val events:List<EventModel>,val context: Context): RecyclerView.Adapter<EventsRecyclerView.ViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val currentItem = events[position]
//        holder.eventItemTitle.text = currentItem.content[0]
//        holder.eventItemDescription.text = currentItem.content
//
////        holder.itemView.setOnClickListener {
////            val intentToEventDetail = Intent(context,EventDetail::class.java)
////            intentToEventDetail.putExtra("title",holder.eventItemTitle.text.toString())
////            intentToEventDetail.putExtra("content",holder.eventItemDescription.text.toString())
////            intentToEventDetail.putExtra("startDay",currentItem.startDay)
////            intentToEventDetail.putExtra("endDay",currentItem.endDay)
////            intentToEventDetail.putExtra("location",currentItem.location)
////
////            context.startActivity(intentToEventDetail)
////
////
////
////        }
//    }
//
//    override fun getItemCount(): Int {
//        return events.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val eventItemTitle = itemView.findViewById<TextView>(R.id.eventItemTitle)
//        val eventItemDescription = itemView.findViewById<TextView>(R.id.eventItemDescription)
//
//    }
//}