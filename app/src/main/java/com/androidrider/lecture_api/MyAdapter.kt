package com.androidrider.lecture_api

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class MyAdapter(val context: Activity, var productArrayList: List<Product>) :
RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.row_item2, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = productArrayList[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
//        holder.price.text = currentItem.price.toString()

        // Set price text with currency symbol
        val priceText = "₹ ${currentItem.price}"
        holder.price.text = priceText

        // Picasso for Image
        Picasso.get().load(currentItem.thumbnail).into(holder.image)

        // Convert rating to star rating
        updateStarIcons(holder.starIcons, currentItem.rating, context)

        holder.cardItem.setOnClickListener {
            // Handle the click event here
            val intent = Intent(context, CardDetailsActivity::class.java)
            // You can put extra data in the intent if needed
            // intent.putExtra("key", value)
             intent.putExtra("title", currentItem.title)
             intent.putExtra("description", currentItem.description)
             intent.putExtra("starIcons", currentItem.rating)
             intent.putExtra("image", currentItem.thumbnail)
             intent.putExtra("price", currentItem.price)
            context.startActivity(intent)
        }

    }

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        var cardItem: CardView

        var image : ShapeableImageView
        var title : TextView
        var description : TextView
        var price : TextView

        var starIcons: List<ImageView> // for star icon

        init {
            cardItem = itemView.findViewById(R.id.cardItem)

            image = itemView.findViewById(R.id.productImage)
            title = itemView.findViewById(R.id.productTitle)
            description = itemView.findViewById(R.id.productDescription)
            price = itemView.findViewById(R.id.productPrice)

            // Find the star icons
            starIcons = listOf(
                itemView.findViewById(R.id.star1),
                itemView.findViewById(R.id.star2),
                itemView.findViewById(R.id.star3),
                itemView.findViewById(R.id.star4),
                itemView.findViewById(R.id.star5)
            )

        }
    }

    //    star icon
    companion object{
        fun updateStarIcons(starIcons: List<ImageView>, rating: Double, context:Context) {

            val fullStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_star_filled)!!
            val halfStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_star_half)!!
            val emptyStarDrawable = ContextCompat.getDrawable(context, R.drawable.ic_star_empty)!!

            val integerPart = rating.toInt()
            val decimalPart = rating - integerPart

            for (i in 0 until 5) {
                if (i < integerPart) {
                    starIcons[i].setImageDrawable(fullStarDrawable)
                } else if (i == integerPart && decimalPart >= 0.25) {
                    starIcons[i].setImageDrawable(halfStarDrawable)
                } else {
                    starIcons[i].setImageDrawable(emptyStarDrawable)
                }
            }
        }
    }

    // Add a new method to update the filtered list
    fun filterList(filteredList: List<Product>) {
        productArrayList= filteredList
        notifyDataSetChanged()
    }



}