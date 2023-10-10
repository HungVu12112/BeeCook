package com.devvux.beecook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.devvux.beecook.CategoryMeal
import com.devvux.beecook.MainActivity
import com.devvux.beecook.adapter.CategoryAdapter
import com.devvux.beecook.databinding.FragmentCategoryBinding
import com.devvux.beecook.viewModel.HomeViewModel

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter:CategoryAdapter
    private lateinit var viewModel : HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycleView()
        observeCategory()
        onCategoryClickItem()
    }

    private fun observeCategory() {
        viewModel.observeCategory().observe(viewLifecycleOwner, Observer {cate->
            categoryAdapter.setCategoryList(cate)
        })
    }

    private fun prepareRecycleView() {
        categoryAdapter = CategoryAdapter()
        binding.recyclerViewCategory.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoryAdapter
        }
    }
    private fun onCategoryClickItem(){
        categoryAdapter.onClickItem = {
            val intent = Intent(context,CategoryMeal::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,it.strCategory)
            startActivity(intent)
        }
    }
}