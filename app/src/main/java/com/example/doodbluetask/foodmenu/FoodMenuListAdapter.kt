package com.example.doodbluetask.foodmenu

import android.content.Context
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhuvanesh.appbase.gone
import com.bhuvanesh.appbase.show
import com.example.doodbluetask.R
import com.example.doodbluetask.model.FoodMenu
import kotlinx.android.synthetic.main.item_food_menu.view.*
import kotlinx.android.synthetic.main.layout_add_remove_item.view.*
import java.lang.ref.WeakReference


/*
* Adapter Class used to display the food menu item.
* This same adapter class has been used in both "food menu" screen & "my cart" screen.
* In My Cart screen - adapter needs FOOTER also..
* */
class FoodMenuListAdapter(
    private val context: Context,
    private val isCart: Boolean,
    private var foodMenuList: MutableList<FoodMenu> = mutableListOf(),
    private val clickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var totalItemCount = 0

    // For Cart
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2
    private val updatedCartItemMap by lazy { SparseIntArray() }
    var onClickShowMore: () -> Unit = {}
    var onItemCountChanged: ((Float) -> Unit)? = {}

    private var mWithHeader = false
    private var mWithFooter = false

    fun setData(list: List<FoodMenu>) {
        foodMenuList = list as MutableList<FoodMenu>
        notifyDataSetChanged()
    }

    fun setTotalItemCount(count: Int) {
        totalItemCount = count
    }

    fun getUpdatedCartMap() = updatedCartItemMap
    /*
    * viewType - what value getItemViewType method returns.
    * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show_more, parent, false)
                ShowMoreViewHolder(view)
            }
            TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_menu, parent, false)
                FoodMenuViewHolder(view, this)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_empty_cart, parent, false)
                EmptyCartViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FoodMenuViewHolder)
            holder.bind(foodMenuList, clickListener)
        else {
            holder.itemView.setOnClickListener { onClickShowMore() }
        }
    }

    override fun getItemCount(): Int {
        var itemCount: Int = foodMenuList.size
        if (mWithHeader) itemCount++
        if (mWithFooter) itemCount++
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (mWithHeader && isPositionHeader(position)) return TYPE_HEADER
        return if (mWithFooter && isPositionFooter(position)) TYPE_FOOTER else TYPE_ITEM
    }

    fun isPositionHeader(position: Int): Boolean {
        return position == 0 && mWithHeader
    }

    fun isPositionFooter(position: Int): Boolean {
        return position == itemCount - 1 && mWithFooter
    }

    fun setWithHeader(value: Boolean) {
        mWithHeader = value
    }

    fun setWithFooter(value: Boolean) {
        mWithFooter = value
    }

    class ShowMoreViewHolder(containerView: View?) : RecyclerView.ViewHolder(containerView!!)
    class EmptyCartViewHolder(containerView: View?) : RecyclerView.ViewHolder(containerView!!)

    class FoodMenuViewHolder(containerView: View?, adapter: FoodMenuListAdapter) :
        RecyclerView.ViewHolder(containerView!!) {
        private val mWeakAdapterInstance: WeakReference<FoodMenuListAdapter>
        private val mAdapter: FoodMenuListAdapter?

        init {
            mWeakAdapterInstance = WeakReference<FoodMenuListAdapter>(adapter)
            mAdapter = mWeakAdapterInstance.get()
        }

        fun bind(itemList: MutableList<FoodMenu>, clickListener: (Int) -> Unit) {
            val item = itemList[adapterPosition]
            itemView.run {

                if (item.itemCount > 0) {
                    buttonAddItem.gone()
                    layoutAddRemoveItem.show()
                    textViewItemCount.text = "${item.itemCount}"
                } else {
                    buttonAddItem.show()
                    layoutAddRemoveItem.gone()
                }
                setData(item, this)

                textViewIncrement.setOnClickListener {
                    if (item.itemCount < 20)
                        updateItem(itemList, true)
                    else {
                        textViewIncrement.isEnabled = false
                        textViewIncrement.alpha = 0.5f
                    }
                    clickListener(mAdapter?.totalItemCount ?: 0)
                }

                textViewDec.setOnClickListener {
                    if (item.itemCount == 1) {
                        buttonAddItem.show()
                        layoutAddRemoveItem.gone()
                    }
                    if (item.itemCount > 0)
                        updateItem(itemList, false)

                    if (!textViewIncrement.isEnabled) {
                        textViewIncrement.isEnabled = true
                        textViewIncrement.alpha = 1f
                    }
                    clickListener(mAdapter?.totalItemCount ?: 0)
                }

                buttonAddItem.setOnClickListener {
                    buttonAddItem.gone()
                    layoutAddRemoveItem.show()
                    updateItem(itemList, true)
                    clickListener(mAdapter?.totalItemCount ?: 0)
                }
            }
        }

        private fun setData(item: FoodMenu, view: View) {
            view.run {
                textviewMenuTitle.text = item.menuName
                textviewMenuSubTitle.text = item.recipe
                textviewPrice.text =
                    mAdapter?.context?.resources?.getString(R.string.text_price, item.price) ?: ""


                textviewFoodTypeN.text = item.typeN
                textviewFoodTypeD.text = item.typeD
            }
        }

        private fun updateItem(itemList: MutableList<FoodMenu>, isIncrement: Boolean) {
            val item = itemList[adapterPosition]
            if (isIncrement) {
                ++(item.itemCount)
                mAdapter?.let { ++(it.totalItemCount) }
            } else {
                --(item.itemCount)
                mAdapter?.let { --(it.totalItemCount) }
            }
            // For Cart
            val isMyCart = mAdapter?.isCart ?: false
            if (isMyCart)
                mAdapter?.updatedCartItemMap?.put(item.menuId.toInt(), item.itemCount)

            // For Cart
            if (isMyCart && itemList.size > 0 && item.itemCount == 0) {
                itemList.removeAt(adapterPosition)
                mAdapter?.notifyItemRemoved(adapterPosition)
            } else {
                itemList[adapterPosition] = item
                mAdapter?.notifyItemChanged(adapterPosition)
            }
            mAdapter?.onItemCountChanged?.invoke(if (isIncrement) item.price else 0 - item.price)

        }
    }
}