package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.TroubleCategorySpinnerAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/24/16.
 */
public class EditCategorySectionView extends BaseView<EditResCenterFormData, BuyerEditResCenterListener> {

    private static final String TAG = EditCategorySectionView.class.getSimpleName();

    private Spinner categoryTroubleSpinner;

    private TroubleCategorySpinnerAdapter adapter;

    public EditCategorySectionView(Context context) {
        super(context);
    }

    public EditCategorySectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(BuyerEditResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_res_center_trouble_section;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull EditResCenterFormData data) {
        Log.d(TAG, "renderData");
        renderTroubleCategory(data);
        setPreviousData(data);
    }

    private void setPreviousData(EditResCenterFormData data) {
        String previousCategoryTroubleID = String.valueOf(data.getForm().getResolutionLast().getLastCategoryTroubleType());
        for (int i = 0; i < adapter.getCount() - 1; i++) {
            if (adapter.getItem(i) != null) {
                if (adapter.getItem(i).getCategoryTroubleId().equals(previousCategoryTroubleID)) {
                    categoryTroubleSpinner.setSelection(i + 1, true);
                }
            }
        }
    }

    public void onTroubleCategorySelected() {
        if (categoryTroubleSpinner.getSelectedItemPosition() != 0) {
            listener.getPresenter().setOnCategoryTroubleSelected(getTroubleCategoryChoosen());
        } else {
            listener.getPresenter().setOnCategoryTroubleNothingSelected();
        }
    }

    public void renderTroubleCategory(EditResCenterFormData data) {
        adapter = new TroubleCategorySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, data.getListTs());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryTroubleSpinner.setAdapter(adapter);
    }

    public EditResCenterFormData.TroubleCategoryData getTroubleCategoryChoosen() {
        return (EditResCenterFormData.TroubleCategoryData) categoryTroubleSpinner.getItemAtPosition(categoryTroubleSpinner.getSelectedItemPosition() - 1);
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        categoryTroubleSpinner = view.findViewById(R.id.spinner_trouble_category);
        categoryTroubleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onTroubleCategorySelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
