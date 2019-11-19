package com.tokopedia.inbox.rescenter.create.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.create.customadapter.TroubleSpinnerAdapter;
import com.tokopedia.inbox.rescenter.create.listener.ChooseTroubleListener;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

import java.util.ArrayList;


/**
 * Created on 8/3/16.
 */
public class ChooseTroubleSectionCreateResCenterView extends BaseView<CreateResCenterFormData, ChooseTroubleListener> {

    public Spinner troubleSpinner;
    public View descBoxView;
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

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        troubleSpinner = view.findViewById(R.id.spinner_trouble);
        descBoxView = view.findViewById(R.id.view_desc);
        descEditText = view.findViewById(R.id.box_desc);
    }
}
