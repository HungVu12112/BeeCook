
package com.devvux.beecook.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvux.beecook.db.MealDatabase
import com.devvux.beecook.obj.Meal
import com.devvux.beecook.obj.MealList
import com.devvux.beecook.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
   private val mealDatabase: MealDatabase
):ViewModel() {
    private var mealDeTailLiveData = MutableLiveData<Meal>()

    fun getDetailMeal(id:String){
        RetrofitInstance.api.getDetailMeal(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    mealDeTailLiveData.value = response.body()!!.meals[0]
                }
                else
                    return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Err Detail Meal"," = ${t.message.toString()}")
            }

        })
    }
    fun observeMealDetail():LiveData<Meal>{
        return mealDeTailLiveData
    }
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}