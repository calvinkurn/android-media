package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.SolutionSpinnerAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditSolutionListener;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import butterknife.BindView;
import butterknife.OnItemSelected;

/**
 * Created on 8/26/16.
 */
public class EditSolutionView extends BaseView<ActionParameterPassData, BuyerEditSolutionListener> {

    @BindView(R2.id.view_refund)
    View viewRefund;
    @BindView(R2.id.refund_box_prompt)
    TextInputLayout refundPrompt;
    @BindView(R2.id.spinner_solution)
    Spinner solutionSpinner;
    @BindView(R2.id.refund_box)
    EditText refundBox;

    private SolutionSpinnerAdapter solutionAdapter;

    public EditSolutionView(Context context) {
        super(context);
    }

    public EditSolutionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(BuyerEditSolutionListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_res_center_solution_section;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull ActionParameterPassData data) {
        renderSolution(data);
    }

    private void renderSolution(final ActionParameterPassData data) {
        solutionAdapter = new SolutionSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, data.getFormData().getListSolution());
        solutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        solutionSpinner.setAdapter(solutionAdapter);
        setPreviousData(data);
    }

    private void resetRefundBox() {
        refundBox.setText(null);
        viewRefund.setVisibility(GONE);
        refundPrompt.setHint(null);
    }

    private void renderRefundBox(EditResCenterFormData.SolutionData solutionData) {
        if (solutionData != null) {
            if (solutionData.getRefundType() == 0) {
                refundPrompt.setHint(null);
                viewRefund.setVisibility(GONE);
            } else if (solutionData.getRefundType() == 1) {
                viewRefund.setVisibility(VISIBLE);
                refundPrompt.setHint(getContext().getString(R.string.refund_from_invoice).replace("XXX", solutionData.getMaxRefundIdr()));
            } else if (solutionData.getRefundType() == 2) {
                viewRefund.setVisibility(VISIBLE);
                refundPrompt.setHint(getContext().getString(R.string.refund_from_shipping_fee).replace("XXX", solutionData.getMaxRefundIdr()));
            } else if(solutionData.getRefundType() == 3) {
                viewRefund.setVisibility(VISIBLE);
                refundPrompt.setHint(getContext().getString(R.string.refund_from_product_price).replace("XXX", solutionData.getMaxRefundIdr()));
            } else {
                viewRefund.setVisibility(VISIBLE);
                refundPrompt.setHint(getContext().getString(R.string.refund_from_invoice).replace("XXX", solutionData.getMaxRefundIdr()));
            }
        }
    }

    private void setPreviousData(ActionParameterPassData data) {
        refundBox.setText(String.valueOf(data.getFormData().getForm().getResolutionLast().getLastRefundAmt()));
        String previousSolutionID = String.valueOf(data.getFormData().getForm().getResolutionLast().getLastSolution());
        for (int i = 0; i < solutionAdapter.getCount() - 1; i++) {
            if (solutionAdapter.getItem(i) != null) {
                if (solutionAdapter.getItem(i).getSolutionId().equals(previousSolutionID)) {
                    solutionSpinner.setSelection(i + 1, true);
                }
            }
        }
    }

    @OnItemSelected(R2.id.spinner_solution)
    public void onSolutionSelected() {
        resetRefundBox();
        if (solutionSpinner.getSelectedItemPosition() != 0) {
            EditResCenterFormData.SolutionData solutionData = ((EditResCenterFormData.SolutionData) solutionSpinner.getItemAtPosition(solutionSpinner.getSelectedItemPosition() - 1));
            renderRefundBox(solutionData);
        }
    }

    public EditResCenterFormData.SolutionData getSolutionChoosen() {
        return (EditResCenterFormData.SolutionData) solutionSpinner.getItemAtPosition(solutionSpinner.getSelectedItemPosition() - 1);
    }

    public EditText getRefundBox() {
        return refundBox;
    }
}
