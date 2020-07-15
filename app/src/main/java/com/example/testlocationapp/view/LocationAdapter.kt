package com.example.testlocationapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testlocationapp.R
import com.example.testlocationapp.data.model.Pickup
import kotlinx.android.synthetic.main.item_location.view.*

class LocationAdapter(private var context: Context, private var locationList: List<Pickup>) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(locationList.isNullOrEmpty()) 0 else locationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.onBind(locationList[position],position)
        holder.itemView.setOnClickListener(View.OnClickListener {

        })
    }

    fun setData(data:List<Pickup>){
        locationList = data
        notifyDataSetChanged()
    }

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(location: Pickup, position: Int) {
            itemView.textLocationAlias.text = if(!location.alias.isBlank()) location.alias
            else "---"

            itemView.textLocationAddress.text = if(!location.address1.isBlank()) location.address1
            else if(!location.address2.isBlank()) location.address2
            else "---"

            itemView.textLocationCity.text = if(!location.city.isBlank()) location.city
            else "---"
        }
    }
}