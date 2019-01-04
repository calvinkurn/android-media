package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.TroubleSpinnerAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created on 8/24/16.
 */
public class EditTroubleSectionView extends BaseView<EditResCenterFormData, BuyerEditResCenterListener> {

    @BindView(R2.id.spinner_trouble)
    public Spinner troubleSpinner;
    @BindView(R2.id.box_desc)
    public EditText descEditText;

    private EditResCenterFormData data;
    private TroubleSpinnerAdapter adapter;

    public EditTroubleSectionView(Context context) {
        super(context);
    }

    public EditTroubleSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(BuyerEditResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_rescenter_choose_trouble;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull EditResCenterFormData data) {
        this.data = data;
    }

    public EditResCenterFormData.TroubleData getTroubleChoosen() {
        return (EditResCenterFormData.TroubleData) troubleSpinner.getItemAtPosition(troubleSpinner.getSelectedItemPosition() - 1);
    }

    public String getDescription() {
        return String.valueOf(descEditText.getText());
    }

    public void renderSpinner(EditResCenterFormData.TroubleCategoryData troubleCategoryChoosen) {
        adapter = new TroubleSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, troubleCategoryChoosen.getTroubleList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        troubleSpinner.setAdapter(adapter);
    }

    public void resetSpinner() {
        TroubleSpinnerAdapter troubleAdapter = new TroubleSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, new ArrayList<EditResCenterFormData.TroubleData>());
        troubleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        troubleSpinner.setAdapter(troubleAdapter);
    }

    public void setPreviousData() {
        String troubleID = String.valueOf(data.getForm().getResolutionLast().getLastTroubleType());
        for (int i = 0; i < adapter.getCount() - 1; i++) {
            if (adapter.getItem(i) != null) {
                if (adapter.getItem(i).getTroubleId().equals(troubleID)) {
                    troubleSpinner.setSelection(i + 1, true);
                }
            }
        }
    }
}
