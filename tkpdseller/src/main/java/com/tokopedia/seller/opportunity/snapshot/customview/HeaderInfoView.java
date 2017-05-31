package com.tokopedia.seller.opportunity.snapshot.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;
import com.tokopedia.core.util.MethodChecker;

/**
 * Created by hangnadi on 3/1/17.
 */

public class HeaderInfoView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    TextView tvName;
    TextView tvPrice;
    TextView tvViewed;
    TextView tvBrought;
    TextView cashbackTextView;
    LinearLayout cashbackHolder;
    TextView titleViewed;
    TextView titleSold;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvViewed = (TextView) findViewById(R.id.tv_viewed);
        tvBrought = (TextView) findViewById(R.id.tv_brought);
        cashbackTextView = (TextView) findViewById(R.id.label_cashback);
        cashbackHolder = (LinearLayout) findViewById(R.id.cashback_holder);
        titleViewed = (TextView) findViewById(R.id.title_viewed);
        titleSold = (TextView) findViewById(R.id.title_sold);
    }

    public HeaderInfoView(Context context) {
        super(context);
    }

    public HeaderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
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
        tvBrought.setVisibility(GONE);
        tvViewed.setVisibility(GONE);
        titleViewed.setVisibility(GONE);
        titleSold.setVisibility(GONE);
        cashbackHolder.setVisibility(GONE);
        tvBrought.setText("");
        tvViewed.setText("");
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvName.setText(MethodChecker.fromHtml(data.getInfo().getProductName()));
        tvPrice.setText(data.getInfo().getProductPrice());
        setVisibility(VISIBLE);
    }

    public void renderTempData(ProductPass productPass) {
        tvName.setText(productPass.getProductName());
        tvPrice.setText(productPass.getProductPrice());
        setVisibility(VISIBLE);
    }
}
