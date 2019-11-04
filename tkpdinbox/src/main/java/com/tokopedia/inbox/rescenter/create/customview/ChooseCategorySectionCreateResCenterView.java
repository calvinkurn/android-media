package com.tokopedia.inbox.rescenter.create.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.create.customadapter.TroubleCategorySpinnerAdapter;
import com.tokopedia.inbox.rescenter.create.listener.ChooseTroubleListener;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

/**
 * Created on 6/16/16.
 */
public class ChooseCategorySectionCreateResCenterView extends BaseView<CreateResCenterFormData, ChooseTroubleListener> {

    public static final String TAG = ChooseCategorySectionCreateResCenterView.class.getSimpleName();

    private Spinner categoryTroubleSpinner;

    private ChooseTroubleListener listener;

    public ChooseCategorySectionCreateResCenterView(Context context) {
        super(context);
    }

    public ChooseCategorySectionCreateResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ChooseTroubleListener listener) {
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
    public void renderData(@NonNull final CreateResCenterFormData data) {
        Log.d(TAG, "renderData");
        renderTroubleCategory(data);
        categoryTroubleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    if (getTroubleCategoryChoosen().getProductRelated() == 1) {
                        listener.showChooseProduct(true);
                        listener.showChooseTrouble(false);
                    } else {
                        listener.showChooseTrouble(true);
                        listener.showChooseProduct(false);
                    }
                } else {
                    listener.showChooseProduct(false);
                    listener.showChooseTrouble(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void renderTroubleCategory(CreateResCenterFormData data) {
        TroubleCategorySpinnerAdapter adapter = new TroubleCategorySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, data.getListTs());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryTroubleSpinner.setAdapter(adapter);
    }

    public CreateResCenterFormData.TroubleCategoryData getTroubleCategoryChoosen() {
        return (CreateResCenterFormData.TroubleCategoryData) categoryTroubleSpinner.getItemAtPosition(categoryTroubleSpinner.getSelectedItemPosition() - 1);
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        categoryTroubleSpinner = view.findViewById(R.id.spinner_trouble_category);
    }
}
