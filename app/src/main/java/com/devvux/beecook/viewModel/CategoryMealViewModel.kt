package com.devvux.beecook.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devvux.beecook.obj.CategoryPopularList
import com.devvux.beecook.obj.CategoryPopularMeal
import com.devvux.beecook.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel :ViewModel() {
    val mealsLiveData = MutableLiveData<List<CategoryPopularMeal>>()
    fun getMealByCategory(category: String){
        RetrofitInstance.api.getMealByCategory(category).enqueue(object : Callback<CategoryPopularList>{
            override fun onResponse(
                call: Call<CategoryPopularList>,
                response: Response<CategoryPopularList>
            ) {
                response.body()?.let {
                    mealsLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<CategoryPopularList>, t: Throwable) {
                Log.d("Err Category By Meals Item ","=${t.message}")
            }

        })
    }
    fun observeMealByCategory():LiveData<List<CategoryPopularMeal>>{
        return mealsLiveData
    }
}