package com.devvux.beecook

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.devvux.beecook.databinding.ActivityMealBinding
import com.devvux.beecook.db.MealDatabase
import com.devvux.beecook.fragment.HomeFragment
import com.devvux.beecook.obj.Meal
import com.devvux.beecook.viewModel.MealViewModel
import com.devvux.beecook.viewModel.MealViewModelFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMealBinding
private lateinit var mealId:String
private lateinit var mealName:String
private lateinit var mealThumb:String
private lateinit var linkYoutube: String
private lateinit var mealViewModel: MealViewModel
private lateinit var mAdView : AdView
private var mInterstitialAd: InterstitialAd? = null
class MealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////Khởi tạo ViewModel
        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
//        mealViewModel = ViewModelProviders.of(this@MealActivity)[MealViewModel::class.java]

        getMealInformation()
        setInformationInView()
        mealViewModel.getDetailMeal(mealId)
        observerMealDetail()
        loadingCase()
        onClickYoutube()
        onFavoriteClick()
        googleAds()
    }
    private fun googleAds(){
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequestStatic = AdRequest.Builder().build()
        mAdView.loadAd(adRequestStatic)



        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    private fun onFavoriteClick() {
        binding.btnAddFavorite.setOnClickListener {
            mealToSave?.let {    ////// chứa đối tượng và chuyền vào cho hàm đó
                mealViewModel.insertMeal(it)
                Toast.makeText(this, "Meal Save", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getMealInformation() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
    private fun setInformationInView() {
       Glide.with(this).load(mealThumb).into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))  ////// Title khi lướt lên thì màu mạc định cuat nó sẽ chuyển thành màu trắng khi kéo hết hành trình
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))   /////set màu vho title
    }

    private var mealToSave:Meal?=null   ////// tương tự trong java lhi tạo một biến có kiểu dữ liệu là đối tượng (  Meal meal = new Meal()  )
    private fun observerMealDetail() {
        mealViewModel.observeMealDetail().observe(this,object :Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponseCase()
                mealToSave = value
                binding.tvCategory.text = "Thể loại : ${value.strCategory}"
                binding.tvArea.text = "Thuộc khu vực : ${value.strArea}"
                binding.tvIntroduction.text = value.strInstructions
                linkYoutube = value.strYoutube.toString()
            }
        })
    }
    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddFavorite.visibility = View.INVISIBLE
        binding.tvIntroduction.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }
    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddFavorite.visibility = View.VISIBLE
        binding.tvIntroduction.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
    private fun onClickYoutube(){

        binding.imgYoutube.setOnClickListener{
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(linkYoutube))
            startActivity(intent)
        }
    }
}