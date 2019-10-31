package com.tokopedia.seller.opportunity.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailSummaryView extends BaseView<OpportunityItemViewModel, OpportunityView> {

    TextView totalPrice;

    public OpportunityDetailSummaryView(Context context) {
        super(context);
    }

    public OpportunityDetailSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OpportunityView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_opportunity_summary_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        totalPrice = (TextView) view.findViewById(R.id.text_total_price);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull OpportunityItemViewModel data) {
        totalPrice.setText(data.getOrderDetail().getDetailOpenAmountIdr());
    }
}
