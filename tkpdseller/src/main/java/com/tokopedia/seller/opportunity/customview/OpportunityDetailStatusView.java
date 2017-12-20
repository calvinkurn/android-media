package com.tokopedia.seller.opportunity.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailStatusView extends BaseView<OpportunityItemViewModel, OpportunityView> {

    private TextView cashback;
    private TextView deadline;
    private TextView destinationCity;
    private TextView shippingMethod;
    private TextView tvReputationPoint;
    private View vgReputationLabel;

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
        View view = inflater.inflate(getLayoutView(), this, true);
        cashback = (TextView) view.findViewById(R.id.value_cashback);
        deadline = (TextView) view.findViewById(R.id.value_response_time);
        destinationCity = (TextView) view.findViewById(R.id.value_destination);
        shippingMethod = (TextView) view.findViewById(R.id.value_shipping);
        tvReputationPoint = (TextView) view.findViewById(R.id.reputation_point);
        vgReputationLabel = view.findViewById(R.id.vg_reputation_label);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull OpportunityItemViewModel data) {
        String color = data.getOrderDeadline().getDeadlineColor();
        if (!TextUtils.isEmpty(color)) {
            Drawable deadlineDrawable = deadline.getBackground();
            deadlineDrawable.clearColorFilter();
            int colorInt = Color.parseColor(color);
            ColorFilter cf = new
                    PorterDuffColorFilter(colorInt, PorterDuff.Mode
                    .MULTIPLY);
            deadlineDrawable.setColorFilter(cf);
        }
        String cashbackIDR = data.getOrderCashbackIdr();
        cashback.setText(TextUtils.isEmpty(cashbackIDR)?"-":cashbackIDR);
        deadline.setText(data.getOrderDeadline().getDeadlineProcess());
        destinationCity.setText(data.getOrderDestination().getAddressCity());
        String shipping = data.getOrderShipment().getShipmentName()
                + " - "
                + data.getOrderShipment().getShipmentProduct();
        shippingMethod.setText(shipping);

        Drawable drawable = tvReputationPoint.getBackground();
        drawable.clearColorFilter();
        String colorStr = data.getReplacementMultiplierColor();
        if (!TextUtils.isEmpty(colorStr)){
            int colorInt = Color.parseColor(colorStr);
            ColorFilter cf = new
                    PorterDuffColorFilter(colorInt, PorterDuff.Mode
                            .MULTIPLY);
            drawable.setColorFilter(cf);
            tvReputationPoint.setTextColor(colorInt);
            tvReputationPoint.invalidate();
        }
        tvReputationPoint.setText(data.getReplacementMultiplierText());

        vgReputationLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReputationLabelClicked();
            }
        });
    }

}
