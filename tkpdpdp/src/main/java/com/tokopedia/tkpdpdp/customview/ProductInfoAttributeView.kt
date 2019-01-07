package com.tokopedia.tkpdpdp.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.tkpdpdp.R
import kotlinx.android.synthetic.main.view_product_info_attribute.view.*

class ProductInfoAttributeView : BaseCustomView {

    val layoutView: Int
        get() = R.layout.view_product_info_attribute

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    protected fun initView(context: Context) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(layoutView, this, true)
    }

    fun renderData(productDetailData: ProductDetailData) {
        view_attribute.visibility = View.VISIBLE;
        tv_seen.setText(productDetailData.statistic.productViewCount)
        tv_success_rate.text =
                productDetailData.statistic.productSuccessRate + "% ("+
                productDetailData.statistic.productSoldCount+" produk)"
    }

    fun renderWishlistCount(wishlistCount : String){
        tv_wishlist.text = wishlistCount;
        tv_wishlist.visibility = View.VISIBLE;

        progress_bar_wishlist_count.visibility = View.GONE;
    }
}
