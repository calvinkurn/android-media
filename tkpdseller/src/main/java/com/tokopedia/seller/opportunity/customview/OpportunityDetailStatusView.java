package com.tokopedia.seller.opportunity.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailStatusView extends BaseView<OpportunityItemViewModel, OpportunityView> {

    TextView cashback;
    TextView deadline;
    TextView destinationCity;
    TextView shippingMethod;

    public OpportunityDetailStatusView(Context context) {
        super(context);
    }

    public OpportunityDetailStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(OpportunityView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_opportunity_status_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(),this, true);
        cashback = (TextView) view.findViewById(R.id.value_cashback);
        deadline = (TextView) view.findViewById(R.id.value_response_time);
        destinationCity = (TextView) view.findViewById(R.id.value_destination);
        shippingMethod = (TextView) view.findViewById(R.id.value_shipping);

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull OpportunityItemViewModel data) {
        cashback.setText(data.getOrderCashbackIdr());
        deadline.setText(data.getOrderDeadline().getDeadlineProcess());
        destinationCity.setText(data.getOrderDestination().getAddressCity());
        String shipping = data.getOrderShipment().getShipmentName()
                + " - "
                + data.getOrderShipment().getShipmentProduct();
        shippingMethod.setText(shipping);
    }

}
