package com.tokopedia.tkpdpdp.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.List;

/**
 * Created by alifa on 5/9/17.
 */

public class OtherProductsView extends BaseView<ProductDetailData, ProductDetailView> {

    private LinearLayout layoutOther;
    private boolean isShopFavorite = false;

    public OtherProductsView(Context context) {
        super(context);
    }

    public OtherProductsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_other_products_pdp;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        layoutOther = (LinearLayout) findViewById(R.id.l_other);

    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        setVisibility(VISIBLE);
    }

    public void renderOtherProduct(List<ProductOther> productOtherList) {
        layoutOther.removeAllViews();
        for (ProductOther productOther : productOtherList) {
            OtherProdItemView otherProdItemView = new OtherProdItemView(getContext());
            otherProdItemView.renderData(productOther);
            otherProdItemView.setOnClickListener(new OtherProductsView.ClickOtherProduct(productOther));
            layoutOther.addView(otherProdItemView);
        }
    }

    private class AnimationFav extends ScaleAnimation {

        AnimationFav(int fromX, float toX, int fromY, float toY, int pivotXType,
                     float pivotXValue, int pivotYType, float pivotYValue) {
            super(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
            setDuration(250);
            setRepeatCount(Animation.INFINITE);
            setRepeatMode(Animation.REVERSE);
            setFillAfter(false);
        }
    }

    private class ClickOtherProduct implements OnClickListener {

        private final ProductOther data;

        ClickOtherProduct(ProductOther productOther) {
            this.data = productOther;
        }

        @Override
        public void onClick(View v) {
            listener.onProductOtherClicked(ProductPass.Builder.aProductPass()
                    .setProductPrice(data.getProductPrice())
                    .setProductId(data.getProductId())
                    .setProductName(data.getProductName())
                    .setProductImage(data.getProductImage())
                    .build());
        }
    }

}

