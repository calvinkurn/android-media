package com.tokopedia.tkpdpdp.customview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.core.analytics.AppEventTracking

import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.analytics.nishikino.model.EventTracking
import com.tokopedia.core.product.customview.BaseView
import com.tokopedia.core.product.model.goldmerchant.VideoData
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.core.util.MethodChecker
import com.tokopedia.tkpdpdp.DescriptionActivity
import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.listener.ProductDetailView

import com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.tkpdpdp.DescriptionActivityNew
import com.tokopedia.track.TrackApp
import kotlinx.android.synthetic.main.youtube_video_list_place_holder.view.*

/**
 * @author kris on 11/3/16. Tokopedia
 */

class VideoDescriptionLayout : BaseCustomView {

    internal var description: String? = ""
    internal var productId = ""
    internal var videoData: VideoData? = null
    internal var listener: ProductDetailView? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    fun initView() {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(getLayoutView(), this, true)
    }

    fun setListener(listener: ProductDetailView) {
        this.listener = listener
    }

    fun getLayoutView(): Int {
        return R.layout.youtube_video_list_place_holder
    }

    fun setViewListener() {
        visibility = View.GONE
    }

    fun renderData(data: ProductDetailData) {
        description = if (data.info.productDescription == null)
            ""
        else
            data.info.productDescription

        productId = Integer.toString(data.info.productId!!)
        ll_wrapper.setOnClickListener(ClickToggle(data))
        tv_description.setOnClickListener(ClickToggle(data))
        tv_description.text = if (description == null
                || description == ""
                || description == "0")
            resources.getString(R.string.no_description_pdp)
        else
            description

        title_description.setOnClickListener(null)

        if (MethodChecker.fromHtml(tv_description.getText().toString()).length > MAX_CHAR) {
            val subDescription = MethodChecker.fromHtml(description).toString().substring(0, MAX_CHAR)
            tv_description.setText(MethodChecker.fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "..."))
        } else {
            tv_description.setText(MethodChecker.fromHtml(tv_description.getText().toString()))
        }

        tv_description.autoLinkMask = 0
        try {
            Linkify.addLinks(tv_description, Linkify.WEB_URLS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tv_description.visibility = View.VISIBLE
        visibility = View.VISIBLE
        ll_wrapper.visibility = View.VISIBLE
    }

    fun renderVideoData(data: VideoData, youTubeThumbnailLoadInProcess: YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess) {
        product_video_horizontal_scroll.renderData(data, youTubeThumbnailLoadInProcess)
        product_video_horizontal_scroll.visibility = View.VISIBLE
        videoData = data
    }

    fun destroyVideoLayoutProcess() {
        product_video_horizontal_scroll.destroyAllOnGoingYoutubeProcess()
    }

    fun refreshVideo() {
        product_video_horizontal_scroll.clearYoutubeVideo()
    }

    private inner class ClickToggle(val data : ProductDetailData) : View.OnClickListener {
        override fun onClick(v: View) {
            val intent = Intent()
            with(DescriptionActivityNew.IntentOptions){
                intent.video = videoData
                intent.description = description
                intent.name = data.info.productName
                intent.price = data.info.productPrice
                intent.isOfficialStore = data.shopInfo.isOfficial
                intent.shopName = data.shopInfo.shopName
                intent.imgUrl = data.productImages[0].imageSrc300
            }

            listener?.onDescriptionClicked(intent)
            eventPDPExpandDescription(
                    this@VideoDescriptionLayout.context)
        }
    }

    fun eventPDPExpandDescription(context: Context) {
        TrackApp.getInstance()!!.gtm.sendGeneralEvent(
            AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
            AppEventTracking.Category.PRODUCT_DETAIL,
            AppEventTracking.Action.CLICK,
            AppEventTracking.EventLabel.PRODUCT_DESCRIPTION)
    }

    companion object {
        val MAX_CHAR = 300
        private val MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>"
    }
}