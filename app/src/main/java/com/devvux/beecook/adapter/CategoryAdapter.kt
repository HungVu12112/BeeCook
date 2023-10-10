package com.devvux.beecook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devvux.beecook.databinding.CategoryItemBinding
import com.devvux.beecook.obj.Category

class CategoryAdapter():RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var categoryList = ArrayList<Category>()
    var onClickItem : ((Category) -> Unit)? = null
    fun setCategoryList(categoryList: List<Category>){
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }
    inner class CategoryViewHolder(val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoryList[position].strCategoryThumb).into(holder.binding.imgCategory)
        holder.binding.tvCategoryName.text = categoryList[position].strCategory
        holder.itemView.setOnClickListener{
            onClickItem!!.invoke(categoryList[position]) ///lấy vị trí của thằng list để lấy id của nó
        }
    }
}