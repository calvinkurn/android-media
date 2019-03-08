package com.tokopedia.tkpdpdp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.core.network.entity.variant.ProductVariant
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.tkpdpdp.fragment.ProductModalFragment
import model.TradeInParams

class ProductModalActivity : BaseSimpleActivity() {

    private var variant: ProductVariant? = null
    private var data: ProductDetailData? = null
    private var tradeInParams: TradeInParams? = null
    private var quantity: Int = 0
    private var stateProductModal: Int = 0
    private var remarkNotes: String = ""
    private var isFromTradeIn: Boolean = false

    companion object {
        private const val ARGS_PRODUCT_VARIANT = "VARIANT_DATA"
        private const val ARGS_PRODUCT_DETAIL = "PRODUCT_DETAIL_DATA"
        private const val ARGS_SELECTED_QUANTITY = "ARGS_SELECTED_QUANTITY"
        private const val ARGS_SELECTED_REMARK_NOTES = "ARGS_SELECTED_REMARK_NOTES"
        private const val ARGS_STATE_PRDUCT_MODAL = "ARGS_STATE_PRDUCT_MODAL"
        private const val ARGS_TRADEIN_PARAMS = "ARGS_TRADE_IN_PARAMS"
        private const val ARGS_IS_FROM_TRADEIN = "ARGS_FROM_TRADE_IN"

        fun createActivity(context: Context,
                           variant: ProductVariant?,
                           productData: ProductDetailData?,
                           tradeInParmas: TradeInParams?,
                           isFromTradeIn: Boolean,
                           quantity: Int,
                           stateProductModal: Int,
                           remarkNotes: String?
        ): Intent {
            val intent = Intent(context, ProductModalActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(ARGS_PRODUCT_VARIANT, variant)
            bundle.putParcelable(ARGS_PRODUCT_DETAIL, productData)
            bundle.putParcelable(ARGS_TRADEIN_PARAMS, tradeInParmas)
            bundle.putBoolean(ARGS_IS_FROM_TRADEIN, isFromTradeIn)
            bundle.putInt(ARGS_SELECTED_QUANTITY, quantity)
            bundle.putInt(ARGS_STATE_PRDUCT_MODAL, stateProductModal)
            bundle.putString(ARGS_SELECTED_REMARK_NOTES, remarkNotes)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        variant = bundle.getParcelable(ARGS_PRODUCT_VARIANT)
        data = bundle.getParcelable(ARGS_PRODUCT_DETAIL)
        tradeInParams = bundle.getParcelable(ARGS_TRADEIN_PARAMS)
        quantity = bundle.getInt(ARGS_SELECTED_QUANTITY, 0)
        remarkNotes = bundle.getString(ARGS_SELECTED_REMARK_NOTES, "")
        stateProductModal = bundle.getInt(ARGS_STATE_PRDUCT_MODAL, 0)
        isFromTradeIn = bundle.getBoolean(ARGS_IS_FROM_TRADEIN, false)

        return ProductModalFragment.newInstance(variant, data, tradeInParams, quantity, remarkNotes, stateProductModal, isFromTradeIn)
    }

}
