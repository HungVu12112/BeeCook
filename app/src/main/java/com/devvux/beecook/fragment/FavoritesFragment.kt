package com.devvux.beecook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.devvux.beecook.MainActivity
import com.devvux.beecook.MealActivity
import com.devvux.beecook.adapter.FavoriteAdapter
import com.devvux.beecook.databinding.FragmentFavoritesBinding
import com.devvux.beecook.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {
    private lateinit var binding:FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapterFA: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecycleView()
        observeFavorite()
        onFavoriteClickItem()


        ///// xử lý khi người dùng vuốt recycleView sang bên trái

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = adapterFA.differ.currentList[position]
                viewModel.deleteMeal(meal)  ///thực hiện xóa

                Snackbar.make(requireView(),"Meal deleted",Snackbar.LENGTH_LONG).setAction("Undo",View.OnClickListener {    ///// thực hiện undo xóa quay lại sẽ gọi lại thằng insert để thêm lại cái thằng meal vừa xóa
                    viewModel.insertMeal(meal)
                }).show()
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recycleViewFavorite)
    }

    private fun prepareRecycleView() {
        adapterFA = FavoriteAdapter()
        binding.recycleViewFavorite.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = adapterFA
        }
    }

    private fun observeFavorite() {
        viewModel.observeFavorite().observe(viewLifecycleOwner, Observer {
            adapterFA.differ.submitList(it)
        })
    }
    private fun onFavoriteClickItem() {
        adapterFA.onClickItem = {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            startActivity(intent)
        }
    }


}