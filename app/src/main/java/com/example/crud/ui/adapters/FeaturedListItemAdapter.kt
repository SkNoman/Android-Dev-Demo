package com.example.crud.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.R
import com.example.crud.model.dashboard.FeaturedItem

class FeaturedListItemAdapter(
    context: Context,
    featuredList: ArrayList<FeaturedItem>
):RecyclerView.Adapter<FeaturedItemViewHolder>(){

    private var mContext: Context = context
    private val content: ArrayList<FeaturedItem> = featuredList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FeaturedItemViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: FeaturedItemViewHolder, position: Int) {
        val featuredContent: FeaturedItem = content[position]
        holder.bind(mContext,featuredContent)
    }

    override fun getItemCount(): Int {
        return content.size
    }
}

class FeaturedItemViewHolder(inflater: LayoutInflater,parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.item_featured_menu_list,parent,false)){

    private var title: TextView = itemView.findViewById(R.id.fTitle)
    private var icon: ImageView = itemView.findViewById(R.id.fImage)
    fun bind(context: Context,featuredItem: FeaturedItem){
        title.text = featuredItem.title
        icon.setImageResource(featuredItem.image)
    }
}
