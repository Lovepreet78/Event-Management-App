package com.example.eventmanagement.managementrole.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventmanagement.R
import com.example.eventmanagement.managementrole.ManagementEventDetail
import com.example.eventmanagement.managementrole.managementeventmodel.ManagementEventDTO

class ManagementEventAdapter(private val events:List<ManagementEventDTO>, private val context: Context): RecyclerView.Adapter<ManagementEventAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = events[position]
        holder.eventItemTitle.text = currentItem.title
        holder.eventItemDescription.text = currentItem.content
        if(currentItem.imageLink=="" || currentItem.imageLink==null){
            holder.eventImageView.setImageResource(R.drawable.event_detail_image)
        }
        else{
            Glide.with(context).load(currentItem.imageLink).into(holder.eventImageView);
        }

        holder.itemView.setOnClickListener {
            val intentToEventDetail = Intent(context, ManagementEventDetail::class.java)
            intentToEventDetail.putExtra("title",holder.eventItemTitle.text.toString())
            intentToEventDetail.putExtra("content",holder.eventItemDescription.text.toString())
            intentToEventDetail.putExtra("startDay",currentItem.startDay)
            intentToEventDetail.putExtra("endDay",currentItem.endDay)
            intentToEventDetail.putExtra("location",currentItem.location)
            intentToEventDetail.putExtra("id",currentItem.id.toString())
            intentToEventDetail.putExtra("startTime",currentItem.startTime)
            intentToEventDetail.putExtra("endTime",currentItem.endTime)
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
        val eventImageView: ImageView = itemView.findViewById(R.id.imageView)
    }

}