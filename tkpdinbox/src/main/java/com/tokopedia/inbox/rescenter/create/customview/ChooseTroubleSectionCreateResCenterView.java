package com.tokopedia.inbox.rescenter.create.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.inbox.rescenter.create.customadapter.TroubleSpinnerAdapter;
import com.tokopedia.inbox.rescenter.create.listener.ChooseTroubleListener;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created on 8/3/16.
 */
public class ChooseTroubleSectionCreateResCenterView extends BaseView<CreateResCenterFormData, ChooseTroubleListener> {

    @BindView(R2.id.spinner_trouble)
    public Spinner troubleSpinner;
    @BindView(R2.id.view_desc)
    public View descBoxView;
    @BindView(R2.id.box_desc)
    public EditText descEditText;


    public ChooseTroubleSectionCreateResCenterView(Context context) {
        super(context);
    }

    public ChooseTroubleSectionCreateResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ChooseTroubleListener chooseTroubleListener) {
        this.listener = chooseTroubleListener;
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
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull CreateResCenterFormData createResCenterFormData) {

    }

    public CreateResCenterFormData.TroubleData getTroubleChoosen() {
        return (CreateResCenterFormData.TroubleData) troubleSpinner.getItemAtPosition(troubleSpinner.getSelectedItemPosition() - 1);
    }

    public String getDescription() {
        return String.valueOf(descEditText.getText());
    }

    public void renderSpinner(CreateResCenterFormData.TroubleCategoryData troubleCategoryChoosen) {
        TroubleSpinnerAdapter troubleAdapter = new TroubleSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, troubleCategoryChoosen.getTroubleList());
        troubleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        troubleSpinner.setAdapter(troubleAdapter);
    }

    public void resetSpinner() {
        TroubleSpinnerAdapter troubleAdapter = new TroubleSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, new ArrayList<CreateResCenterFormData.TroubleData>());
        troubleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        troubleSpinner.setAdapter(troubleAdapter);
    }
}
