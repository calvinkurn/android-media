package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author ANGGA on 10/29/2015.
 */
public class ErrorShopView extends BaseView<ProductDetailData, ProductDetailView> {

    private TextView tvTitle;
    private TextView tvSubTitle;

    public ErrorShopView(Context context) {
        super(context);
    }

    public ErrorShopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
        tvTitle = (TextView) findViewById(R.id.tv_error_tilte_product);
        tvSubTitle = (TextView) findViewById(R.id.tv_error_subtilte_product);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_error_product;
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
        switch (data.getShopInfo().getShopStatus()) {
            case 1:
                setVisibility(View.GONE);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                listener.onProductShopInfoError();
                setVisibility(VISIBLE);
                tvTitle.setText(data.getShopInfo().getShopStatusTitle() != null
                        && !data.getShopInfo().getShopStatusTitle().isEmpty()
                        ? data.getShopInfo().getShopStatusTitle() : "");
                tvSubTitle.setText(data.getShopInfo().getShopStatusMessage() != null
                        && !data.getShopInfo().getShopStatusMessage().isEmpty()
                        ? data.getShopInfo().getShopStatusMessage() : "");
                break;
        }
    }
}
