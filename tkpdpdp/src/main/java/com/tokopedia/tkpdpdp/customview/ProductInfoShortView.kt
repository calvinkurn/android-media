package com.tokopedia.tkpdpdp.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.listener.ProductDetailView
import kotlinx.android.synthetic.main.view_detail_product_info_short.view.*

class ProductInfoShortView : BaseCustomView {

    private var productDetailData: ProductDetailData? = null
    private var listener: ProductDetailView? = null;

    val layoutView: Int
        get() = R.layout.view_detail_product_info_short

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

    fun renderProductData(data : ProductDetailData) {
        if (data.preOrder != null && data.preOrder.preorderStatus == "1"
                && data.preOrder.preorderStatus != "0"
                && data.preOrder.preorderProcessTime != "0"
                && data.preOrder.preorderProcessTimeType != "0"
                && data.preOrder.preorderProcessTimeTypeString != "0") {
            tv_preorder.setText(String.format("%s %s %s", "Waktu Proses",
                    data.preOrder.preorderProcessTime,
                    data.preOrder.preorderProcessTimeTypeString))
            showPreorder(true)
        }

        tv_minimum.text = data.info.productMinOrder
        tv_condition.text = data.info.productCondition
    }

    fun showPreorder(show : Boolean){
        when(show) {
            true -> {
                tv_label_preorder.visibility = View.VISIBLE
                tv_preorder.visibility = View.VISIBLE
            }

            false -> {
                tv_label_preorder.visibility = View.GONE
                tv_preorder.visibility = View.GONE
            }
        }
    }


    fun setListener(listener : ProductDetailView) {
        this.listener = listener;
    }

    companion object {
        private val PERCENTAGE = "%"
        private val CURRENCY = "Rp"
    }
}