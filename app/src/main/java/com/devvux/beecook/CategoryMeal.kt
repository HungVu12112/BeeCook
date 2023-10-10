package com.devvux.beecook

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.devvux.beecook.adapter.CategoryMealAdapter
import com.devvux.beecook.databinding.ActivityCategoryMealBinding
import com.devvux.beecook.fragment.HomeFragment
import com.devvux.beecook.viewModel.CategoryMealViewModel

@SuppressLint("StaticFieldLeak")
private lateinit var binding:ActivityCategoryMealBinding
private lateinit var categoryMealViewModel: CategoryMealViewModel
private lateinit var categoryMealAdapter:CategoryMealAdapter
class CategoryMeal : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecycelView()
        categoryMealViewModel = ViewModelProviders.of(this@CategoryMeal)[CategoryMealViewModel::class.java]
        categoryMealViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealViewModel.observeMealByCategory().observe(this, Observer{
            binding.textCountMeal.text = "Number Of Meals : ${it.size.toString()}"
            categoryMealAdapter.setCategoryByMeal(it)
        })
        onPopularClickItem()


    }

    private fun prepareRecycelView() {
        categoryMealAdapter = CategoryMealAdapter()
        binding.recycelViewMeal.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealAdapter
        }
    }
    private fun onPopularClickItem() {
        categoryMealAdapter.onClickItem = {
            val intent = Intent(this,MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            startActivity(intent)
        }
    }
}