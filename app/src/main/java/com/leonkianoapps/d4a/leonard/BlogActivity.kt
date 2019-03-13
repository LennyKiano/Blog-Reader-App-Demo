package com.leonkianoapps.d4a.leonard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_blog.*

class BlogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        //setting up toolbar

        setSupportActionBar(blog_toolBar)
        supportActionBar!!.title = "Blog Post"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //getting passed data from the previous activity

        val receivedIntent = intent

        blog_view_title.text = receivedIntent.getStringExtra("title")
        blog_view_author.text = receivedIntent.getStringExtra("author")
        blog_view_date.text = receivedIntent.getStringExtra("date")
        blogContentTextView.text = receivedIntent.getStringExtra("content")


    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId){

            android.R.id.home -> {

                onBackPressed()

            }


        }


        return super.onOptionsItemSelected(item)
    }

}
