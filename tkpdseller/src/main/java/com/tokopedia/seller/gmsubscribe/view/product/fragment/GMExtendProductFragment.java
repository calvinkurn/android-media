package com.tokopedia.seller.gmsubscribe.view.product.fragment;

import android.app.Fragment;

import com.tokopedia.seller.R;

/**
 * Created by sebastianuskh on 1/26/17.
 */

public class GMExtendProductFragment extends GMProductFragment {

    public static Fragment createFragment(String buttonString, int defaultSelected, int returnType) {
        return createFragment(new GMExtendProductFragment(), buttonString, defaultSelected, returnType);
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
