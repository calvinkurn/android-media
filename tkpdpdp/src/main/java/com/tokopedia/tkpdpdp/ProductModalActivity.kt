package com.tokopedia.tkpdpdp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.core.network.entity.variant.ProductVariant
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.tkpdpdp.fragment.ProductModalFragment

class ProductModalActivity : BaseToolbarActivity() {

    private var variant: ProductVariant? = null
    private var data: ProductDetailData? = null
    private var quantity: Int = 0
    private var remarkNotes: String = ""

    companion object {
        private const val ARGS_PRODUCT_VARIANT = "VARIANT_DATA"
        private const val ARGS_PRODUCT_DETAIL = "PRODUCT_DETAIL_DATA"
        private const val ARGS_SELECTED_QUANTITY = "ARGS_SELECTED_QUANTITY"
        private const val ARGS_SELECTED_REMARK_NOTES = "ARGS_SELECTED_REMARK_NOTES"

        fun createActivity(context: Context,
                           variant: ProductVariant?,
                           productData: ProductDetailData?,
                           quantity: Int,
                           remarkNotes: String?
        ): Intent {
            val intent = Intent(context, ProductModalActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(ARGS_PRODUCT_VARIANT, variant)
            bundle.putParcelable(ARGS_PRODUCT_DETAIL, productData)
            bundle.putInt(ARGS_SELECTED_QUANTITY, quantity)
            bundle.putString(ARGS_SELECTED_REMARK_NOTES, remarkNotes)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        variant = bundle.getParcelable(ARGS_PRODUCT_VARIANT)
        data = bundle.getParcelable(ARGS_PRODUCT_DETAIL)
        quantity = bundle.getInt(ARGS_SELECTED_QUANTITY, 0)
        remarkNotes = bundle.getString(ARGS_SELECTED_REMARK_NOTES, "")

        return ProductModalFragment.newInstance(variant, data, quantity, remarkNotes)
    }

}
