package com.example.crud.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.R
import com.example.crud.model.SlideItem
import com.makeramen.roundedimageview.RoundedImageView


class SlideItemAdapter(
    slideItemList: ArrayList<SlideItem>
): RecyclerView.Adapter<SlideItemViewHolder>() {

    private val content: ArrayList<SlideItem> = slideItemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SlideItemViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: SlideItemViewHolder, position: Int) {
        val slideContent: SlideItem = content[position]
        holder.bind(slideContent,position,content,viewPagerRunnable)

    }
    private val viewPagerRunnable = Runnable {
        slideItemList.addAll(slideItemList)
        notifyItemChanged(content.size)
    }
}

class SlideItemViewHolder(inflater: LayoutInflater,parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.slide_item_container,parent,false)){
    private var imageView: RoundedImageView = itemView.findViewById(R.id.imageSlide)

    fun bind(
        slideItemList: SlideItem,
        position: Int,
        content: ArrayList<SlideItem>,
        viewPagerRunnable: Runnable
    ){
        imageView.setImageResource(slideItemList.image)
        if (position == content.size -2){
            itemView.post(viewPagerRunnable)
        }

    }
}
