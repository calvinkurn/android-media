package com.tokopedia.transaction.purchase.detail.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.transaction.R;

/**
 * Created by kris on 11/7/17. Tokopedia
 */

public class OrderHistoryStepperLayout extends LinearLayout{


    public OrderHistoryStepperLayout(Context context) {
        super(context);
        initView();
    }

    public OrderHistoryStepperLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public OrderHistoryStepperLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.order_history_stepper, this);

    }

}
