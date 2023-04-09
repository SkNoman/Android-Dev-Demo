package com.example.crud.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crud.R
import com.example.crud.model.dashboard.MenusItem

class DashboardMainMenuAdapter(
    context: Context,
    menuList: List<MenusItem>,
    private var onMenuItemClick: OnClickMenu
)
    :RecyclerView.Adapter<MenuItemViewHolder>(){

    private var mContext: Context = context
    private val content: List<MenusItem> = menuList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MenuItemViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menuContent: MenusItem = content[position]
        holder.bind(mContext,menuContent)
        holder.itemView.setOnClickListener{
            menuContent.menuId?.let { it1 -> onMenuItemClick.onClick(it1) }
        }
    }

    override fun getItemCount(): Int {
        return content.size
    }
}
interface OnClickMenu{
    fun onClick(id:Int)
}
class MenuItemViewHolder(inflater: LayoutInflater,parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.item_home_main_menu,parent,false)){

    private var title: TextView = itemView.findViewById(R.id.menu_title)
    private var icon: ImageView = itemView.findViewById(R.id.menu_icon)

    fun bind(context: Context,menuData:MenusItem){
        title.text = menuData.menuTitle
        Glide.with(context).load(menuData.menuImage).placeholder(R.drawable.preloader).into(icon)
    }
}


