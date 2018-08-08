package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author Angga.Prasetiyo on 02/11/2015.
 */
public class LastUpdateView extends BaseView<ProductDetailData, ProductDetailView> {
    private TextView tvLastUpdate;

    public LastUpdateView(Context context) {
        super(context);
    }

    public LastUpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_last_update_product;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvLastUpdate = (TextView) findViewById(R.id.tv_last_update);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvLastUpdate.setText(MethodChecker.fromHtml(getContext().getString(R.string.title_last_update_price)
                + " "
                + data.getInfo().getProductLastUpdate()));
        setVisibility(VISIBLE);
    }
}
