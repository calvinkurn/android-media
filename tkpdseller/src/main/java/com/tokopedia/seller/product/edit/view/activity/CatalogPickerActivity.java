package com.tokopedia.seller.product.edit.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.di.component.CatalogPickerComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerCatalogPickerComponent;
import com.tokopedia.seller.product.edit.di.module.CatalogPickerModule;
import com.tokopedia.seller.product.edit.view.fragment.CatalogPickerFragment;

/**
 * @author hendry on 4/3/17.
 */

public class CatalogPickerActivity extends BaseActivity implements HasComponent<CatalogPickerComponent>{

    private CatalogPickerComponent component;

    public static final String KEYWORD = "q";
    public static final String DEP_ID = "dep_id";
    public static final String CATALOG_ID = "cat_id";

    public static final String CATALOG_NAME = "cat_nm";

    private String keyword;
    private long departmentId;
    private long selectedCatalogId;

    public static void start(Activity activity, int requestCode, String keyword, long depId, long selectedCatalogId){
        Intent intent = createIntent(activity, keyword, depId, selectedCatalogId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(android.app.Fragment fragment, Context context, int requestCode, String keyword, long depId, long selectedCatalogId){
        Intent intent = createIntent(context, keyword, depId, selectedCatalogId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(android.support.v4.app.Fragment fragment, Context context, int requestCode, String keyword, long depId, long selectedCatalogId){
        Intent intent = createIntent(context, keyword, depId, selectedCatalogId);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context, @Nullable String keyword, long departmentId, long selectedCatalogId){
        Intent intent = new Intent(context, CatalogPickerActivity.class);
        intent.putExtra(KEYWORD, keyword);
        intent.putExtra(DEP_ID, departmentId);
        intent.putExtra(CATALOG_ID, selectedCatalogId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        keyword = extras.getString(KEYWORD);
        departmentId = extras.getLong(DEP_ID);
        selectedCatalogId = extras.getLong(CATALOG_ID);

        setContentView(R.layout.activity_simple_fragment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initInjection();
        inflateCatalogPickerFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
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
            fragment = CatalogPickerFragment.newInstance(keyword, departmentId, selectedCatalogId);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, CatalogPickerFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public CatalogPickerComponent getComponent() {
        return component;
    }

}
