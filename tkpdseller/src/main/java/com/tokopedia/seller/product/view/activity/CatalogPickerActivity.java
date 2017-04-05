package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.CatalogPickerComponent;
import com.tokopedia.seller.product.di.component.CategoryPickerComponent;
import com.tokopedia.seller.product.di.component.DaggerCatalogPickerComponent;
import com.tokopedia.seller.product.di.component.DaggerCategoryPickerComponent;
import com.tokopedia.seller.product.di.module.CatalogPickerModule;
import com.tokopedia.seller.product.di.module.CategoryPickerModule;
import com.tokopedia.seller.product.view.fragment.CatalogPickerFragment;
import com.tokopedia.seller.product.view.fragment.CategoryPickerFragment;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CatalogPickerActivity extends TActivity implements HasComponent<CatalogPickerComponent>{

    private CatalogPickerComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initInjection();
        inflateCatalogPickerFragment();
    }

    private void initInjection() {
        component = DaggerCatalogPickerComponent
                .builder()
                .appComponent(getApplicationComponent())
                .catalogPickerModule(new CatalogPickerModule())
                .build();
    }

    private void inflateCatalogPickerFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(CatalogPickerFragment.TAG);
        if (fragment == null) {
            fragment = CatalogPickerFragment.createInstance();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, CategoryPickerFragment.TAG);
            fragmentTransaction.commit();
        }

    }

    @Override
    public CatalogPickerComponent getComponent() {
        return component;
    }
}
