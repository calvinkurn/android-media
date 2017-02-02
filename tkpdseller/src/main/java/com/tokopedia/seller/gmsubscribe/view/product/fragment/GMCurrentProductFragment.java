package com.tokopedia.seller.gmsubscribe.view.product.fragment;

import com.tokopedia.seller.R;

/**
 * Created by sebastianuskh on 1/26/17.
 */

public class GMCurrentProductFragment extends GMProductFragment {

    public static final int EDIT_TAG = 100;
    public static final int INIT_TAG = 200;

    public static GMProductFragment createFragment(String buttonString, int defaultSelected, int returnType) {
        return createFragment(new GMCurrentProductFragment(), buttonString, defaultSelected, returnType);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.gmsubscribe_current_product_selector);
    }

    @Override
    protected void getPackage() {
        presenter.getCurrentPackageSelection();
    }
}
