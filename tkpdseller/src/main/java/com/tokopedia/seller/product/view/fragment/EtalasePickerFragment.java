package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerFragment extends BaseDaggerFragment implements EtalasePickerView{
    public static final String TAG = "EtalasePicker";

    public static EtalasePickerFragment createInstance() {
        return new EtalasePickerFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
