package com.tokopedia.gm.subscribe.view.fragment;

import com.tokopedia.gm.R;

/**
 * Created by sebastianuskh on 1/26/17.
 */

public class GmCurrentProductFragment extends GmProductFragment {

    public static final int EDIT_TAG = 100;
    public static final int INIT_TAG = 200;

    public static GmProductFragment createFragment(String buttonString, int defaultSelected, int returnType) {
        return createFragment(new GmCurrentProductFragment(), buttonString, defaultSelected, returnType);
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
