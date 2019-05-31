package com.leonkianoapps.d4a.leonard

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.leonkianoapps.d4a.leonard.adapters.RecyclerViewCustomAdapter
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.lang.reflect.Type


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var drawerToggle: ActionBarDrawerToggle? = null

    private var initialLaunch = true

    var adapter : RecyclerViewCustomAdapter? = null

    private val fileCacheTextFile = "D4ALeonardData.txt"





    private var postsInfoArrayList = ArrayList<PostBasicInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting up tool bar
        setSupportActionBar(main_toolBar)


        //setting up Nav Drawer
        setupNavDrawer()

        //check if there is data in cache
        loadDataFromCache()



        //Swipe to Refresh Layout init
        swipeLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorAccent)

        swipeLayout.setOnRefreshListener {
            initialLaunch = false
            getData()
        }




    }

    private fun setupNavDrawer() {

        main_nav_view.setNavigationItemSelectedListener(this)

        //for hamburger icon
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            main_toolBar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawer_layout.addDrawerListener(drawerToggle!!)
        drawerToggle!!.syncState()

    }

    private fun showNetworkNotFoundInfo(){ errorMessageTextView.visibility = View.VISIBLE }


    private fun getData() {

        if (checkInternetConnection()){

            //Do network Request

            if (initialLaunch){

                //show progress bar and snack bar on launch
                //show progress bar and SnackBar while data is being fetched

                progress_circular.visibility = View.VISIBLE

                showSnack("Loading...")

                //Using OkHttp Lib

                val client = OkHttpClient()

                val url = "https://digital4africa.com/api/get_recent_posts/"

                val request: okhttp3.Request = okhttp3.Request.Builder().url(url).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                        showToast("OKHTTP FAIL")
                    }

                    override fun onResponse(call: Call, response: okhttp3.Response) {

                        if (response.isSuccessful) {

                            val jsonObject = JSONObject(response.body()?.string()) //creating a JSONObject From the response

                            val jsonArray = jsonObject.getJSONArray("posts") //creating a JSONArray to get the post

                            var blogTitle = ""
                            var blogDate = ""
                            var blogAuth = ""
                            var blogcontent = ""

                            for (element in 0 until jsonArray.length()) {  //looping through the array to get the blog info

                                val jsonInner: JSONObject = jsonArray.getJSONObject(element)

                                blogTitle = jsonInner.getString("title")
                                blogDate = jsonInner.getString("date")
                                blogcontent = jsonInner.getString("content")

                                val jsonObjectAuthor = jsonInner.getJSONObject("author")

                                blogAuth = jsonObjectAuthor.getString("name")


                                val postInfo = PostBasicInfo(blogTitle, blogDate, blogAuth, blogcontent)


                                postsInfoArrayList.add(postInfo)   //adding info to arrayList for RecyclerView Adapter


                            }




                            runOnUiThread(   //we have to run to the main ui since enqueue runs in the background
                                object : Runnable {
                                    override fun run() {

                                        //Hide progress bar while data is being fetched

                                        progress_circular.visibility = View.INVISIBLE

                                        initializeRecyclerView(postsInfoArrayList)

                                        //save data to cache to be used later
                                        saveDataToCache(postsInfoArrayList)


                                    }
                                }
                            )

                        } else {

                            runOnUiThread(
                                object : Runnable {
                                    override fun run() {

                                        showToast("OK_HTTP2 FAIL")

                                    }
                                }
                            )
                        }

                    }
                })


            } else {

                //swipe to refresh was triggered therefore get new data

                //Using OkHttp Lib

                val client = OkHttpClient()

                val url = "https://digital4africa.com/api/get_recent_posts/"

                val request: okhttp3.Request = okhttp3.Request.Builder().url(url).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                        showToast("OKHTTP FAIL")
                    }

                    override fun onResponse(call: Call, response: okhttp3.Response) {

                        if (response.isSuccessful) {

                            val jsonObject = JSONObject(response.body()?.string()) //creating a JSONObject From the response

                            val jsonArray = jsonObject.getJSONArray("posts") //creating a JSONArray to get the post

                            var blogTitle = ""
                            var blogDate = ""
                            var blogAuth = ""
                            var blogcontent = ""

                            for (element in 0 until jsonArray.length()) {  //looping through the array to get the blog info

                                val jsonInner: JSONObject = jsonArray.getJSONObject(element)

                                blogTitle = jsonInner.getString("title")
                                blogDate = jsonInner.getString("date")
                                blogcontent = jsonInner.getString("content")

                                val jsonObjectAuthor = jsonInner.getJSONObject("author")

                                blogAuth = jsonObjectAuthor.getString("name")


                                val postInfo = PostBasicInfo(blogTitle, blogDate, blogAuth, blogcontent)


                                postsInfoArrayList.add(postInfo)   //adding info to arrayList for RecyclerView Adapter


                            }




                            runOnUiThread(   //we have to run to the main ui since enqueue runs in the background
                                object : Runnable {
                                    override fun run() {

                                        //stop showing swipe loader
                                        swipeLayout.isRefreshing = false

                                        //notify adapter that data has changed

                                        adapter?.notifyDataSetChanged()

//                                    initializeRecyclerView(postsInfoArrayList)

                                        //save data to cache to be used later
                                        saveDataToCache(postsInfoArrayList)


                                    }
                                }
                            )

                        } else {

                            runOnUiThread(
                                object : Runnable {
                                    override fun run() {

                                        showToast("OK_HTTP2 FAIL")

                                    }
                                }
                            )
                        }

                    }
                })


            }



        }else {

            showSnack("Make sure you have Internet Connection to show Latest posts")
            swipeLayout.isRefreshing  =  false

        }




    }

    private fun initializeRecyclerView(arrayList: ArrayList<PostBasicInfo>) {

        val linearLayoutManger = LinearLayoutManager(this@MainActivity)


        posts_recyclerView.layoutManager = linearLayoutManger


        posts_recyclerView.setHasFixedSize(true)  //since the size of the RecyclerView doesn't depend on the adapter content

         adapter = RecyclerViewCustomAdapter(arrayList, this@MainActivity, main_UI_layout)

        posts_recyclerView.adapter = adapter

    }



    private fun checkInternetConnection(): Boolean {

        //check if there is internet connection first
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        return activeNetworkInfo != null && activeNetworkInfo.isConnected


    }


