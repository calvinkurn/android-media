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
import com.tokopedia.inbox.rescenter.edit.listener.SellerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import butterknife.BindView;
import butterknife.OnItemSelected;

/**
 * Created on 8/29/16.
 */
public class EditSolutionSellerView extends BaseView<EditResCenterFormData, SellerEditResCenterListener> {

    @BindView(R2.id.view_refund)
    View viewRefund;
    @BindView(R2.id.refund_box_prompt)
    TextInputLayout refundPrompt;
    @BindView(R2.id.spinner_solution)
    Spinner solutionSpinner;
    @BindView(R2.id.refund_box)
    EditText refundBox;
    @BindView(R2.id.view_message)
    View viewMessage;
    @BindView(R2.id.message_box)
    EditText messageBox;

    public EditSolutionSellerView(Context context) {
        super(context);
    }

    public EditSolutionSellerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SellerEditResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_edit_rescenter_solution_seller;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull EditResCenterFormData data) {
        renderSolution(data);
    }

    private void renderSolution(final EditResCenterFormData data) {
        SolutionSpinnerAdapter  solutionAdapter = new SolutionSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, data.getForm().getResolutionSolutionList());
        solutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        solutionSpinner.setAdapter(solutionAdapter);
    }

    private void resetRefundBox() {
        refundBox.setText(null);
        viewRefund.setVisibility(GONE);
        refundPrompt.setHint(null);
    }

    @OnItemSelected(R2.id.spinner_solution)
    public void onSolutionSelected() {
        resetRefundBox();
        if (solutionSpinner.getSelectedItemPosition() != 0) {
            EditResCenterFormData.SolutionData solutionData = ((EditResCenterFormData.SolutionData) solutionSpinner.getItemAtPosition(solutionSpinner.getSelectedItemPosition() - 1));
            renderRefundBox(solutionData);
        }
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

    public EditResCenterFormData.SolutionData getSolutionChoosen() {
        return (EditResCenterFormData.SolutionData) solutionSpinner.getItemAtPosition(solutionSpinner.getSelectedItemPosition() - 1);
    }

    public EditText getRefundBox() {
        return refundBox;
    }

    public EditText getMessageBox() {
        return messageBox;
    }
}
