package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerEtalasePickerViewComponent;
import com.tokopedia.seller.product.di.component.EtalasePickerComponent;
import com.tokopedia.seller.product.di.component.EtalasePickerViewComponent;
import com.tokopedia.seller.product.di.module.EtalasePickerViewModule;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerFragment extends BaseDaggerFragment implements EtalasePickerView{
    public static final String TAG = "EtalasePicker";

    public static EtalasePickerFragment createInstance() {
        return new EtalasePickerFragment();
    }

    @Override
    protected void initInjector() {
        EtalasePickerViewComponent component = DaggerEtalasePickerViewComponent
                .builder()
                .etalasePickerComponent(getComponent(EtalasePickerComponent.class))
                .etalasePickerViewModule(new EtalasePickerViewModule())
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.etalase_picker_fragment_layout, container, false);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
