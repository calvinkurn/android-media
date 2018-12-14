package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.DescriptionActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID;

/**
 * @author kris on 11/3/16. Tokopedia
 */

public class VideoDescriptionLayout extends BaseView<ProductDetailData, ProductDetailView> {

    private DescriptionTextView tvDesc;
    private LinearLayout descriptionContainer;
    private LinearLayout container;
    private TextView desc;
    private ProductVideoHorizontalScroll productVideoHorizontalScroll;

    String description = "";
    String productId = "";
    VideoData videoData;

    public static final int MAX_CHAR = 300;
    private static final String MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>";

    public VideoDescriptionLayout(Context context) {
        super(context);
        initView(context);
    }

    public VideoDescriptionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.youtube_video_list_place_holder;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvDesc = (DescriptionTextView) findViewById(R.id.tv_description);
        desc = findViewById(R.id.desc);
        ImageView ivToggle = (ImageView) findViewById(R.id.iv_toggle);
        descriptionContainer = (LinearLayout) findViewById(R.id.tv_desc);
        productVideoHorizontalScroll
                = (ProductVideoHorizontalScroll) findViewById(R.id.product_video_horizontal_scroll);
        container = (LinearLayout) findViewById(R.id.ll_wrapper);
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        desc.setText(getContext().getString(R.string.title_desc));
        description = data.getInfo().getProductDescription() == null ? "" :
                data.getInfo().getProductDescription();
        productId = Integer.toString(data.getInfo().getProductId());
        ClickToggle clickToggleDescription = new VideoDescriptionLayout.ClickToggle();
        container.setOnClickListener(clickToggleDescription);
        tvDesc.setOnClickListener(clickToggleDescription);
        tvDesc.setText(description == null
                || description.equals("")
                || description.equals("0")
                ? getResources().getString(R.string.no_description_pdp) : description);
        tvDesc.setAutoLinkMask(0);
        try {
            Linkify.addLinks(tvDesc, Linkify.WEB_URLS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MethodChecker.fromHtml(tvDesc.getText().toString()).length() > MAX_CHAR) {
            String subDescription = MethodChecker.fromHtml(description).toString().substring(0, MAX_CHAR);
            tvDesc.setText(MethodChecker.fromHtml(subDescription.replaceAll("(\r\n|\n)", "<br />") + "..."
                    + MORE_DESCRIPTION));
        } else {
            tvDesc.setText(MethodChecker.fromHtml(tvDesc.getText().toString()));
        }
        descriptionContainer.setVisibility(VISIBLE);

        Drawable drawableShadow = AppCompatResources.getDrawable(getContext(), R.drawable.bg_shadow);
        if (drawableShadow != null) {
            container.setBackground(drawableShadow);
        }


        float density = getContext().getResources().getDisplayMetrics().density;
        float padding = 10 * density;

        container.setPadding((int) padding, (int) padding, (int) padding, (int) padding);

        setVisibility(VISIBLE);
    }

    public void renderVideoData(VideoData data,YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess youTubeThumbnailLoadInProcess) {
        productVideoHorizontalScroll.renderData(data,youTubeThumbnailLoadInProcess);
        videoData = data;
    }

    public void destroyVideoLayoutProcess() {
        productVideoHorizontalScroll.destroyAllOnGoingYoutubeProcess();
    }

    public void refreshVideo() {
        productVideoHorizontalScroll.clearYoutubeVideo();
    }

    private class ClickToggle implements OnClickListener {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(DescriptionActivity.KEY_DESCRIPTION, description);
            bundle.putString(EXTRA_PRODUCT_ID, productId);
            if (videoData != null) bundle.putParcelable(DescriptionActivity.KEY_VIDEO, videoData);
            listener.onDescriptionClicked(bundle);
            UnifyTracking.eventPDPExpandDescription(getContext());
        }
    }
}