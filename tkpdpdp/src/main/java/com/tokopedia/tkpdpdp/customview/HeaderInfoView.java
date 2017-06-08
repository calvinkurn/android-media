package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;


/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class HeaderInfoView extends BaseView<ProductDetailData, ProductDetailView> {
    private TextView tvName;
    private TextView cashbackTextView;
    private TextView tvPrice;
    private LinearLayout cashbackHolder;


    public HeaderInfoView(Context context) {
        super(context);
    }

    public HeaderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;

    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        cashbackTextView = (TextView) findViewById(R.id.label_cashback);
        cashbackHolder = (LinearLayout) findViewById(R.id.cashback_holder);

    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_header_product_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(INVISIBLE);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvName.setText(MethodChecker.fromHtml(data.getInfo().getProductName()));
        tvPrice.setText(data.getInfo().getProductPrice());
        setVisibility(VISIBLE);

        if (data.getCashBack() != null && !data.getCashBack().getProductCashbackValue().isEmpty()) {
            cashbackHolder.setVisibility(VISIBLE);
            cashbackTextView.setText(data.getCashBack().getProductCashbackValue());
        }

    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(MethodChecker.fromHtml(productPass.getProductName());
        tvPrice.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }
}
