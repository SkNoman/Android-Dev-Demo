package com.example.crud.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crud.R
import com.example.crud.model.menu.CarListItem

class CarListAdapter(context: Context, carList: List<CarListItem?>, private var onCarClickCar: OnClickCar)
    : RecyclerView.Adapter<CarListItemViewHolder>()
{
    private var mContext: Context = context
    private val content : List<CarListItem?> = carList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CarListItemViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: CarListItemViewHolder, position: Int) {
        val menuContent: CarListItem? = content[position]
        holder.bind(mContext,menuContent)
        holder.itemView.setOnClickListener{
            onCarClickCar.onClick(menuContent!!.carId!!)
        }
    }
}
interface OnClickCar{
    fun onClick(id:Int)
}

class CarListItemViewHolder(inflater: LayoutInflater,parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_cars_list,parent,false)){

    private var carName: TextView = itemView.findViewById(R.id.txtCarName)
    private var carMoto: TextView = itemView.findViewById(R.id.txtCarMoto)
    private var carIcon: ImageView = itemView.findViewById(R.id.ivCar)
    fun bind(context: Context,carListData: CarListItem?){
        carName.text = carListData!!.carName
        carMoto.text = carListData.carType
        Glide.with(context).load(carListData.carImage)
            .placeholder(R.drawable.preloader).into(carIcon)
    }
}
