package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.CategoryPickerFragment;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerActivity extends TActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        fragmentManager = getSupportFragmentManager();
        inflateCategoryFragment();
    }

    private void inflateCategoryFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(CategoryPickerFragment.TAG);
        if (fragment == null) {
            fragment = CategoryPickerFragment.createInstance();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, CategoryPickerFragment.TAG);
        fragmentTransaction.commit();
    }
}
