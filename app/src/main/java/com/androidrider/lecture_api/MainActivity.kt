package com.androidrider.lecture_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrider.lecture_api.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView
        val progressBar = binding.progressBar
// Step-4
        /* step-4.1 */
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        /* step-4.2 */
        val retrofitData = retrofitBuilder.getProductData()
        progressBar.visibility = View.VISIBLE // Show progress bar
        /* step-4.3 */
        retrofitData.enqueue(object : Callback<MyData?> {

            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {

                val responseBody = response.body()
                val productList = responseBody?.products!!

                myAdapter = MyAdapter(this@MainActivity, productList)
                recyclerView.adapter = myAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                progressBar.visibility = View.GONE // Hide progress bar on success

            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {

                Log.d("MainActivity", "onFailure" + t.message)
                progressBar.visibility = View.GONE // Hide progress bar on failure
            }
        }) // Ctrl+Shift+Space - inside enqueue bracket

    }
}


/* Fetching only title- meanWhile single string */

//val collectDataInStringBuilder = StringBuilder()
//
//for (myData in productList){
//    collectDataInStringBuilder.append(myData.title + "\n")
//    binding.textView.text = collectDataInStringBuilder
//
//}