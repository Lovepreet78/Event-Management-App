package com.example.eventmanagement.eventactivities.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagement.eventactivities.EventDetail
import com.example.eventmanagement.R
import com.example.eventmanagement.eventmodel.EventDTO

class EventsRecyclerView(private val events:List<EventDTO>, private val context: Context): RecyclerView.Adapter<EventsRecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = events[position]
        holder.eventItemTitle.text = currentItem.title
        holder.eventItemDescription.text = currentItem.content

        holder.itemView.setOnClickListener {
            val intentToEventDetail = Intent(context, EventDetail::class.java)
            intentToEventDetail.putExtra("title",holder.eventItemTitle.text.toString())
            intentToEventDetail.putExtra("content",holder.eventItemDescription.text.toString())
            intentToEventDetail.putExtra("startDay",currentItem.startDay)
            intentToEventDetail.putExtra("endDay",currentItem.endDay)
            intentToEventDetail.putExtra("location",currentItem.location)


            intentToEventDetail.putExtra("registrationLink",currentItem.enrollmentLink)
            intentToEventDetail.putExtra("imageLink",currentItem.imageLink)

            context.startActivity(intentToEventDetail)



        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventItemTitle: TextView = itemView.findViewById(R.id.eventItemTitle)
        val eventItemDescription: TextView = itemView.findViewById(R.id.eventItemDescription)

    }
}









