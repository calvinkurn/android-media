package com.tokopedia.tkpdpdp.customview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.listener.ProductDetailView
import kotlinx.android.synthetic.main.view_wholesale_installment.view.*
import com.tokopedia.tkpdpdp.WholesaleActivity
import android.os.Bundle
import com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID
import com.tokopedia.tkpdpdp.InstallmentActivity

class WholesaleInstallmentView : BaseCustomView {

    private var data: ProductDetailData? = null
    private var listener: ProductDetailView? = null

    val layoutView: Int
        get() = R.layout.view_wholesale_installment

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
        this.data = data

        if (data.info.wholseSaleMinPrice != null &&
                !TextUtils.isEmpty(data.info.wholseSaleMinPrice)){
            tv_wholesale_price.text =
                    String.format(
                            resources.getString(R.string.value_wholesale_price),
                            data.info.wholseSaleMinPrice
                    )
            showWholesale(true)
            container_wholesale_installment.visibility = View.VISIBLE
        } else {
            showWholesale(false)
        }

        if (data.info.installmentMinPrice != null &&
                !TextUtils.isEmpty(data.info.installmentMinPrice)){
            tv_installment.text =
                    String.format(
                            resources.getString(R.string.value_installment),
                            data.info.installmentMinPercentage,
                            data.info.installmentMinPrice
                    )
            showInstallment(true)
            container_wholesale_installment.visibility = View.VISIBLE
        } else {
            showInstallment(false)
        }
    }

    fun showWholesale(show : Boolean){
        when(show) {
            true -> {
                view_wholesale.visibility = View.VISIBLE
                tv_title_wholesale.setOnClickListener(ClickWholeSale(listener, data))
                tv_wholesale_price.setOnClickListener(ClickWholeSale(listener, data))
            }

            false -> {
                view_wholesale.visibility = View.GONE
            }
        }
    }

    fun showInstallment(show : Boolean){
        when(show) {
            true -> {
                view_installment.visibility = View.VISIBLE
                tv_title_installment.setOnClickListener(ClickInstallment(listener, data))
                tv_installment.setOnClickListener(ClickInstallment(listener, data))
            }

            false -> {
                view_installment.visibility = View.GONE
            }
        }
    }

    fun setListener(listener : ProductDetailView) {
        this.listener = listener;
    }

    class ClickWholeSale(val listener : ProductDetailView?,
                         val data : ProductDetailData?) : OnClickListener {
        override fun onClick(p0: View?) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(WholesaleActivity.KEY_WHOLESALE_DATA,
                    ArrayList(data?.getWholesalePrice()))
            bundle.putString(EXTRA_PRODUCT_ID, data?.info?.productId.toString())
            listener?.onWholesaleClicked(bundle)
        }
    }

    class ClickInstallment(val listener : ProductDetailView?,
                         val data : ProductDetailData?) : OnClickListener {
        override fun onClick(p0: View?) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(InstallmentActivity.KEY_INSTALLMENT_DATA,
                    ArrayList(data?.info?.productInstallments))
            bundle.putString(EXTRA_PRODUCT_ID, data?.info?.productId.toString())
            listener?.onInstallmentClicked(bundle)
        }
    }
}