package com.leonkianoapps.d4a.leonard.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leonkianoapps.d4a.leonard.BlogActivity
import com.leonkianoapps.d4a.leonard.PostBasicInfo
import com.leonkianoapps.d4a.leonard.R
import com.leonkianoapps.d4a.leonard.viewHolders.CustomViewHolder

class RecyclerViewCustomAdapter(val dataList: ArrayList<PostBasicInfo>, val context: Context, val rootLayout: View) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CustomViewHolder {

        //inflate the recyclerView layout item here

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_layout, parent, false)


        return CustomViewHolder(view)


    }

    override fun getItemCount(): Int {

        //return the size of the arrayList here

        return dataList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, postion: Int) {

        val postInfo: PostBasicInfo = dataList[postion]

        holder.titleTextView.text = postInfo.title
        holder.authorTextView.text = postInfo.authorName
        holder.dateTextView.text = postInfo.date


        holder.linearLayout.setOnClickListener { it ->

            //open blog page here in another activity

            val intent = Intent(context, BlogActivity::class.java)

            val blogTitle = postInfo.title
            val blogAuthor = postInfo.authorName
            val blogDate = postInfo.date
            val blogContent = postInfo.blogContent

            intent.putExtra("title",blogTitle)
            intent.putExtra("author",blogAuthor)
            intent.putExtra("date",blogDate)
            intent.putExtra("content",blogContent)

            context.startActivity(intent)



        }


    }
}