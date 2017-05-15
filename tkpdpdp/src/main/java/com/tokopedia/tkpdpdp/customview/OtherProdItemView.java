package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import butterknife.BindView;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
public class OtherProdItemView extends BaseView<ProductOther, ProductDetailView> {
    private static final String TAG = OtherProdItemView.class.getSimpleName();

    @BindView(R2.id.iv_pic)
    ImageView ivPic;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_price)
    TextView tvPrice;

    public OtherProdItemView(Context context) {
        super(context);
    }

    public OtherProdItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_other_product_item;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull ProductOther data) {
        tvName.setText(data.getProductName());
        tvPrice.setText(data.getProductPrice());
        ImageHandler.LoadImageResize(getContext(), ivPic, data.getProductImage(), 300, 300);
//        ImageHandler.LoadImageRounded(ivPic, data.getProductImage());
    }
}
