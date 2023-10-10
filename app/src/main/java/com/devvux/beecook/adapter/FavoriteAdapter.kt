package com.devvux.beecook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devvux.beecook.databinding.MealItemBinding
import com.devvux.beecook.obj.Meal

class FavoriteAdapter:RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var favoriteList = ArrayList<Meal>()
    lateinit var onClickItem:((Meal) -> Unit)
    inner class FavoriteViewHolder(val binding: MealItemBinding):RecyclerView.ViewHolder(binding.root)
    private val diffUtil = object :DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val obj = differ.currentList[position]
        Glide.with(holder.itemView).load(obj.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvNameMeal.text = obj.strMeal
        holder.itemView.setOnClickListener {
            onClickItem.invoke(obj)
        }
    }

}