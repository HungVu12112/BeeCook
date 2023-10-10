package com.devvux.beecook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devvux.beecook.databinding.MealItemBinding
import com.devvux.beecook.obj.CategoryPopularMeal

class CategoryMealAdapter:RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>() {
    private var categoryMealList = ArrayList<CategoryPopularMeal>()
    lateinit var onClickItem:((CategoryPopularMeal) -> Unit)
    fun setCategoryByMeal(category:List<CategoryPopularMeal>){
        this.categoryMealList = category as ArrayList<CategoryPopularMeal>
        notifyDataSetChanged()
    }
    inner class CategoryMealViewHolder(val binding: MealItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryMealList.size
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoryMealList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvNameMeal.text = categoryMealList[position].strMeal
        holder.itemView.setOnClickListener {
            onClickItem!!.invoke(categoryMealList[position])
        }
    }
}