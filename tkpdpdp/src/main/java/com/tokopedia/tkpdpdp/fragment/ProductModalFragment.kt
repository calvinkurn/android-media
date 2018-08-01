package com.tokopedia.tkpdpdp.fragment

import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.core.network.entity.variant.ProductVariant
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.tkpdpdp.R
import kotlinx.android.synthetic.main.fragment_product_modal.*
import kotlinx.android.synthetic.main.layout_variant_activity_section_notes.*
import kotlinx.android.synthetic.main.layout_variant_activity_section_quantity.*
import kotlinx.android.synthetic.main.variant_title_item.*

class ProductModalFragment : BaseDaggerFragment() {

    companion object {

        private const val DEFAULT_MAXIMUM_STOCK_PICKER = 99999

        private const val ARGS_PRODUCT_VARIANT = "VARIANT_DATA"
        private const val ARGS_PRODUCT_DETAIL = "PRODUCT_DETAIL_DATA"
        private const val ARGS_SELECTED_QUANTITY = "ARGS_SELECTED_QUANTITY"
        private const val ARGS_SELECTED_REMARK_NOTES = "ARGS_SELECTED_REMARK_NOTES"

        fun newInstance(variant: ProductVariant?,
                        detailData: ProductDetailData?,
                        quantity: Int,
                        remarkNotes: String
        ): Fragment {
            val fragment = ProductModalFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARGS_PRODUCT_VARIANT, variant)
            bundle.putParcelable(ARGS_PRODUCT_DETAIL, detailData)
            bundle.putInt(ARGS_SELECTED_QUANTITY, quantity)
            bundle.putString(ARGS_SELECTED_REMARK_NOTES, remarkNotes)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    private var productVariant: ProductVariant? = null
    private var productData: ProductDetailData? = null
    private var selectedQuantity: Int? = 0
    private var selectedRemarkNotes: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productVariant = arguments?.getParcelable(ARGS_PRODUCT_VARIANT)
        productData = arguments?.getParcelable(ARGS_PRODUCT_DETAIL)
        selectedQuantity = arguments?.getInt(ARGS_SELECTED_QUANTITY)
        selectedRemarkNotes = arguments?.getString(ARGS_SELECTED_REMARK_NOTES)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_modal, container, false)
    }

    private fun isCampaign(): Boolean {
        return productData!!.campaign != null && productData!!.campaign.active
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderProductInfoView()
    }

    private fun renderProductInfoView() {
        ImageHandler.LoadImage(variant_image_title, productData?.productImages?.get(0)?.imageSrc300)

        variant_product_name.text = productData?.info?.productName
        variant_product_price.text = productData?.info?.productPrice

        et_remark.setText(selectedRemarkNotes)

        number_picker_quantitiy_product.setNumber(selectedQuantity!!)
        number_picker_quantitiy_product.setMinValue(Integer.parseInt(productData?.info?.productMinOrder))
        number_picker_quantitiy_product.setMaxValue(DEFAULT_MAXIMUM_STOCK_PICKER)
        number_picker_quantitiy_product.setOnPickerActionListener { num ->
            val price: Int = if (isCampaign()) {
                productData?.campaign?.discountedPrice!!
            } else {
                productData?.info?.productPriceUnformatted!!
            }
            text_product_price.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            price * num,
                            true
                    )
        }

        if (isCampaign()) {
            text_original_price.text = productData?.campaign?.originalPriceFmt
            text_original_price.paintFlags = text_original_price.paintFlags ; Paint.STRIKE_THRU_TEXT_FLAG
            text_discount.text = String.format(
                    getString(R.string.label_discount_percentage),
                    productData?.campaign?.discountedPercentage
            )
            variant_product_price.text = productData?.campaign?.discountedPriceFmt
            text_discount.visibility = View.VISIBLE
            text_original_price.visibility = View.VISIBLE
        } else {
            text_discount.visibility = View.GONE
            text_original_price.visibility = View.GONE
        }
    }
}
