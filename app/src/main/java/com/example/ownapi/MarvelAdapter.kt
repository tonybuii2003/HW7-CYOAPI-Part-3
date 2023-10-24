package com.example.ownapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MarvelAdapter(private val marvelList: List<String>, private val nameList: List<String>, private val idList: List<String>) : RecyclerView.Adapter<MarvelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val marvelImage: ImageView
        val marvelID: TextView
        val marvelName: TextView

        init {
            // Find our RecyclerView item's ImageView for future use
            marvelImage = view.findViewById(R.id.marvel_image)
            marvelID = view.findViewById(R.id.textID)
            marvelName = view.findViewById(R.id.textName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.marvel_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = marvelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(marvelList[position])
            .centerCrop()
            .into(holder.marvelImage)
        holder.marvelID.text = idList[position]
        holder.marvelName.text = nameList[position]
    }
}
