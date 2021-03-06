package com.example.doodbluetask.foodmenu

import android.app.Application
import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doodbluetask.AppConstants
import com.example.doodbluetask.MCommerceRepository
import com.example.doodbluetask.model.FoodMenu
import com.example.doodbluetask.model.RestaurantAddress
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FoodMenuViewModel(val application: Application): ViewModel() {

    private var foodMenuListLiveData = MutableLiveData<List<FoodMenu>>()
    private var addressLiveData = MutableLiveData<RestaurantAddress>()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val repository: MCommerceRepository

    private lateinit var  originalList : List<FoodMenu>
    private var updatedCartMap = SparseIntArray()
    private var totalCartItemCount = 0

    init {
        repository = MCommerceRepository(application)
    }

    fun getFoodMenuListLiveData(): LiveData<List<FoodMenu>> = foodMenuListLiveData

    fun getAddressLiveData(): LiveData<RestaurantAddress> = addressLiveData


    fun getFoodMenu() {
        val disposable = Single.fromCallable { repository.getFoodMenu() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                foodMenuListLiveData.value = it
            }, {})
        compositeDisposable.add(disposable)
    }

    fun getAddress() {
        val disposable = Single.fromCallable { repository.getRestaurantAddress() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                addressLiveData.value = it
            }, {})
        compositeDisposable.add(disposable)
    }

    fun setCartList(list: List<FoodMenu>) {
        originalList = list as ArrayList<FoodMenu>
    }

    fun setUpdatedCartMap(map: SparseIntArray) {
        this.updatedCartMap = map
    }

    fun setTotalCartItemCount(count: Int) {
        this.totalCartItemCount = count
    }

    fun getInitialData() =
        originalList.filterIndexed { index, foodMenu ->  index < AppConstants.INITIAL_DATA_TO_LOAD}

    fun getRemainingData() =
        originalList.filterIndexed { index, foodMenu ->  index > AppConstants.INITIAL_DATA_TO_LOAD - 1}

    fun getUpdatedCartMap() = updatedCartMap

    fun getTotalCartItemCount() = totalCartItemCount
}