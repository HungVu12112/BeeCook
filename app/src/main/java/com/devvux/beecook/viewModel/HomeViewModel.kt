package com.devvux.beecook.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvux.beecook.db.MealDatabase
import com.devvux.beecook.obj.Category
import com.devvux.beecook.obj.CategoryList
import com.devvux.beecook.obj.CategoryPopularList
import com.devvux.beecook.obj.CategoryPopularMeal
import com.devvux.beecook.obj.Meal
import com.devvux.beecook.obj.MealList
import com.devvux.beecook.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemLiveData = MutableLiveData<List<CategoryPopularMeal>>()
    private var categoryLivewData = MutableLiveData<List<Category>>()
    private var favoriteLiveData = mealDatabase.mealDao().getAllMeal() //// lấy tất cả các meal về đối tượng này
    private var bottomSheetLiveData = MutableLiveData<Meal>()
    private var searchLiveData = MutableLiveData<List<Meal>>()



    fun searchMeal(searchMeal: String){
        RetrofitInstance.api.searchMeal(searchMeal).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList.let {
                    searchLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomViewModel","=${t.message}")
            }

        })
    }
    fun observeSearchMeal():LiveData<List<Meal>>{
        return searchLiveData
    }


    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d("Random meal","name${randomMeal.strMeal}")
//                    Glide.with(this@HomeFragment).load(randomMeal.strMealThumb).into(binding.imgRandomMeal)
                    randomMealLiveData.value = randomMeal
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("ERR Random meal",t.message.toString())
            }

        })
    }
    fun observeRandomLivedata():LiveData<Meal>{  ////đây là một hàm quan sát , xem đối tượng thằng Meal thay đổi thì hàm này sẽ cập nhật lại thằng recycelView ở activity
        return randomMealLiveData
    }
    fun getPopularItem(){
        RetrofitInstance.api.getPopularItem("Seafood").enqueue(object : Callback<CategoryPopularList>{
            override fun onResponse(call: Call<CategoryPopularList>, response: Response<CategoryPopularList>) {
                if (response.body() != null){
                    popularItemLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<CategoryPopularList>, t: Throwable) {
                Log.d("Err Popular Item ","=${t.message}")
            }

        })
    }
    fun observePopularItem():LiveData<List<CategoryPopularMeal>>{
        return popularItemLiveData
    }
    fun getCategory(){
        RetrofitInstance.api.getCategory().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
//                response.body()?.let { categoryList ->
//                    categoryLivewData.postValue(categoryList.categories)
//                }
                if (response.body()!=null){
                    categoryLivewData.value = response.body()!!.categories
                }

            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("ERR Category","=${t.message}")
            }

        })
    }
    fun observeCategory():LiveData<List<Category>>{
        return categoryLivewData
    }
    fun observeFavorite():LiveData<List<Meal>>{
        return favoriteLiveData
    }
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun getMealById(id : String){
        RetrofitInstance.api.getDetailMeal(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal->
                    bottomSheetLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomViewModel","=${t.message}")
            }

        })
    }
    fun observeBottomById():LiveData<Meal>{
        return bottomSheetLiveData
    }
//    fun observeBottomById():LiveData<Meal> = bottomSheetLiveData (Cách viết khác nhanh gọn hơn hàm trên)

}