package com.leonkianoapps.d4a.leonard.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.leonkianoapps.d4a.leonard.R
import kotlinx.android.synthetic.main.recycler_item_layout.view.*

class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val linearLayout: LinearLayout =  itemView.findViewById(R.id.recycler_layout)
    val titleTextView: TextView = itemView.findViewById(R.id.blog_recycler_item_title)
    val authorTextView: TextView = itemView.findViewById(R.id.author_recycler_item)
    val dateTextView: TextView = itemView.findViewById(R.id.date_recycler_item)



}