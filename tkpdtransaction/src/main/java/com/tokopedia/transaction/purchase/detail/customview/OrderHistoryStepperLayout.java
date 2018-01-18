package com.tokopedia.transaction.purchase.detail.customview;

import android.content.Context;
import android.graphics.Color;
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
        title.setTextColor(Color.parseColor(model.getOrderListData().get(0).getColor()));
        if(model.getHistoryImage() == null
                || model.getHistoryImage().isEmpty()) setVisibility(GONE);
        else ImageHandler.LoadImage(stepperImage, model.getHistoryImage());
    }

}
