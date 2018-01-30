package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpdpdp.CourierActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author alifa on 5/8/17.
 */

public class RatingTalkCourierView extends BaseView<ProductDetailData, ProductDetailView> {
    private ImageView ivQualityRate;
    private TextView tvReview;
    private TextView tvTalk;
    private TextView tvCourier;
    private LinearLayout talkContainer;
    private LinearLayout reviewContainer;
    private LinearLayout courierContainer;


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
        ivQualityRate = (ImageView) findViewById(R.id.iv_quality);
        tvReview = (TextView) findViewById(R.id.tv_review);
        tvTalk = (TextView) findViewById(R.id.tv_talk);
        tvCourier = (TextView) findViewById(R.id.tv_courier);
        talkContainer = (LinearLayout) findViewById(R.id.talk_container);
        reviewContainer = (LinearLayout) findViewById(R.id.review_container);
        courierContainer = (LinearLayout) findViewById(R.id.courier_container);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        ivQualityRate
                .setImageResource(getRatingDrawable(data.getRating().getProductRatingStarPoint()));

        String ulasan = getContext().getString(R.string.ulasan);
        String review = data.getStatistic().getProductReviewCount() + " " + ulasan;
        String diskusi = getContext().getString(R.string.diskusi);
        String talk = data.getStatistic().getProductTalkCount() + " " + diskusi;
        String kurir = getContext().getString(R.string.kurir);
        tvReview.setText(review);
        tvTalk.setText(talk);
        talkContainer.setOnClickListener(new ClickTalk(data));
        reviewContainer.setOnClickListener(new ClickReview(data));
        int courierCount = 0;
        if (data.getShopInfo().getShopShipments() != null) {
            courierCount = data.getShopInfo().getShopShipments().size();
        }

        String courierText = String.valueOf(courierCount) + " " + kurir;
        tvCourier.setText(courierText);
        courierContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(CourierActivity.KEY_COURIER_DATA,
                        data.getShopInfo().getShopShipments());
                listener.onCourierClicked(bundle);
            }
        });

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
            listener.onProductTalkClicked(bundle);
            if(data != null) {
                TrackingUtils.sendMoEngageClickDiskusi(data);
            }
        }
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

                TrackingUtils.sendMoEngageClickUlasan(data);
            }
        }
    }
}
