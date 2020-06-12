package com.example.doodbluetask.mycart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhuvanesh.appbase.getViewModel
import com.example.doodbluetask.AppConstants
import com.example.doodbluetask.FoodDeliveryApplication
import com.example.doodbluetask.R
import com.example.doodbluetask.foodmenu.FoodMenuListAdapter
import com.example.doodbluetask.foodmenu.FoodMenuViewModel
import com.example.doodbluetask.model.FoodMenu
import kotlinx.android.synthetic.main.fragment_food_menu.toolbar
import kotlinx.android.synthetic.main.fragment_my_cart.*

class MyCartFragment : Fragment() {

    private lateinit var mViewModel: FoodMenuViewModel
    private lateinit var foodMenuAdapter: FoodMenuListAdapter
    private var foodMenuList: List<FoodMenu> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedFoodMenuList =
            MyCartFragmentArgs.fromBundle(requireArguments()).selectedFoodMenuList
        foodMenuList = selectedFoodMenuList

        activity?.let {
            mViewModel =
                (it as AppCompatActivity).getViewModel { FoodMenuViewModel(FoodDeliveryApplication.getAppContext()) }
//            setObservers(it)
        }

        foodMenuAdapter = FoodMenuListAdapter(requireContext(), true) { totalItemCount ->
            //                handleRecyclerViewItemClick(totalItemCount)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_cart, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun initViews() {
        with(recyclerviewCartList) {
            layoutManager = LinearLayoutManager(context)
            adapter = foodMenuAdapter
        }

        if (foodMenuList.size <= AppConstants.INITIAL_DATA_TO_LOAD) {
            foodMenuAdapter.setData(foodMenuList)
        } else {
            mViewModel.setCartList(foodMenuList)
            val initialList = mViewModel.getInitialData()

            foodMenuAdapter.setWithFooter(true)
            foodMenuAdapter.setData(initialList)

            foodMenuAdapter.onClickShowMore = {
                foodMenuAdapter.setWithFooter(false)
                foodMenuAdapter.notifyItemRemoved(initialList.size)

                mViewModel.getRemainingData().forEach {
                    (initialList as ArrayList<FoodMenu>).add(it)
                    foodMenuAdapter.notifyItemInserted(initialList.size - 1)
                }
            }
        }
    }
}