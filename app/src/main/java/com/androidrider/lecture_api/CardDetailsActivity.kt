package com.androidrider.lecture_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.androidrider.lecture_api.databinding.ActivityCardDetailsBinding
import com.androidrider.lecture_api.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class CardDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCardDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the extra data from the intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val rating = intent.getDoubleExtra("starIcons", 0.0)
        val imageUrl = intent.getStringExtra("image")
        val price = intent.getIntExtra("price", 0)

        // Set the received data to your activity views
        binding.productTitle.text = title
        binding.productDescription.text = description
        binding.productPrice.text = "₹ $price"
        Picasso.get().load(imageUrl).into(binding.productImage)

        // Find the star icons in the CardDetailsActivity layout
        var starIcons: List<ImageView> = listOf(
            findViewById(R.id.star1),
            findViewById(R.id.star2),
            findViewById(R.id.star3),
            findViewById(R.id.star4),
            findViewById(R.id.star5)
        )
        // for star icon
        MyAdapter.updateStarIcons(starIcons, rating, this)
    }

}