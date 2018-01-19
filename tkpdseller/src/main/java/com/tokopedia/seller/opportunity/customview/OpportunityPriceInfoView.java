package com.tokopedia.seller.opportunity.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityPriceInfoViewModel;

/**
 * Created by normansyahputa on 1/15/18.
 */

public class OpportunityPriceInfoView extends BaseView<OpportunityPriceInfoViewModel, OpportunityView> {

    private TextView title;
    private TextView nonStrikeThroughText;
    private TextView strikeThroughText;

    public OpportunityPriceInfoView(Context context) {
        super(context);
    }

    public OpportunityPriceInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OpportunityView listener) {

    }

    @Override
    protected void initView(Context context) {
        super.initView(context);

        title = findViewById(R.id.title);
        nonStrikeThroughText = findViewById(R.id.non_strike_through_text);
        strikeThroughText = findViewById(R.id.strike_through_detail);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_opportunity_price_info;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull OpportunityPriceInfoViewModel data) {
        title.setText(data.getTitle());
        nonStrikeThroughText.setText(data.getNonStrikeThroughText());
        strikeThroughText.setText(data.getStrikeThroughText());
    }
}
