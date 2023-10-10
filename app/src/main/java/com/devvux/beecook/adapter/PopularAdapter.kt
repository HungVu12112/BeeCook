package com.devvux.beecook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.devvux.beecook.databinding.ItemPopularBinding
import com.devvux.beecook.obj.CategoryPopularMeal

class PopularAdapter():RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {
    private var mealsList = ArrayList<CategoryPopularMeal>()
    lateinit var onClickItem:((CategoryPopularMeal) -> Unit)
    var onLongClickItem : ((CategoryPopularMeal) -> Unit)?=null
    fun setMeals(mealsList: ArrayList<CategoryPopularMeal>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(ItemPopularBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgPopular)

        holder.itemView.setOnClickListener{
            onClickItem.invoke(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongClickItem?.invoke(mealsList[position])
            true
        }
    }
    inner class PopularViewHolder(val binding: ItemPopularBinding):ViewHolder(binding.root)

}