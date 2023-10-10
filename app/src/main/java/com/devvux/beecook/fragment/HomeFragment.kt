package com.devvux.beecook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devvux.beecook.CategoryMeal
import com.devvux.beecook.MainActivity
import com.devvux.beecook.MealActivity
import com.devvux.beecook.R
import com.devvux.beecook.adapter.CategoryAdapter
import com.devvux.beecook.adapter.PopularAdapter
import com.devvux.beecook.databinding.FragmentHomeBinding
import com.devvux.beecook.fragment.bottomsheet.MealBottomSheetFragment
import com.devvux.beecook.obj.CategoryPopularMeal
import com.devvux.beecook.obj.Meal
import com.devvux.beecook.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemAdapter: PopularAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    companion object {
        const val MEAL_ID = "com.devvux.beecook.fragment.MEAL_ID"
        const val MEAL_NAME = "com.devvux.beecook.fragment.MEAL_NAME"
        const val MEAL_THUMB = "com.devvux.beecook.fragment.MEAL_THUMB"
        const val CATEGORY_NAME = "com.devvux.beecook.fragment.CATEGORY_NAME"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).viewModel
        popularItemAdapter = PopularAdapter()
        categoryAdapter = CategoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getRandomMeal()
        observeRandomMeal()
        onClickDetailMeal()
        homeViewModel.getPopularItem()
        observePopularItem()
        setupPopularItem()
        onPopularClickItem()
        homeViewModel.getCategory()
        observeCategory()
        setupItemCategory()
        onClickCategory()
        onPopularLongItemClick()
        onSearchIconClick()
    }

    private fun onSearchIconClick() {

        binding.imgSearch.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)  /////chuyển màn theo nav
        }
    }

    private fun onPopularLongItemClick() {
        popularItemAdapter.onLongClickItem = {meal->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Information")
        }
    }

    private fun onClickCategory() {
        categoryAdapter.onClickItem = {
            val intent = Intent(activity,CategoryMeal::class.java)
            intent.putExtra(CATEGORY_NAME,it.strCategory)
            startActivity(intent)
        }
    }

    private fun setupItemCategory() {
        binding.recycelViewCategory.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoryAdapter
        }
    }

    private fun observeCategory() {
        homeViewModel.observeCategory().observe(viewLifecycleOwner){
                categoryAdapter.setCategoryList(it)
        }
    }

    private fun onPopularClickItem() {
        popularItemAdapter.onClickItem = {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,it.idMeal)
            intent.putExtra(MEAL_NAME,it.strMeal)
            intent.putExtra(MEAL_THUMB,it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun setupPopularItem() {
        binding.recycelViewHomePopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemAdapter
        }
    }

    private fun observePopularItem() {
        homeViewModel.observePopularItem().observe(viewLifecycleOwner
        ) {
            popularItemAdapter.setMeals(mealsList = it as ArrayList<CategoryPopularMeal>)
        }
    }

    private fun onClickDetailMeal() {
        binding.imgRandomMeal.setOnClickListener{
            var intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal(){ ////// hàm này sẽ nắng nghe các thay đổi mà thằng meal khi bị thay đổi bên thằng viewmodel
        homeViewModel.observeRandomLivedata().observe(viewLifecycleOwner
        ) { t ->
            Glide.with(this@HomeFragment).load(t!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal=t
        /////t!! là gán cho giá trị này không null tránh bị RuntimeException

        }
    }
}