package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ReturnInfo;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * @author mady on 05/08/17
 */
public class TransactionDetailView extends BaseView<ProductDetailData, ProductDetailView> {
    private LinearLayout noFreeReturnView;
    private FreeReturnView freeReturnView;

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
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        setVisibility(VISIBLE);
        if (data.getInfo() != null && data.getInfo().getReturnInfo() != null) {
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
