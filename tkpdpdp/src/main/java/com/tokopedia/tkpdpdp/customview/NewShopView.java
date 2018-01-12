package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author Angga.Prasetiyo on 02/11/2015.
 */
public class NewShopView extends BaseView<ProductDetailData, ProductDetailView> {

    public NewShopView(Context context) {
        super(context);
    }

    public NewShopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_new_shop_product_info;
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
        if (SessionHandler.isV4Login(getContext())) {
            if (SessionHandler.isUserHasShop(getContext())){
                setVisibility(View.GONE);
            } else {
                setVisibility(View.VISIBLE);
            }
        } else {
            setVisibility(View.VISIBLE);
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProductNewShopClicked();
            }
        });
    }
}
