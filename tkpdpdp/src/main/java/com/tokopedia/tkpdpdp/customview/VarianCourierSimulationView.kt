package com.tokopedia.tkpdpdp.customview

import android.content.Context
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
        variant_courier_container.visibility = View.VISIBLE

        if(view_courier_field.visibility == View.VISIBLE){
            line.visibility = View.VISIBLE
        }
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
            showShopDestination(false)
        } else {
            variant_courier_container.visibility = View.VISIBLE;
            showCourierRate(true)
            showShopLocationView(true)
            showShopDestination(true)
          
            when(productDetailData!!.shopInfo.shopShipments != null){
                true -> tv_courier_count.text = productDetailData!!.shopInfo.shopShipments.size.toString()
                false -> tv_courier_count.text = "-"
            }

            tv_courier_est_cost.text = MethodChecker.fromHtml(ratesModel.texts.textMinPrice)
            tv_destination.text = ratesModel.texts.textDestination
            tv_shop_location.text = productDetailData!!.shopInfo.shopLocation;

            view_courier_field.setOnClickListener({ view -> listener!!.moveToEstimationDetail() })
        }
    }

    fun renderRateEstimation() {
        showCourierRate(false)
        showShopLocationView(false)
        showShopDestination(false)
        tv_shop_location.text = productDetailData!!.shopInfo.shopLocation;
    }

    private fun showCourierRate(show : Boolean) {
        when(show){
            true -> {
                view_courier_field.visibility = View.VISIBLE
                if(view_variant_field.visibility == View.VISIBLE){
                    line.visibility = View.VISIBLE
                }
            }
            false -> {view_courier_field.visibility = View.GONE}
        }
    }

    private fun showShopLocationView(show : Boolean){
        when(show){
            true -> {
                view_shop_address.visibility = View.VISIBLE
            }
            false -> {
                view_shop_address.visibility = View.GONE
            }
        }
    }

    private fun showShopDestination(show : Boolean){
        when(show){
            true -> {
                view_destination.visibility = View.VISIBLE
                view_courier.visibility = View.VISIBLE
            }
            false -> {
                view_destination.visibility = View.GONE
                view_courier.visibility = View.GONE
            }
        }
    }

    companion object {
        private val PERCENTAGE = "%"
        private val CURRENCY = "Rp"
    }
}