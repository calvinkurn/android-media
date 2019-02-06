package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.TroubleCategorySpinnerAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import butterknife.BindView;
import butterknife.OnItemSelected;

/**
 * Created on 8/24/16.
 */
public class EditCategorySectionView extends BaseView<EditResCenterFormData, BuyerEditResCenterListener> {

    private static final String TAG = EditCategorySectionView.class.getSimpleName();

    @BindView(R2.id.spinner_trouble_category)
    Spinner categoryTroubleSpinner;

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

    @OnItemSelected(R2.id.spinner_trouble_category)
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

}
