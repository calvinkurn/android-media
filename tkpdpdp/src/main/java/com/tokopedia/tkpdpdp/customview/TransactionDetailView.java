package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductInfo;
import com.tokopedia.core.product.model.productdetail.ReturnInfo;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author mady on 05/08/17
 */
public class TransactionDetailView extends BaseView<ProductDetailData, ProductDetailView> {

    private LinearLayout noFreeReturnView;
    private FreeReturnView freeReturnView;

    private TextView textSold;
    private TextView textSeen;
    private TextView textInsurance;
    private TextView textWeight;

    public TransactionDetailView(Context context) {
        super(context);
    }

    public TransactionDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_transaction_detail;
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
        noFreeReturnView = (LinearLayout) findViewById(R.id.view_no_free_return);
        freeReturnView = (FreeReturnView) findViewById(R.id.view_free_return);
        textSold = (TextView) findViewById(R.id.text_sold);
        textSeen = (TextView) findViewById(R.id.text_view);
        textInsurance = (TextView) findViewById(R.id.text_insurance);
        textWeight = (TextView) findViewById(R.id.text_weight);

    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        setVisibility(VISIBLE);
        if (data.getInfo() != null && data.getInfo().getReturnInfo() != null) {
            textWeight.setText(data.getInfo().getProductWeight()+data.getInfo().getProductWeightUnit());
            textInsurance.setText(data.getInfo().getProductInsurance());
            textSeen.setText(data.getStatistic().getProductViewCount());
            textSold.setText(data.getStatistic().getProductSoldCount());
            ReturnInfo returnInfo = data.getInfo().getReturnInfo();
            if ("".equals(returnInfo.getContent())) {
                noFreeReturnView.setVisibility(VISIBLE);
                freeReturnView.setVisibility(GONE);
            } else {
                freeReturnView.setVisibility(VISIBLE);
                noFreeReturnView.setVisibility(GONE);
                freeReturnView.renderData(data);
            }
        }
    }
}
