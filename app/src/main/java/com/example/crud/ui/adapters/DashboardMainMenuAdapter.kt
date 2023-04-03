package com.example.crud.ui.adapters

import android.content.Context
import android.text.Html.ImageGetter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crud.R
import com.example.crud.model.dashboard.MenusItem

class DashboardMainMenuAdapter(context: Context,menuList: List<MenusItem>)
    :RecyclerView.Adapter<MenuItemViewHolder>(){

    private val content: List<MenusItem> = menuList
    var mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MenuItemViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menuContent: MenusItem = content[position]
        holder.bind(mContext,menuContent)
    }

    override fun getItemCount(): Int {
        return content.size
    }

}
class MenuItemViewHolder(inflater: LayoutInflater,parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.item_home_main_menu,parent,false)){

    var title: TextView = itemView.findViewById(R.id.menu_title)
    var icon: ImageView = itemView.findViewById(R.id.menu_icon)

    fun bind(context: Context,menuData:MenusItem){
        title.text = menuData.menuTitle
        Glide.with(context).load(menuData.menuImage).placeholder(R.drawable.preloader).into(icon)
    }
}


