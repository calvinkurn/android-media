package com.tokopedia.tkpdpdp.customview

import android.content.Context
import android.os.Bundle
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.View

import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.product.customview.BaseView
import com.tokopedia.core.product.model.goldmerchant.VideoData
import com.tokopedia.core.product.model.productdetail.ProductDetailData
import com.tokopedia.core.util.MethodChecker
import com.tokopedia.tkpdpdp.DescriptionActivity
import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.listener.ProductDetailView

import com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID
import kotlinx.android.synthetic.main.youtube_video_list_place_holder.view.*

/**
 * @author kris on 11/3/16. Tokopedia
 */

class VideoDescriptionLayout : BaseView<ProductDetailData, ProductDetailView> {

    internal var description: String? = ""
    internal var productId = ""
    internal var videoData: VideoData? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    override fun setListener(listener: ProductDetailView) {
        this.listener = listener
    }

    override fun getLayoutView(): Int {
        return R.layout.youtube_video_list_place_holder
    }

    override fun initView(context: Context) {
        super.initView(context)
    }

    override fun parseAttribute(context: Context, attrs: AttributeSet) {

    }

    override fun setViewListener() {
        visibility = View.GONE
    }

    override fun renderData(data: ProductDetailData) {
        description = if (data.info.productDescription == null)
            ""
        else
            data.info.productDescription

        productId = Integer.toString(data.info.productId!!)
        ll_wrapper.setOnClickListener(ClickToggle())
        tv_description.setOnClickListener(ClickToggle())
        tv_description.text = if (description == null
                || description == ""
                || description == "0")
            resources.getString(R.string.no_description_pdp)
        else
            description

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
    }

    fun renderVideoData(data: VideoData, youTubeThumbnailLoadInProcess: YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess) {
        product_video_horizontal_scroll.renderData(data, youTubeThumbnailLoadInProcess)
        videoData = data
    }

    fun destroyVideoLayoutProcess() {
        product_video_horizontal_scroll.destroyAllOnGoingYoutubeProcess()
    }

    fun refreshVideo() {
        product_video_horizontal_scroll.clearYoutubeVideo()
    }

    private inner class ClickToggle : View.OnClickListener {
        override fun onClick(v: View) {
            val bundle = Bundle()
            bundle.putString(DescriptionActivity.KEY_DESCRIPTION, description)
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            if (videoData != null) bundle.putParcelable(DescriptionActivity.KEY_VIDEO, videoData)
            listener.onDescriptionClicked(bundle)
            UnifyTracking.eventPDPExpandDescription()
        }
    }

    companion object {
        val MAX_CHAR = 300
        private val MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>"
    }
}