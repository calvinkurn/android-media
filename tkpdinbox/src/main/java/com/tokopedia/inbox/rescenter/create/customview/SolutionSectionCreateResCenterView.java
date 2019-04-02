package com.tokopedia.inbox.rescenter.create.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.inbox.rescenter.create.customadapter.SolutionSpinnerAdapter;
import com.tokopedia.inbox.rescenter.create.listener.ChooseSolutionListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

import butterknife.BindView;
import butterknife.OnItemSelected;

/**
 * Created on 6/17/16.
 */
public class SolutionSectionCreateResCenterView extends BaseView<ActionParameterPassData, ChooseSolutionListener> {

    @BindView(R2.id.view_refund)
    View viewRefund;
    @BindView(R2.id.refund_box_prompt)
    TextInputLayout refundPrompt;
    @BindView(R2.id.spinner_solution)
    Spinner solutionSpinner;
    @BindView(R2.id.refund_box)
    EditText refundBox;

    public SolutionSectionCreateResCenterView(Context context) {
        super(context);
    }

    public SolutionSectionCreateResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        viewRefund.setVisibility(GONE);
    }

    @Override
    public void setListener(ChooseSolutionListener chooseSolutionPresenter) {
        this.listener = chooseSolutionPresenter;
    }

    @Override
    public void renderData(@NonNull ActionParameterPassData data) {
        renderSolution(data);
    }

    private void renderRefundBox(CreateResCenterFormData.SolutionData solutionData) {
        if (!(solutionData == null || solutionData.getRefundType() == null)) {
            switch (solutionData.getRefundType()) {
                case 0:
                    refundPrompt.setHint(null);
                    viewRefund.setVisibility(GONE);
                    break;
                case 1:
                    viewRefund.setVisibility(VISIBLE);
                    refundPrompt.setHint(getContext().getString(R.string.refund_from_invoice).replace("XXX", solutionData.getMaxRefundIdr()));
                    break;
                case 2:
                    viewRefund.setVisibility(VISIBLE);
                    refundPrompt.setHint(getContext().getString(R.string.refund_from_shipping_fee).replace("XXX", solutionData.getMaxRefundIdr()));
                    break;
                case 3:
                    viewRefund.setVisibility(VISIBLE);
                    refundPrompt.setHint(getContext().getString(R.string.refund_from_product_price).replace("XXX", solutionData.getMaxRefundIdr()));
                    break;
                default:
                    viewRefund.setVisibility(VISIBLE);
                    refundPrompt.setHint(getContext().getString(R.string.refund_from_invoice).replace("XXX", solutionData.getMaxRefundIdr()));
                    break;
            }
        }
    }

    private void resetRefundBox() {
        refundBox.setText(null);
        viewRefund.setVisibility(GONE);
        refundPrompt.setHint(null);
    }

    private void renderSolution(final ActionParameterPassData data) {
        SolutionSpinnerAdapter solutionAdapter = new SolutionSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, data.getFormData().getListSolution());
        solutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        solutionSpinner.setAdapter(solutionAdapter);
    }

    public EditText getRefundBox() {
        return refundBox;
    }

    public CreateResCenterFormData.SolutionData getSolutionData() {
        if (solutionSpinner.getSelectedItemPosition() == 0) {
            return null;
        } else {
            return (CreateResCenterFormData.SolutionData) solutionSpinner.getItemAtPosition(solutionSpinner.getSelectedItemPosition() - 1);
        }
    }

    @OnItemSelected(R2.id.spinner_solution)
    public void onSolutionSelected() {
        resetRefundBox();
        if (solutionSpinner.getSelectedItemPosition() != 0) {
            CreateResCenterFormData.SolutionData solutionData = ((CreateResCenterFormData.SolutionData) solutionSpinner.getItemAtPosition(solutionSpinner.getSelectedItemPosition() - 1));
            renderRefundBox(solutionData);
        }
    }
}
