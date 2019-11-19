package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.SolutionSpinnerAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.AppealResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;


/**
 * Created on 8/31/16.
 */
public class AppealSolutionView extends BaseView<AppealResCenterFormData, AppealResCenterListener> {

    private View viewRefund;
    private TextInputLayout refundPrompt;
    private Spinner solutionSpinner;
    private EditText refundBox;
    private View viewMessage;
    private EditText messageBox;

    public AppealSolutionView(Context context) {
        super(context);
    }

    public AppealSolutionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(AppealResCenterListener listener) {

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
    public void renderData(@NonNull AppealResCenterFormData data) {
        renderSolution(data);
    }

    private void renderSolution(final AppealResCenterFormData data) {
        SolutionSpinnerAdapter solutionAdapter = new SolutionSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, data.getForm().getResolutionSolutionList());
        solutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        solutionSpinner.setAdapter(solutionAdapter);
    }

    private void resetRefundBox() {
        refundBox.setText(null);
        viewRefund.setVisibility(GONE);
        refundPrompt.setHint(null);
    }

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

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        settingUpVariables(view);
        solutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSolutionSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void settingUpVariables(View view) {
        viewRefund = view.findViewById(R.id.view_refund);
        refundPrompt = view.findViewById(R.id.refund_box_prompt);
        solutionSpinner = view.findViewById(R.id.spinner_solution);
        refundBox = view.findViewById(R.id.refund_box);
        viewMessage = view.findViewById(R.id.view_message);
        messageBox = view.findViewById(R.id.message_box);
    }


}
