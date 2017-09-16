package com.tokopedia.gm.subscribe.view.fragment;

import android.app.Fragment;

import com.tokopedia.gm.R;

/**
 * Created by sebastianuskh on 1/26/17.
 */

public class GmExtendProductFragment extends GmProductFragment {

    public static Fragment createFragment(String buttonString, int defaultSelected, int returnType) {
        return createFragment(new GmExtendProductFragment(), buttonString, defaultSelected, returnType);
    }

    @Override
    protected String getTitle() {
        return getString(R.string.gmsubscribe_extend_product_selector);
    }

    @Override
    protected void getPackage() {
        presenter.getExtendPackageSelection();
    }
}
