package com.example.doodbluetask.mycart

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhuvanesh.appbase.getViewModel
import com.bhuvanesh.appbase.ui.BaseFragment
import com.example.doodbluetask.AppConstants
import com.example.doodbluetask.FoodDeliveryApplication
import com.example.doodbluetask.R
import com.example.doodbluetask.foodmenu.FoodMenuListAdapter
import com.example.doodbluetask.foodmenu.FoodMenuViewModel
import com.example.doodbluetask.model.FoodMenu
import kotlinx.android.synthetic.main.fragment_food_menu.toolbar
import kotlinx.android.synthetic.main.fragment_my_cart.*

class MyCartFragment : BaseFragment() {

    private lateinit var mViewModel: FoodMenuViewModel
    private lateinit var foodMenuAdapter: FoodMenuListAdapter
    private var foodMenuList: List<FoodMenu> = mutableListOf()

    private var totalCost = 0f
    private var isCartItemUpdated = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedFoodMenuList =
            MyCartFragmentArgs.fromBundle(requireArguments()).selectedFoodMenuList
        foodMenuList = selectedFoodMenuList

        activity?.let {
            mViewModel =
                (it as AppCompatActivity).getViewModel { FoodMenuViewModel(FoodDeliveryApplication.getAppContext()) }
        }

        foodMenuAdapter = FoodMenuListAdapter(requireContext(), true) { totalItemCount -> }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
//        (requireActivity() as AppCompatActivity).setupActionBarWithNavController(navController)
    }

    private fun initViews() {
        mViewModel.getUpdatedCartMap().clear()
        mViewModel.setTotalCartItemCount(0)

        updateTotalCost()

        with(recyclerviewCartList) {
            layoutManager = LinearLayoutManager(context)
            adapter = foodMenuAdapter
        }

        if (foodMenuList.size <= AppConstants.INITIAL_DATA_TO_LOAD) {
            foodMenuAdapter.setData(foodMenuList)
        } else {
            updateCartList()
        }
        setListeners()
    }

    private fun setListeners() {
        foodMenuAdapter.onItemCountChanged = { itemPrice ->
            isCartItemUpdated = true
            totalCost += itemPrice
            createSpannableString()

            if (totalCost == 0f) {
                foodMenuAdapter.setWithHeader(true)
                foodMenuAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateCartList() {
        mViewModel.setCartList(foodMenuList)
        val initialList = mViewModel.getInitialData()

        foodMenuAdapter.setWithFooter(true)
        foodMenuAdapter.setData(initialList)

        foodMenuAdapter.onClickShowMore = {
            foodMenuAdapter.setWithFooter(false)
            foodMenuAdapter.notifyItemRemoved(initialList.size)

            mViewModel.getRemainingData().forEach {
                (initialList as ArrayList<FoodMenu>).add(it)
                foodMenuAdapter.notifyItemInserted(initialList.size-1)
            }
        }
    }

    private fun updateTotalCost() {
        foodMenuList.forEach { totalCost += it.itemCount * it.price }
        createSpannableString()
    }

    private fun createSpannableString() {
        val amount = resources.getString(R.string.text_price, totalCost)
        val text = "Total Cost \n $amount"

        val spannable = SpannableString(text)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorLightOrange)),
            0, 10,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorDarkBlue)),
            13, text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        buttonTotalCost.text = spannable
    }

    fun onBackPressed() {
        if (!foodMenuList.isEmpty()) {
            var totalCartItemCount = 0
            foodMenuList.forEach { totalCartItemCount += it.itemCount }
            mViewModel.setTotalCartItemCount(totalCartItemCount)
        }

        if (!isCartItemUpdated)
            return

        mViewModel.setUpdatedCartMap(foodMenuAdapter.getUpdatedCartMap())
    }
}