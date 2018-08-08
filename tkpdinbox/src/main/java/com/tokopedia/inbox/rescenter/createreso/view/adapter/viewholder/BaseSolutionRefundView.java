package com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.inbox.R;

/**
 * @author by yfsx on 07/08/18.
 */
public class BaseSolutionRefundView extends BaseCustomView {

    private TextView tvQty;
    private TextView tvRefund;
    private EditText etRefund;

    public BaseSolutionRefundView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseSolutionRefundView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseSolutionRefundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_solution_refund_base, this);
        tvQty = (TextView) view.findViewById(R.id.tv_qty);
        tvRefund = (TextView) view.findViewById(R.id.tv_max_refund);
        etRefund = (EditText) view.findViewById(R.id.et_fund);
    }
}
