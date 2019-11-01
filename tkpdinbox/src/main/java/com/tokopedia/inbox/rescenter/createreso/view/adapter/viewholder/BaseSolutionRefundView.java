package com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionProblemModel;

/**
 * @author by yfsx on 07/08/18.
 */
public class BaseSolutionRefundView extends BaseCustomView {

    private TextView tvRefund;
    private EditText etRefund;

    private SolutionDetailFragmentListener.View mainView;
    private ComplaintResult complaintResult;

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
        setView();
    }

    private void setView() {
        View view = inflate(getContext(), R.layout.item_solution_refund_base, this);
        tvRefund = (TextView) view.findViewById(R.id.tv_max_refund);
        etRefund = (EditText) view.findViewById(R.id.et_fund);
    }

    public void bind(SolutionProblemModel element, SolutionDetailFragmentListener.View mainView, ComplaintResult complaintResult) {
        this.mainView = mainView;
        this.complaintResult = complaintResult;
        initView(element);
        setViewListener(element);
    }

    private void initView(SolutionProblemModel element) {
        int refundAmount = complaintResult.problem.amount == 0 ? element.getAmount().getInteger() : complaintResult.problem.amount;
        etRefund.setText(String.valueOf(refundAmount));
        complaintResult.problem.amount = refundAmount;
        tvRefund.setText(element.getMaxAmount().getIdr());
        mainView.initAmountToResult(complaintResult);
    }

    private void setViewListener(SolutionProblemModel element) {
        etRefund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onAmountChanged(charSequence.toString(), element);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onAmountChanged(String amount, SolutionProblemModel element) {
        if (!amount.equals("")) {
            int intAmount = Integer.parseInt(amount);
            if (element.getMaxAmount().getInteger() != 0 && intAmount > element.getMaxAmount().getInteger()) {
                complaintResult.problem.amount = element.getAmount().getInteger();
                etRefund.setText(String.valueOf(element.getMaxAmount().getInteger()));
            } else {
                complaintResult.problem.amount = intAmount;
            }
        } else {
            complaintResult.problem.amount = 0;
            etRefund.setText(String.valueOf(0));
        }
        mainView.calculateTotalRefund(complaintResult);
    }
}
