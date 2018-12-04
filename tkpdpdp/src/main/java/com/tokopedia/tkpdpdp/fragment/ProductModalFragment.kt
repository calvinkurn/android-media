package com.tokopedia.tkpdpdp.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.core.network.entity.variant.ProductVariant
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.constant.ConstantKey.*
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
        private const val ARGS_STATE_FORM_PRODUCT_MODAL = "ARGS_STATE_FORM_PRODUCT_MODAL"

        fun newInstance(variant: ProductVariant?,
                        detailData: ProductDetailData?,
                        quantity: Int,
                        remarkNotes: String,
                        stateProductModal: Int
        ): Fragment {
            val fragment = ProductModalFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARGS_PRODUCT_VARIANT, variant)
            bundle.putParcelable(ARGS_PRODUCT_DETAIL, detailData)
            bundle.putInt(ARGS_SELECTED_QUANTITY, quantity)
            bundle.putString(ARGS_SELECTED_REMARK_NOTES, remarkNotes)
            bundle.putInt(ARGS_STATE_FORM_PRODUCT_MODAL, stateProductModal)
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
    private var stateProductModal: Int? = 0
    private var selectedRemarkNotes: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        productVariant = arguments?.getParcelable(ARGS_PRODUCT_VARIANT)
        productData = arguments?.getParcelable(ARGS_PRODUCT_DETAIL)
        selectedQuantity = arguments?.getInt(ARGS_SELECTED_QUANTITY)
        selectedRemarkNotes = arguments?.getString(ARGS_SELECTED_REMARK_NOTES)
        stateProductModal = arguments?.getInt(ARGS_STATE_FORM_PRODUCT_MODAL)
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
        renderButton()
    }

    private fun renderButton() {
        if (productVariant == null) {
            renderButtonNonVariant()
        } else {
            renderButtonVariant()
        }
    }

    private fun isPreOrder(): Boolean {
        return (productData?.preOrder != null
                && productData?.preOrder?.preorderStatus == "1"
                && productData?.preOrder?.preorderStatus != "0"
                && productData?.preOrder?.preorderProcessTime != "0"
                && productData?.preOrder?.preorderProcessTimeType != "0"
                && productData?.preOrder?.preorderProcessTimeTypeString != "0")
    }

    private fun generateTextCartPrice(): String {
        if (isCampaign()) {
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(productData?.campaign?.discountedPrice!! * selectedQuantity!!, true)
        } else {
            for (item in productData!!.wholesalePrice) {
                if (selectedQuantity!! >= item.wholesaleMinRaw && selectedQuantity!! <= item.wholesaleMaxRaw) {
                    return CurrencyFormatUtil.convertPriceValueToIdrFormat(item.wholesalePriceRaw * selectedQuantity!!, true)
                }
            }
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(productData?.info?.productPriceUnformatted!! * selectedQuantity!!, true)
        }
    }

    private fun generateTextButtonBuy(): String {
        return when (stateProductModal) {
            STATE_BUTTON_BUY -> if (isPreOrder()) {
                resources.getString(R.string.title_pre_order)
            } else {
                resources.getString(R.string.title_buy_now)
            }
            STATE_BUTTON_CART -> resources.getString(R.string.title_add_to_cart)
            else -> if (isPreOrder()) {
                resources.getString(R.string.title_pre_order)
            } else {
                resources.getString(R.string.title_buy)
            }
        }
    }
    private fun renderButtonNonVariant() {
        if (productData?.shopInfo?.shopStatus == 1) {
            activity?.let {
                new_button_save.background = ContextCompat.getDrawable(it, R.drawable.orange_button_rounded)
            }

            new_text_button_save.text = generateTextButtonBuy()
            text_product_price.text = generateTextCartPrice()

            new_button_save.isClickable = true
            new_button_save.setOnClickListener {
                if (stateProductModal == STATE_BUTTON_CART) onButtonCartClick() else onButtonBuyClick()
            }
            action_button_cart.setOnClickListener {
                onButtonCartClick()
            }

            container_new_checkout_flow.visibility = View.VISIBLE
            button_save.visibility = View.GONE
            action_button_cart.visibility = if (stateProductModal == STATE_VARIANT_DEFAULT) View.VISIBLE else View.GONE
        } else {
            container_new_checkout_flow.visibility = View.GONE
            button_save.visibility = View.VISIBLE

            if (isPreOrder()) {
                new_text_button_save.text = resources.getString(R.string.title_pre_order)
            } else {
                new_text_button_save.text = resources.getString(R.string.title_buy_now)
            }

            new_button_save.isClickable = false
            activity?.let {
                text_button_save_variant.setTextColor(ContextCompat.getColor(it, R.color.black_38))
                button_save.background = ContextCompat.getDrawable(it, R.drawable.button_save_grey)
            }
        }
    }

    private fun generateExtraSelectedIntent(): Intent {
        val intent = Intent()
        intent.putExtra(ARGS_RESULT_DETAIL_DATA_PDP_MODAL, productData)
        intent.putExtra(ARGS_RESULT_VARIANT_DATA_PDP_MODAL, productVariant)
        intent.putExtra(ARGS_RESULT_QUANTITY_PDP_MODAL, number_picker_quantitiy_product.value)
        intent.putExtra(ARGS_RESULT_REMARK_PDP_MODAL, et_remark.text.toString())
        return intent
    }

    private fun onButtonBuyClick() {
        val intent = generateExtraSelectedIntent()
        intent.putExtra(ARGS_STATE_RESULT_PDP_MODAL, SELECTED_VARIANT_RESULT_SKIP_TO_CART)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
        activity?.overridePendingTransition(0, R.anim.push_down)
    }

    private fun onButtonCartClick() {
        val intent = generateExtraSelectedIntent()
        intent.putExtra(ARGS_STATE_RESULT_PDP_MODAL, SELECTED_VARIANT_RESULT_STAY_IN_PDP)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
        activity?.overridePendingTransition(0, R.anim.push_down)
    }

    private fun renderButtonVariant() {

    }

    private fun renderProductInfoView() {
        ImageHandler.LoadImage(variant_image_title, productData?.productImages?.get(0)?.imageSrc300)

        variant_product_name.text = MethodChecker.fromHtml(productData?.info?.productName)
        variant_product_price.text = productData?.info?.productPrice

        et_remark.setText(selectedRemarkNotes)

        text_variant_stock.text = MethodChecker.fromHtml(productData?.info?.productStockWording)
        activity?.let {
            text_variant_stock.visibility = View.VISIBLE
            if (productData?.info?.limitedStock!!) {
                text_variant_stock.setTextColor(ContextCompat.getColor(it, R.color.tkpd_dark_red))
            } else {
                text_variant_stock.setTextColor(ContextCompat.getColor(it, R.color.black_70))
            }
        }

        number_picker_quantitiy_product.setInitialState(
                Integer.parseInt(productData?.info?.productMinOrder),
                DEFAULT_MAXIMUM_STOCK_PICKER,
                selectedQuantity!!
                )

        number_picker_quantitiy_product.setOnPickerActionListener { num ->
            selectedQuantity = num
            text_product_price.text = generateTextCartPrice()

            if (num < number_picker_quantitiy_product.getMinValue()) {
                activity?.let {
                    new_button_save.background = ContextCompat.getDrawable(it, R.drawable.button_save_grey)
                }
                new_button_save.isClickable = false;
            } else {
                activity?.let {
                    new_button_save.background = ContextCompat.getDrawable(it, R.drawable.orange_button_rounded)
                }
                new_button_save.isClickable = true;
            }
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onProductModalCancel()
                return true
            }
        }
        return false
    }

    private fun onProductModalCancel() {
        val intent = Intent()
        if (productVariant != null) {
            intent.putExtra(ARGS_PRODUCT_VARIANT, productVariant)
            intent.putExtra(ARGS_PRODUCT_DETAIL, productData)
        }
        activity?.setResult(Activity.RESULT_CANCELED, intent)
        activity?.finish()
        activity?.overridePendingTransition(0, R.anim.push_down)
    }

}
