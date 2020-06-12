package com.example.doodbluetask.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class RestaurantAddress(
    @SerializedName("available_time")
    val availableTime: String,
    @SerializedName("contact_number")
    val contactNumber: String,
    val name: String,
    val rating: Float,
    val reviews: Int
)

@Parcelize
data class FoodMenu(
    @SerializedName("menu_id")
    val menuId: Long,
    @SerializedName("menu_name")
    val menuName: String,
    val price: Float,
    val recipe: String,
    val typeD: String,
    val typeN: String,
    var itemCount: Int = 0
) : Parcelable


// TODO: New Learn; passing list of objects using Safe Args in Nav comp
/*
* Currently i don't think there is a simple way to use list of parcelables with safe args, But i have found some "hack" to make this work.
* For example, i have object 'User' and it parcelable, i am declaring a new parcelable object 'Users' that extending ArrayList().
* */
@Parcelize
class SelectedFoodMenuList: ArrayList<FoodMenu>(), Parcelable