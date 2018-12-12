package com.tokopedia.tkpdpdp.customview

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.core.network.entity.variant.ProductVariant
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel
import com.tokopedia.tkpdpdp.listener.ProductDetailView


import com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID
import kotlinx.android.synthetic.main.view_varian_courier_simulation.view.*

class VarianCourierSimulationView : BaseCustomView {

    private var productDetailData: ProductDetailData? = null
    private var listener: ProductDetailView? = null;

    val layoutView: Int
        get() = R.layout.view_varian_courier_simulation

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    protected fun initView(context: Context) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(layoutView, this, true)
    }

    fun setProductDetailData(data : ProductDetailData){
        this.productDetailData = data;
    }

    fun addProductVariant(productVariant: ProductVariant, productDetailData: ProductDetailData) {
        view_variant_field!!.setOnClickListener { listener!!.openVariantPage(0) }
        view_variant_field!!.visibility = View.VISIBLE
    }

    fun updateVariant(variantSelected: String) {
        variant_title.setText(variantSelected)
        variant_title.setTextColor(ContextCompat.getColor(context!!, R.color.medium_green))
    }

    fun setListener(listener : ProductDetailView) {
        this.listener = listener;
    }

    fun renderRateEstimation(ratesModel: RatesModel?) {
        if (ratesModel == null) {
            showCourierRate(false)
        } else {
            showCourierRate(true)
            showShopLocationView(true)
            tv_courier_count.text = ratesModel.attributes.size.toString()
            tv_courier_est_cost.text = MethodChecker.fromHtml(ratesModel.texts.textMinPrice)
            tv_destination.text = ratesModel.texts.textDestination
            tv_shop_location.text = productDetailData!!.shopInfo.shopLocation;

            view_courier_field.setOnClickListener({ view -> listener!!.moveToEstimationDetail() })
        }
    }

    fun renderRateEstimation() {
        showCourierRate(true)
        tv_shop_location.text = productDetailData!!.shopInfo.shopLocation;
        view_courier_field.setOnClickListener({ view -> listener!!.moveToEstimationDetail() })
    }

    private fun showCourierRate(show : Boolean) {
        when(show){
            true -> {view_courier_field.visibility = View.VISIBLE}
            false -> {view_courier_field.visibility = View.GONE}
        }
    }

    private fun showShopLocationView(show : Boolean){
        when(show){
            true -> {
                img_shop_location.visibility = View.VISIBLE
                tv_shop_location_label.visibility = View.VISIBLE
                tv_shop_location.visibility = View.VISIBLE
            }
            false -> {
                img_shop_location.visibility = View.GONE
                tv_shop_location_label.visibility = View.GONE
                tv_shop_location.visibility = View.GONE
            }
        }
    }

    companion object {
        private val PERCENTAGE = "%"
        private val CURRENCY = "Rp"
    }
}