package com.tokopedia.transaction.purchase.detail.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

import static com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData.ORDER_ARRIVED;
import static com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData.ORDER_CHECKED_OUT;
import static com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData.ORDER_FINISHED;
import static com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData.ORDER_PROCESSED;
import static com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData.ORDER_SENT;
import static com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData.ORDER_VERIFIED;

/**
 * Created by kris on 11/7/17. Tokopedia
 */

public class OrderHistoryStepperLayout extends LinearLayout{

    public OrderHistoryStepperLayout(Context context) {
        super(context);
        initView(context);
    }

    public OrderHistoryStepperLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public OrderHistoryStepperLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.order_history_stepper, this, true);

    }

    public void setStepperStatus(OrderHistoryData model) {
        TextView title = findViewById(R.id.stepper_title);
        ImageView stepperImage = findViewById(R.id.stepper_image);
        title.setText(model.getStepperStatusTitle());
        if(model.getHistoryImage() == null
                || model.getHistoryImage().isEmpty()) setVisibility(GONE);
        else ImageHandler.LoadImage(stepperImage, model.getHistoryImage());
        stepperImage.setImageResource(generateStepperImage(model.getStepperMode()));
    }

    private int generateStepperImage(int orderMode) {
        switch (orderMode) {
            case ORDER_FINISHED:
                return R.drawable.stepper_order_finished;
            case ORDER_ARRIVED:
                return R.drawable.stepper_order_arrived;
            case ORDER_SENT:
                return R.drawable.stepper_order_being_delivered;
            case ORDER_PROCESSED:
                return R.drawable.stepper_seller_accept_order;
            case ORDER_VERIFIED:
                return R.drawable.stepper_waiting_seller_response;
            case ORDER_CHECKED_OUT:
                return R.drawable.stepper_waiting_seller_response;
            default:
                return R.drawable.stepper_order_finished;
        }
    }

}
