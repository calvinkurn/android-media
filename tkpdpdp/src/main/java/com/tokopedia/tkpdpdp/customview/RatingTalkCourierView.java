package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.revamp.ProductViewData;
import com.tokopedia.track.TrackApp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alifa on 5/8/17.
 */

public class RatingTalkCourierView extends BaseView<ProductDetailData, ProductDetailView> {
    private RatingBarWithTextView productRating;
    private TextView tvReview;
    private TextView tvTalk;
    private TextView tvCourier;
    private LinearLayout talkContainer;
    private LinearLayout reviewContainer;
    private LinearLayout courierContainer;
    private VideoData videoData;


    public RatingTalkCourierView(Context context) {
        super(context);
    }

    public RatingTalkCourierView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_rating_talk_courier;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        productRating = findViewById(R.id.product_rating);
        tvReview = (TextView) findViewById(R.id.tv_review);
        tvTalk = (TextView) findViewById(R.id.tv_talk);
        talkContainer = (LinearLayout) findViewById(R.id.talk_container);
        reviewContainer = (LinearLayout) findViewById(R.id.review_container);
        courierContainer = (LinearLayout) findViewById(R.id.courier_container);
        tvCourier = findViewById(R.id.tv_courier);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {

    }

    public void renderData(@NonNull ProductDetailData data, @NonNull ProductViewData viewData) {
        productRating.setRating(
                data.getRating().getProductRatingPoint()
        );
        tvReview.setText(String.format("%1$s %2$s", data.getStatistic().getProductReviewCount(), getContext().getString(R.string.ulasan)));
        tvTalk.setText(String.format("%1$s %2$s",data.getStatistic().getProductTalkCount(), getContext().getString(R.string.diskusi)));
        courierContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCourierClicked(
                        viewData.getProductId(),
                        viewData.getCourierList()
                );
            }
        });
        tvCourier.setText(
                String.format(getResources().getString(R.string.value_courier_count),
                        data.getShopInfo().getShopShipments().size())
        );
        talkContainer.setOnClickListener(new ClickTalk(data));
        reviewContainer.setOnClickListener(new ClickReview(data));
        setVisibility(VISIBLE);
    }

    public void setVideoData(VideoData data, YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess youTubeThumbnailLoadInProcess){
        this.videoData = data;
    }

    public void renderTempdata(ProductPass productPass) {
        productRating.setRating(
                productPass.getStarRating()
        );
        tvReview.setText(String.format("%1$s %2$s", productPass.getCountReview(), getContext().getString(R.string.ulasan)));
        tvTalk.setText(String.format("%1$s %2$s", productPass.getCountDiscussion(), getContext().getString(R.string.diskusi)));
        setVisibility(VISIBLE);
    }


    private int getRatingDrawable(int param) {
        switch (param) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.ic_star_one;
            case 2:
                return R.drawable.ic_star_two;
            case 3:
                return R.drawable.ic_star_three;
            case 4:
                return R.drawable.ic_star_four;
            case 5:
                return R.drawable.ic_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }

    private class ClickTalk implements OnClickListener {
        private final ProductDetailData data;

        ClickTalk(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
            bundle.putString("shop_id", String.valueOf(data.getShopInfo().getShopId()));
            bundle.putString("prod_name", data.getInfo().getProductName());
            bundle.putString("is_owner", String.valueOf(data.getShopInfo().getShopIsOwner()));
            bundle.putString("product_image", data.getProductImages().get(0).getImageSrc300());
            bundle.putString("product_price", data.getInfo().getProductPrice());
            listener.onProductTalkClicked(bundle);
            if(data != null) {
                sendMoEngageClickDiskusi(getContext(), data);
            }
        }
    }

    public static void sendMoEngageClickDiskusi(Context context, @NonNull ProductDetailData data) {
        Map<String, Object> value = new HashMap<>();
        if (data.getBreadcrumb().size() > 1) {
           value.put(AppEventTracking.MOENGAGE.SUBCATEGORY, data.getBreadcrumb().get(1).getDepartmentName());
           value.put(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, data.getBreadcrumb().get(1).getDepartmentId());
           value.put(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
           value.put(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        } else if (data.getBreadcrumb().size() == 1) {
           value.put(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
           value.put(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        }

        if (data.getInfo() != null) {
           value.put(AppEventTracking.MOENGAGE.PRODUCT_NAME, data.getInfo().getProductName());
           value.put(AppEventTracking.MOENGAGE.PRODUCT_ID, data.getInfo().getProductId() + "");
           value.put(AppEventTracking.MOENGAGE.PRODUCT_URL, data.getInfo().getProductUrl());
           value.put(AppEventTracking.MOENGAGE.PRICE, data.getInfo().getProductPrice());
        }

        if (data.getShopInfo() != null) {
            value.put(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, data.getShopInfo().getShopIsOfficial() == 1);
           value.put(AppEventTracking.MOENGAGE.SHOP_ID, data.getShopInfo().getShopId());
           value.put(AppEventTracking.MOENGAGE.SHOP_NAME, data.getShopInfo().getShopName());
        }

        if (data.getProductImages() != null
                && data.getProductImages().size() > 0
                && data.getProductImages().get(0) != null) {
           value.put(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, data.getProductImages().get(0).getImageSrc());
        }

        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.CLICKED_DISKUSI_PDP);
    }

    private class ClickReview implements OnClickListener {
        private final ProductDetailData data;

        ClickReview(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if(data != null) {
                String productId = String.valueOf(data.getInfo().getProductId());
                String shopId = String.valueOf(data.getShopInfo().getShopId());
                String productName = data.getInfo().getProductName();
                listener.onProductReviewClicked(productId, shopId, productName);

                sendMoEngageClickUlasan(getContext(), data);
            }
        }
    }

    public void sendMoEngageClickUlasan(Context context, @NonNull ProductDetailData data) {
        Map<String, Object> value = new HashMap<>();
        if (data.getBreadcrumb().size() > 1) {
            value.put(AppEventTracking.MOENGAGE.SUBCATEGORY, data.getBreadcrumb().get(1).getDepartmentName());
            value.put(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, data.getBreadcrumb().get(1).getDepartmentId());
            value.put(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
            value.put(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        } else if (data.getBreadcrumb().size() != 0) {
            value.put(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
            value.put(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        }

        if (data.getInfo() != null) {
            value.put(AppEventTracking.MOENGAGE.PRODUCT_NAME, data.getInfo().getProductName());
            value.put(AppEventTracking.MOENGAGE.PRODUCT_ID, data.getInfo().getProductId() + "");
            value.put(AppEventTracking.MOENGAGE.PRODUCT_URL, data.getInfo().getProductUrl());
            value.put(AppEventTracking.MOENGAGE.PRICE, data.getInfo().getProductPrice());
        }

        if (data.getShopInfo() != null) {
            value.put(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, data.getShopInfo().getShopIsOfficial() == 1);
            value.put(AppEventTracking.MOENGAGE.SHOP_ID, data.getShopInfo().getShopId());
            value.put(AppEventTracking.MOENGAGE.SHOP_NAME, data.getShopInfo().getShopName());
        }

        if (data.getProductImages() != null
                && data.getProductImages().size() > 0
                && data.getProductImages().get(0) != null) {
            value.put(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, data.getProductImages().get(0).getImageSrc());
        }
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.CLICKED_ULASAN_PDP);
    }
}
