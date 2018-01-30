package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author Angga.Prasetiyo on 29/10/2015.
 */
public class TalkReviewView extends BaseView<ProductDetailData, ProductDetailView> {
    private LinearLayout llTalk;
    private TextView tvTalk;
    private LinearLayout llReview;
    private TextView tvReview;

    public TalkReviewView(Context context) {
        super(context);
    }

    public TalkReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_talk_review_product_info;
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
        llTalk = (LinearLayout) findViewById(R.id.l_talk);
        tvTalk = (TextView) findViewById(R.id.tv_talk);
        llReview = (LinearLayout) findViewById(R.id.l_review);
        tvReview = (TextView) findViewById(R.id.tv_review);

    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvTalk.setText(data.getStatistic().getProductTalkCount());
        tvReview.setText(data.getStatistic().getProductReviewCount());

        llTalk.setOnClickListener(new ClickTalk(data));
        llReview.setOnClickListener(new ClickReview(data));
        
        setVisibility(VISIBLE);
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
        }
    }

    private class ClickReview implements OnClickListener {
        private final ProductDetailData data;

        ClickReview(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {

            String productId = String.valueOf(data.getInfo().getProductId());
            String shopId = String.valueOf(data.getShopInfo().getShopId());
            String productName = data.getInfo().getProductName();
            listener.onProductReviewClicked(productId, shopId, productName);
        }
    }
}