//    navigation Drawer Setup

    //needs to be called for the drawer toggle
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        drawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        val itemSelected = menuItem.itemId

        navigate(itemSelected)

        return true


    }

    //This method check which item in the navigation drawer was selected and opens the corresponding activity


    private fun navigate(itemSelected: Int) {

        var intent: Intent? = null



        when (itemSelected) {

            //navigation view items

            R.id.all_Posts_nav_menu -> {
                showSnack("All Posts, Coming Soon On a later Update :)")
                drawer_layout.closeDrawer(Gravity.START)
            }

            R.id.about_nav_item_menu -> {
                showSnack("About Info, Coming Soon On a later Update :)")
                drawer_layout.closeDrawer(Gravity.START)
            }

        }
    }


    //Toast Method

    private fun showToast(message: String) {

        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()

    }


    //Snack Method

    private fun showSnack(messageInfo: String) {

        val snack = Snackbar.make(main_UI_layout, messageInfo, Snackbar.LENGTH_LONG)
            .setAction("OKAY", View.OnClickListener {
            })
        snack.show()

    }

    //Saving data to Internal Cache

    private fun saveDataToCache(arrayList : ArrayList<PostBasicInfo>){

        //using Gson to convert the arrayList to a string to be store in the cache

        val gson =  Gson()

        val data : String = gson.toJson(arrayList)



        val folder : File = cacheDir  //getting apps cache Directory
        val file  = File(folder,fileCacheTextFile)  //Creating file where data will be stored

        var fileOutputStream : FileOutputStream? = null

        try {  //because the file might not be found

            fileOutputStream = FileOutputStream(file)  // Used to write to the file
            fileOutputStream.write(data.toByteArray())

            Log.i("MainActivity DataCache", "DATA : $data was written Successfully to ${file.absolutePath}")

        }catch (e: FileNotFoundException){

            e.printStackTrace()

        } finally {

            fileOutputStream?.close()   //if not null means the stream is open and must be closed
        }

    }

    private fun loadDataFromCache(){

        val folder : File= cacheDir

        val file = File(folder,fileCacheTextFile)

        if(file.exists()){


            val loadDateFromCache = LoadDateFromCache(file)

            val loadedData : String = loadDateFromCache.loadInfo()

            Log.i("MainActivity DataLOAD", "DATALOAD : $loadedData was loaded Successfully to ${file.absolutePath}")


            val gson = Gson()

            val type = object : TypeToken<ArrayList<PostBasicInfo>>() {

            }.type

            postsInfoArrayList = gson.fromJson(loadedData,type)

            if(postsInfoArrayList.isEmpty()){

                if(checkInternetConnection()){
                    getData()  //Get Data From Api
                    errorMessageTextView.visibility =  View.INVISIBLE

                }else{ showNetworkNotFoundInfo() }

            }else{

                initializeRecyclerView(postsInfoArrayList)

                //to get new data after user has seen cached data

                swipeLayout.isRefreshing  =  true

                if(checkInternetConnection()){

                    initialLaunch = false
                    getData()  //Get Data From Api
                    errorMessageTextView.visibility =  View.INVISIBLE

                }else{  showSnack("Make sure you have Internet Connection to show Latest posts")
                        swipeLayout.isRefreshing  =  false

                }
            }

        } else {

            if(checkInternetConnection()){
                getData()  //Get Data From Api
                errorMessageTextView.visibility =  View.INVISIBLE

            }else{ showNetworkNotFoundInfo() }

        }

    }

}
