package com.devvux.beecook.retrofit

import com.devvux.beecook.obj.CategoryList
import com.devvux.beecook.obj.CategoryPopularList
import com.devvux.beecook.obj.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php")
    fun getDetailMeal(@Query("i") id:String) :Call<MealList>

    @GET("filter.php") ////link Api : https://www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    fun getPopularItem(@Query("c") categoryName: String):Call<CategoryPopularList>

    @GET("categories.php")
    fun getCategory():Call<CategoryList>

    @GET("filter.php") ////link Api : https://www.themealdb.com/api/json/v1/1/filter.php?
    fun getMealByCategory(@Query("c") categoryName: String):Call<CategoryPopularList>
    @GET("search.php")    ///// link Api : www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    fun searchMeal(@Query("s") searchMeal: String):Call<MealList>

    @GET("lookup.php")  ////link Api : https://www.themealdb.com/api/json/v1/1/lookup.php?i=53043
    fun getMealById(@Query("") idMeal: String):Call<MealList>
}