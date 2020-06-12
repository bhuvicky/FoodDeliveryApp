package com.example.doodbluetask.mycart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doodbluetask.R
import com.example.doodbluetask.model.FoodMenu
import com.example.doodbluetask.model.SelectedFoodMenuList


class MyCartAdapter(private val context: Context,
                    private var employeeList: SelectedFoodMenuList = SelectedFoodMenuList(),
                    private val clickListener: (Int, FoodMenu) -> Unit):
    RecyclerView.Adapter<MyCartAdapter.EmployeeViewHolder>() {

    fun setData(list: List<FoodMenu>) {
        employeeList = list as SelectedFoodMenuList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): EmployeeViewHolder {
        return EmployeeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food_menu, parent, false))
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val item = employeeList.get(position)
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    class EmployeeViewHolder(val containerView: View?): RecyclerView.ViewHolder(containerView!!) {

        fun bind(item: FoodMenu, clickListener: (Int, FoodMenu) -> Unit) {
            itemView.run {

/*
                imageviewEdit.setOnClickListener { clickListener(R.id.imageviewEdit, item) }
                imageviewDelete.setOnClickListener { clickListener(R.id.imageviewDelete, item) }*/
            }

        }
    }
}