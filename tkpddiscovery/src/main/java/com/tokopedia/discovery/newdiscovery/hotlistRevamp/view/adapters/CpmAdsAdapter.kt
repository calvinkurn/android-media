package com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.interfaces.CpmTopAdsListener

class CpmAdsAdapter(private var cpmItemList: ArrayList<CpmItem>,
                    private var cpmTopAdsListener: CpmTopAdsListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_SHOP = 0
        const val VIEW_PRODUCT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            VIEW_SHOP -> {
                val v = LayoutInflater.from(parent.context).inflate(ShopViewHolder.LAYOUT, parent, false)
                ShopViewHolder(v)
            }

            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ProductViewHolder.LAYOUT, parent, false)
                ProductViewHolder(v)
            }
        }

    }

    override fun getItemCount(): Int {
        return cpmItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {

            VIEW_SHOP -> {
                setShopData(holder as ShopViewHolder, position)
            }
            else -> {
                setProductData(holder as ProductViewHolder, position)
            }

        }
    }

    private fun setShopData(holder: ShopViewHolder, position: Int) {
        holder.shopName.text = cpmItemList[position].name
        holder.description.text = cpmItemList[position].description
        com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageCircle2(holder.itemView.context,
                holder.shopImage,
                cpmItemList[position].image)
    }

    private fun setProductData(holder: ProductViewHolder, position: Int) {
        holder.product_name.text = cpmItemList[position].name
        holder.productDesc.text = cpmItemList[position].description
        holder.productPrice.text = cpmItemList[position].price_format
        com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageCircle2(holder.itemView.context,
                holder.productImage,
                cpmItemList[position].image)
    }


    override fun getItemViewType(position: Int): Int {
        return if (cpmItemList[position].is_product) {
            VIEW_PRODUCT
        } else
            VIEW_SHOP
    }


    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_hotlist_cpm_shop
        }

        val shopBadge = itemView.findViewById<ImageView>(R.id.badge)
        val shopName = itemView.findViewById<TextView>(R.id.shop_name)
        val description = itemView.findViewById<TextView>(R.id.description)
        val shopImage = itemView.findViewById<ImageView>(R.id.shop_image)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_hotlist_cpm_product
        }

        val productImage = itemView.findViewById<ImageView>(R.id.product_image)
        val product_name = itemView.findViewById<TextView>(R.id.product_name)
        val productDesc = itemView.findViewById<TextView>(R.id.product_desc)
        val productPrice = itemView.findViewById<TextView>(R.id.product_price)
    }
}