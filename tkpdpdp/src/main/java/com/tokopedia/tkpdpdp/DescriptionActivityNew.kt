package com.tokopedia.tkpdpdp

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.text.util.Linkify
import android.view.View
import android.view.WindowManager
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.core.`var`.TkpdCache.Key.STATE_ORIENTATION_CHANGED
import com.tokopedia.core.`var`.TkpdCache.PRODUCT_DETAIL
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.product.model.goldmerchant.VideoData
import com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID
import com.tokopedia.core.util.MethodChecker
import com.tokopedia.tkpdpdp.customview.YoutubeThumbnailViewHolder
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.simple_top_bar_layout.*
import kotlin.reflect.KProperty

class DescriptionActivityNew : AppCompatActivity(),
        View.OnClickListener,
        YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess{

    private lateinit var localCacheHandler: LocalCacheHandler
    private var isBackPressed: Boolean = false

    companion object IntentOptions {
        private const val KEY_DESCRIPTION = "PRODUCT_DETAIL_DESCRIPTION"
        private const val KEY_VIDEO = "PRODUCT_DETAIL_VIDEO"
        private const val KEY_NAME = "PRODUCT_NAME"
        private const val KEY_PRICE = "PRODUCT_PRICE"
        private const val KEY_SHOP_NAME = "PRODUCT_SHOP_NAME"
        private const val KEY_IS_OFFICIAL_STORE = "PRODUCT_IS_OFFICIAL_STORE"
        private const val KEY_PRODUCT_IMAGE_URL = "PRODUCT_IMAGE_URL"

        var Intent.description : String? by IntentExtraStringDelegate(KEY_DESCRIPTION)
        var Intent.video: VideoData? by IntentExtraParcelableDelegate(KEY_VIDEO)
        var Intent.name: String? by IntentExtraStringDelegate(KEY_NAME)
        var Intent.price: String? by IntentExtraStringDelegate(KEY_PRICE)
        var Intent.shopName: String? by IntentExtraStringDelegate(KEY_SHOP_NAME)
        var Intent.isOfficialStore: Boolean? by IntentExtraBooleanDelegate(KEY_IS_OFFICIAL_STORE)
        var Intent.imgUrl: String? by IntentExtraStringDelegate(KEY_PRODUCT_IMAGE_URL)
    }

    class IntentExtraStringDelegate(val name : String) {
        operator fun getValue(intent : Intent,
                              property : KProperty<*>) : String? =
                intent.getStringExtra(name)
        operator fun setValue(intent : Intent,property: KProperty<*>, value : String?){
            intent.putExtra(name, value)
        }
    }

    class IntentExtraBooleanDelegate(val name : String) {
        operator fun getValue(intent : Intent,
                              property : KProperty<*>) : Boolean? =
                intent.getBooleanExtra(name, false)
        operator fun setValue(intent : Intent,property: KProperty<*>, value : Boolean?){
            intent.putExtra(name, value)
        }
    }

    class IntentExtraParcelableDelegate(val name : String) {
        operator fun getValue(intent : Intent,
                              property : KProperty<*>) : VideoData? =
                intent.getParcelableExtra(name)
        operator fun setValue(intent : Intent,property: KProperty<*>, value : VideoData?){
            intent.putExtra(name, value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Tokopedia3)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(com.tokopedia.core.R.color.green_600)
        }
        localCacheHandler = LocalCacheHandler(this,  PRODUCT_DETAIL)
        setContentView(getLayout())
        hideToolbar()
        showData()
        setupTopbar()
    }

    fun getLayout(): Int {
        return R.layout.activity_description;
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    private fun setUpByConfiguration(configuration: Configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!localCacheHandler.getBoolean(STATE_ORIENTATION_CHANGED)) {
                val productId : String = intent.getStringExtra(EXTRA_PRODUCT_ID) ?: ""
                UnifyTracking.eventPDPOrientationChanged(this, productId)
                localCacheHandler.putBoolean(STATE_ORIENTATION_CHANGED, java.lang.Boolean.TRUE)
                localCacheHandler.applyEditor()
            }
        }
    }

    private fun setupTopbar() {
        simple_top_bar_title.setText(getString(R.string.description_page_title))
        simple_top_bar_close_button.setOnClickListener {
            finish()
        }
    }

    fun showData() {
        supportPostponeEnterTransition()

        val productDescription = intent.getStringExtra(KEY_DESCRIPTION)
        tv_description.text =
                if (productDescription == null
                        || productDescription == ""
                        || productDescription == "0")
                    resources.getString(R.string.no_description_pdp)
                else
                    productDescription

        tv_description.setAutoLinkMask(0)
        Linkify.addLinks(tv_description, Linkify.WEB_URLS)
        scroll_view.setVisibility(View.VISIBLE)
        tv_description.text = MethodChecker.fromHtml(tv_description.text.toString())
        val videoData = intent.getParcelableExtra<VideoData>(KEY_VIDEO)
        if (videoData != null) {
            product_video_horizontal_scroll.setVisibility(View.VISIBLE)
            product_video_horizontal_scroll.renderData(videoData, this)
        } else {
            product_video_horizontal_scroll.setVisibility(View.GONE)
        }

        ImageHandler.loadImageAndCache(
                img_product, intent.imgUrl
        )
        tv_name.text = com.tokopedia.abstraction.common.utils.view.MethodChecker.fromHtml(intent.name)
        tv_price.text = intent.price
        when (intent.isOfficialStore) {
            true -> iv_official.visibility = View.VISIBLE
            false -> iv_official.visibility = View.GONE
        }
        tv_shop_name.text = intent.shopName

        supportStartPostponedEnterTransition()
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.simple_top_bar_close_button) {
            close()
        }
    }

    fun close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
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
}
