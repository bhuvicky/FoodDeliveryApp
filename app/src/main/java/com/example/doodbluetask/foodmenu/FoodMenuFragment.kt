package com.example.doodbluetask.foodmenu

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhuvanesh.appbase.getViewModel
import com.bhuvanesh.appbase.gone
import com.bhuvanesh.appbase.show
import com.example.doodbluetask.*
import com.example.doodbluetask.model.FoodMenu
import com.example.doodbluetask.model.RestaurantAddress
import com.example.doodbluetask.model.SelectedFoodMenuList
import kotlinx.android.synthetic.main.fragment_food_menu.*
import kotlinx.android.synthetic.main.layout_restaurant_address.*

class FoodMenuFragment : Fragment() {

    private lateinit var mViewModel: FoodMenuViewModel
    private lateinit var foodMenuAdapter: FoodMenuListAdapter
    private var foodMenuList: List<FoodMenu> = mutableListOf()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            mViewModel = (it as AppCompatActivity).getViewModel { FoodMenuViewModel(FoodDeliveryApplication.getAppContext()) }
            setObservers(it)
        }

        foodMenuAdapter =
            FoodMenuListAdapter(requireContext(), false) { totalItemCount ->
                handleRecyclerViewItemClick(totalItemCount)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_food_menu, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel.getAddress()
        mViewModel.getFoodMenu()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = requireActivity().findNavController(R.id.nav_host_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initViews() {
        toolbar.title = ""
        with(recyclerviewMenuList) {
            layoutManager = LinearLayoutManager(context)
            adapter = foodMenuAdapter
        }
        foodMenuAdapter.setData(foodMenuList)

        layoutViewCart.setOnClickListener {
            //ClassCastException: java.util.ArrayList cannot be cast to com.example.doodbluetask.model.SelectedFoodMenuList
            // TODO: New Learn; passing list of objects using Safe Args in Nav comp
            val selectedFoodMenuList =  SelectedFoodMenuList()
            val list = foodMenuList.filter { it.itemCount > 0 } as ArrayList<FoodMenu>
            selectedFoodMenuList.addAll(list)

            val action = FoodMenuFragmentDirections.actionFoodMenuFragmentToMyCartFragment(selectedFoodMenuList)
            navController.navigate(action)
        }
    }

    private fun setObservers(it: AppCompatActivity) {
        mViewModel.getAddressLiveData().observe(it, Observer { setAddress(it) })

        mViewModel.getFoodMenuListLiveData().observe(it, Observer {
            foodMenuList = it
            foodMenuAdapter.setData(foodMenuList)
        })
    }


    private fun handleRecyclerViewItemClick(totalItemCount: Int) {
        // TODO: New Learn; plurals
        /*
        * When using the getQuantityString() method, you need to pass the count twice if your string includes string formatting with a number.
        *
        * For example, for the string %d songs found, the first count parameter selects the appropriate plural string and
        * the second count parameter is inserted into the %d placeholder.
        * If your plural strings do not include string formatting, you don't need to pass the third parameter to getQuantityString.
        * */
        if (totalItemCount > 0) {
            layoutViewCart.show()
            textViewCartCount.text = resources.getQuantityString(R.plurals.numberOfItemsInCart, totalItemCount, totalItemCount)
        }
        else
            layoutViewCart.gone()
    }

    private fun setAddress(address: RestaurantAddress) {
        textviewName.text = address.name

        val reviews = if (address.reviews > 200) "200 +" else address.reviews.toString()
        val ratings = "${address.rating} ($reviews) | "
        textviewRatings.text = ratings

        textviewTiming.text = "All Days: ${address.availableTime}"
        textviewContactNumber.text = "Reach us at: ${address.contactNumber}"
    }

    onB

}