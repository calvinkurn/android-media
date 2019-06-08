package com.tokopedia.discovery.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.listener.ProductDetailView;

public class RatingView extends BaseView<ProductDetailData, ProductDetailView> {
    private TextView tvQualityRate;
    private TextView tvAccuracyRate;
    private ImageView ivQualityRate;
    private ImageView ivAccuracyRate;

    public RatingView(Context context) {
        super(context);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_rate_product_info;
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
        tvQualityRate = (TextView) findViewById(R.id.tv_quality);
        tvAccuracyRate = (TextView) findViewById(R.id.tv_accuracy);
        ivQualityRate = (ImageView) findViewById(R.id.iv_quality);
        ivAccuracyRate = (ImageView) findViewById(R.id.iv_accuracy);


    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        tvQualityRate.setText(data.getRating().getProductRatingPoint());
        tvAccuracyRate.setText(data.getRating().getProductRateAccuracyPoint());
        ivQualityRate.setImageResource(getRatingDrawable(data.getRating().getProductRatingStarPoint()));
        ivAccuracyRate.setImageResource(getRatingDrawable(data.getRating().getProductAccuracyStarRate()));

        setOnClickListener(v -> {
            String productId = String.valueOf(data.getInfo().getProductId());
            String shopId = String.valueOf(data.getShopInfo().getShopId());
            String productName =  String.valueOf(data.getShopInfo().getShopId());
            listener.onProductRatingClicked(productId, shopId, productName);
        });

        setVisibility(VISIBLE);
    }


    public static int getRatingDrawable(int param) {
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
}
