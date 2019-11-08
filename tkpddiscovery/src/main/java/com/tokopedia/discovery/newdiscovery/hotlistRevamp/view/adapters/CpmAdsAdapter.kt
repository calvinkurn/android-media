package com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.interfaces.CpmTopAdsListener

class CpmAdsAdapter(private var cpmItemList: ArrayList<CpmItem>,
                    private var cpmTopAdsListener: CpmTopAdsListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_SHOP = 0
        const val VIEW_PRODUCT = 1
        const val VIEW_SHIMMER = 2
        const val SHIMMER_ITEM_COUNT = 3
    }
    val viewMap = HashMap<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            VIEW_SHOP -> {
                val v = LayoutInflater.from(parent.context).inflate(ShopViewHolder.LAYOUT, parent, false)
                ShopViewHolder(v)
            }

            VIEW_PRODUCT -> {
                val v = LayoutInflater.from(parent.context).inflate(ProductViewHolder.LAYOUT, parent, false)
                ProductViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(ShimmerViewHolder.LAYOUT, parent, false)
                ShimmerViewHolder(v)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (cpmItemList.size <= 0) {
            SHIMMER_ITEM_COUNT  // done to load shimmer items
        } else {
            cpmItemList.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {

            VIEW_SHOP -> {
                setShopData(holder as ShopViewHolder, position)
            }
            VIEW_PRODUCT -> {
                setProductData(holder as ProductViewHolder, position)
            }
        }
    }

    private fun setShopData(holder: ShopViewHolder, position: Int) {
        val item = cpmItemList[position]
        holder.shopName.text = item.name
        holder.description.text = item.description
        ImageHandler.loadImageCircle2(holder.itemView.context,
                holder.shopImage,
                item.image)
        ImageHandler.loadImage(holder.itemView.context,
                holder.shopBadge,
                item.badge_url,
                R.drawable.loading_page)
        holder.shop_cpm_parent.setOnClickListener {
            cpmTopAdsListener.onCpmClicked(item.applinks ?: "", item)
        }
    }

    private fun setProductData(holder: ProductViewHolder, position: Int) {
        val item = cpmItemList[position]
        holder.product_name.text = item.name
        holder.productPrice.text = item.price_format
        ImageHandler.loadImage(holder.itemView.context,
                holder.productImage,
                item.image, R.drawable.loading_page)
        holder.product_cpm_parent.setOnClickListener {
            cpmTopAdsListener.onCpmClicked(item.applinks ?: "", item)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (cpmItemList.size <= 0) {
            VIEW_SHIMMER
        } else {
            if (cpmItemList[position].is_product) {
                VIEW_PRODUCT
            } else
                VIEW_SHOP
        }
    }


    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_hotlist_cpm_shop
        }

        val shopBadge = itemView.findViewById<ImageView>(R.id.badge)
        val shopName = itemView.findViewById<TextView>(R.id.shop_name)
        val description = itemView.findViewById<TextView>(R.id.description)
        val shopImage = itemView.findViewById<ImageView>(R.id.shop_image)
        val shop_cpm_parent = itemView.findViewById<ConstraintLayout>(R.id.shop_cpm_parent)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_hotlist_cpm_product
        }

        val productImage = itemView.findViewById<ImageView>(R.id.product_image)
        val product_name = itemView.findViewById<TextView>(R.id.product_name)
        val productPrice = itemView.findViewById<TextView>(R.id.product_price)
        val product_cpm_parent = itemView.findViewById<ConstraintLayout>(R.id.product_cpm_parent)
    }

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val LAYOUT = R.layout.item_nav_cpm_shimmer
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap.containsKey(position) && viewMap.size > 0) {
            viewMap[position] = true
            val item = cpmItemList[position]
            cpmTopAdsListener.onCpmImpression(item)
        }
    }
}