package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.product.di.component.CategoryPickerComponent;

/**
 * Created by sebastianuskh on 4/3/17.
 */

public class CategoryPickerFragment extends BaseDaggerFragment{
    public static final String TAG = "CategoryPickerFragment";
    private CategoryPickerComponent component;

    public static CategoryPickerFragment createInstance() {
        return new CategoryPickerFragment();
    }

    @Override
    protected void initInjector() {
        component
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
