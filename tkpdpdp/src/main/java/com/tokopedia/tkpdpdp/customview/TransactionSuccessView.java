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
 * @author alvarisi on 9/8/16.
 */
public class TransactionSuccessView extends BaseView<ProductDetailData, ProductDetailView> {

    private TextView tvSuccessRate;

    public TransactionSuccessView(Context context) {
        super(context);
    }

    public TransactionSuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_transaction_success;
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
        tvSuccessRate = (TextView) findViewById(R.id.tv_success);
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        switch (data.getStatistic().getProductSuccessRate()) {
            case "0":
                tvSuccessRate.setVisibility(INVISIBLE);
                setVisibility(INVISIBLE);
                break;
            default:
                tvSuccessRate.setVisibility(VISIBLE);
                tvSuccessRate.setText(MethodChecker.fromHtml("<b>"
                        + data.getStatistic().getProductSuccessRate()
                        + "%</b> "
                        + getContext().getString(R.string.tx_success_from)
                        + " <b>"
                        + data.getStatistic().getProductTransactionCount()
                        + "</b> "
                        + getContext().getString(R.string.tx_title)));

                setVisibility(VISIBLE);
                break;
        }
    }
}
