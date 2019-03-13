package com.tokopedia.tkpdpdp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.internal.ApplinkConstInternal
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.core.`var`.TkpdCache.Key.STATE_ORIENTATION_CHANGED
import com.tokopedia.core.`var`.TkpdCache.PRODUCT_DETAIL
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.core.router.productdetail.PdpRouter
import com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID
import com.tokopedia.core.util.MethodChecker
import com.tokopedia.tkpdpdp.customview.YoutubeThumbnailViewHolder
import kotlinx.android.synthetic.main.activity_detail_info.*
import kotlinx.android.synthetic.main.simple_top_bar_layout.*
import kotlin.reflect.KProperty

class ProductInfoShortDetailActivity : AppCompatActivity(),
        View.OnClickListener,
        YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess{

    private lateinit var localCacheHandler: LocalCacheHandler
    private var isBackPressed: Boolean = false

    companion object IntentOptions {
        private const val KEY_PRODUCT_DETAIL_DATA = "PRODUCT_DETAIL_DATA"

        var Intent.productDetailData : ProductDetailData? by IntentExtraParcelableDelegate(KEY_PRODUCT_DETAIL_DATA)
    }

    class IntentExtraParcelableDelegate(val name : String) {
        operator fun getValue(intent : Intent,
                              property : KProperty<*>) : ProductDetailData? =
                intent.getParcelableExtra(name)
        operator fun setValue(intent : Intent,property: KProperty<*>, value : ProductDetailData?){
            intent.putExtra(name, value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Tokopedia3)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.green_600)
        }
        localCacheHandler = LocalCacheHandler(this,  PRODUCT_DETAIL)
        setContentView(getLayout())
        hideToolbar()
        setupTopbar()
        showData()
    }

    fun close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    }

    fun getLayout(): Int {
        return R.layout.activity_detail_info;
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    private fun setUpByConfiguration(configuration: Configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!localCacheHandler.getBoolean(STATE_ORIENTATION_CHANGED)) {
                val productId : String = intent.getParcelableExtra<Parcelable>(EXTRA_PRODUCT_ID).toString()
                UnifyTracking.eventPDPOrientationChanged(this, productId)
                localCacheHandler.putBoolean(STATE_ORIENTATION_CHANGED, java.lang.Boolean.TRUE)
                localCacheHandler.applyEditor()
            }
        }
    }

    private fun setupTopbar() {
        simple_top_bar_title.setText(getString(R.string.title_product_info_detail))
        simple_top_bar_close_button.setOnClickListener {
            finish()
        }
    }

    fun showData() {
        val data : ProductDetailData? = intent.productDetailData
        when (data?.preOrder != null && data?.preOrder.preorderStatus == "1"
                && data?.preOrder?.preorderStatus != "0"
                && data?.preOrder?.preorderProcessTime != "0"
                && data?.preOrder?.preorderProcessTimeType != "0"
                && data?.preOrder?.preorderProcessTimeTypeString != "0") {
            true -> tv_preorder.text = String.format(
                    getString(R.string.value_preorder_process_time),
                    data?.preOrder?.preorderProcessTime,
                    data?.preOrder?.preorderProcessTimeTypeString)
            false -> {
                view_preorder.visibility = View.GONE
                view_preorder.visibility = View.GONE
            }
        }
        when(data?.info?.productMinOrder != null){
            true -> tv_minimum.text = data?.info?.productMinOrder?.replace(".","")
            false -> view_min_order.visibility = View.GONE
        }
        when(data?.info?.productCondition != null){
            true -> tv_condition.text = data?.info?.productCondition
            false -> view_condition.visibility = View.GONE
        }
        when(data?.info?.productInsurance != null){
            true -> tv_insurance.text = data?.info?.productInsurance
            false -> view_insurance.visibility = View.GONE
        }
        when(data?.info?.productEtalase != null){
            true -> {tv_storefront.text = MethodChecker.fromHtml(data?.info?.productEtalase).toString()
                tv_storefront.setOnClickListener(EtalaseClick(
                        data!!.info.productEtalase,
                        data.info.productEtalaseId,
                        data.shopInfo.shopId,
                        this
                ))
            }
            false -> view_storefront.visibility = View.GONE
        }

        val productDepartments = data?.breadcrumb
        when (productDepartments!!.size > 0) {
            true -> {
                val productBreadcrumb = productDepartments[productDepartments.size - 1]
                tv_category.text = MethodChecker.fromHtml(productBreadcrumb.departmentName).toString()
                tv_category.setOnClickListener(CategoryClick(
                        productBreadcrumb.departmentId,
                        this
                ))
            }
            false -> view_category.visibility = View.GONE

        }

        view_free_return.renderData(data)
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.simple_top_bar_close_button) {
            close()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setUpByConfiguration(newConfig)
    }

    override fun onBackPressed() {
        if (!thumbnailIntializing) {
            close()
        } else {
            isBackPressed = true
            return
        }

    }

    internal var thumbnailIntializing = false
    override fun onIntializationStart() {
        thumbnailIntializing = true
    }

    override fun onIntializationComplete() {
        if (isBackPressed) {
            super.onBackPressed()
        }
        thumbnailIntializing = false
    }

    private inner class CategoryClick internal constructor(private val categoryId: String,
                                                           private val context: Context) : View.OnClickListener {

        override fun onClick(v: View) {
            if (!GlobalConfig.isSellerApp()) {
                val intent = RouteManager.getIntentInternal(context,
                    UriUtil.buildUri(ApplinkConstInternal.Marketplace.DISCOVERY_CATEGORY_DETAIL, categoryId))
                startActivity(intent)
            }
        }
    }

    private inner class EtalaseClick internal constructor(private val etalaseName : String,
                                                          private val etalaseId : String,
                                                          private val shopId : String,
                                                          private val context : Context) : View.OnClickListener {
        val KEY_ETALASE_NAME : String = "etalase_name"
        val KEY_ETALASE_ID : String = "etalase_id"
        val KEY_SHOP_ID : String = "shop_id"
        val EMPTY_STRING : String = ""

        override fun onClick(v: View) {
            val bundle = Bundle()
            bundle.putString(KEY_ETALASE_NAME, MethodChecker.fromHtml(etalaseName)?.toString())
            bundle.putString(KEY_ETALASE_ID, etalaseId)
            bundle.putString(KEY_SHOP_ID, shopId)
            val etalaseId = bundle.getString(KEY_ETALASE_ID)

            var intent: Intent? = null
            if (!TextUtils.isEmpty(etalaseId)) {
                intent = (context.applicationContext as PdpRouter).getShoProductListIntent(context,
                        bundle.getString(KEY_SHOP_ID), EMPTY_STRING, etalaseId)
            } else {
                intent = (context.applicationContext as PdpRouter).getShopPageIntent(context,
                        bundle.getString(KEY_SHOP_ID))
            }

            startActivity(intent)
        }
    }
}
