package com.devvux.beecook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.devvux.beecook.MainActivity
import com.devvux.beecook.MealActivity
import com.devvux.beecook.adapter.FavoriteAdapter
import com.devvux.beecook.databinding.FragmentSearchBinding
import com.devvux.beecook.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
   private lateinit var binding: FragmentSearchBinding
   private lateinit var viewModel: HomeViewModel
   private lateinit var searchMealAdapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel  = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerViewSearch()
        binding.imgBackArr.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgSearchArr.setOnClickListener{
            searchMeal()
        }
        observeSearchMeal()
        var searchJob: Job? = null
        binding.edSearchBox.addTextChangedListener {searchQuery->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal(searchQuery.toString())
            }
        }
        onPopularClickItem()
    }

    private fun observeSearchMeal() {
        viewModel.observeSearchMeal().observe(viewLifecycleOwner, Observer {mealList->
            searchMealAdapter.differ.submitList(mealList)
        })
    }

    private fun searchMeal() {
        val searchQuery = binding.edSearchBox.text.toString()
        if (searchQuery.isNotEmpty()){
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun prepareRecyclerViewSearch() {
        searchMealAdapter = FavoriteAdapter()
        binding.recyclerViewSearchMeal.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = searchMealAdapter
        }
    }
    private fun onPopularClickItem() {
        searchMealAdapter.onClickItem = {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            startActivity(intent)
        }
    }

}