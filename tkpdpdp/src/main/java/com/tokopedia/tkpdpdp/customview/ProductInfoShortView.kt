package com.tokopedia.tkpdpdp.customview

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.Pair
import android.view.LayoutInflater
import android.view.View

import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.tkpdpdp.ProductInfoShortDetailActivity
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
            tv_preorder.setText(String.format(
                    resources.getString(R.string.value_preorder_process_time),
                    data.preOrder.preorderProcessTime,
                    data.preOrder.preorderProcessTimeTypeString))
            showPreorder(true)
        } else {
            showPreorder(false)
        }

        this.productDetailData = data

        tv_minimum.text = data.info.productMinOrder
        tv_condition.text = data.info.productCondition

        view_detail_product_info_short.setOnClickListener {
            val intent = Intent()
            with(ProductInfoShortDetailActivity.IntentOptions){
                intent.productDetailData = data
            }

            listener?.onProductInfoShortClicked(intent)
        }

        label_seller_textview.setOnClickListener(null)

        view_detail_product_info_short.visibility = View.VISIBLE
    }

    fun showPreorder(show : Boolean){
        when(show) {
            true -> {
                view_preorder.visibility = View.VISIBLE
            }

            false -> {
                view_preorder.visibility = View.GONE
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