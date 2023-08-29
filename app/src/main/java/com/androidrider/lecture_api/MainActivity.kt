package com.androidrider.lecture_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidrider.lecture_api.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var myAdapter: MyAdapter
    lateinit var originalProductList: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        setSupportActionBar(toolbar)

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

//                originalProductList = productList.toList() // Initialize current list
                originalProductList = productList

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                filter(newText.orEmpty()) // Call the filter function with the search query

                return true
            }
        })
        return true
    }

    private fun filter(text: String) {
        val filteredList = if (text.isBlank()) {
            originalProductList// Show all items when the query is blank
        } else {
            originalProductList.filter {
                it.title.lowercase(Locale.ROOT).contains(text.lowercase(Locale.getDefault())) ||
                        it.description.lowercase(Locale.ROOT).contains(text.lowercase(Locale.getDefault()))
            }
        }

        myAdapter.filterList(filteredList)
    }


//    private fun filter(text: String) {
//        val filteredList = myAdapter.productArrayList.toMutableList() // Convert to a mutable list
//
//        if (text.isBlank()) {
//            myAdapter.filterList(filteredList) // Show all items when the query is blank
//        } else {
//            filteredList.retainAll {
//                it.title.lowercase(Locale.ROOT).contains(text.lowercase(Locale.getDefault())) ||
//                        it.description.lowercase(Locale.ROOT)
//                            .contains(text.lowercase(Locale.getDefault()))
//            }
//                myAdapter.filterList(filteredList) // Update the adapter with the filtered list
//        }
//
//    }
}
