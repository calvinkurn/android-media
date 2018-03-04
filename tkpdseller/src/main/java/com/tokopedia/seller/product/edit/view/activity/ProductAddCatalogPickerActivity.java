package com.tokopedia.seller.product.edit.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.di.component.CatalogPickerComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerCatalogPickerComponent;
import com.tokopedia.seller.product.edit.di.module.CatalogPickerModule;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddCatalogPickerFragment;

/**
 * @author hendry on 4/3/17.
 */

public class ProductAddCatalogPickerActivity extends BaseSimpleActivity implements HasComponent<CatalogPickerComponent> {

    public static final String KEYWORD = "q";
    public static final String DEP_ID = "dep_id";
    public static final String CATALOG_ID = "cat_id";

    public static final String CATALOG_NAME = "cat_nm";

    public static void start(Activity activity, int requestCode, String keyword, long depId, long selectedCatalogId) {
        Intent intent = createIntent(activity, keyword, depId, selectedCatalogId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(android.app.Fragment fragment, Context context, int requestCode, String keyword, long depId, long selectedCatalogId) {
        Intent intent = createIntent(context, keyword, depId, selectedCatalogId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(android.support.v4.app.Fragment fragment, Context context, int requestCode, String keyword, long depId, long selectedCatalogId) {
        Intent intent = createIntent(context, keyword, depId, selectedCatalogId);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context, @Nullable String keyword, long departmentId, long selectedCatalogId) {
        Intent intent = new Intent(context, ProductAddCatalogPickerActivity.class);
        intent.putExtra(KEYWORD, keyword);
        intent.putExtra(DEP_ID, departmentId);
        intent.putExtra(CATALOG_ID, selectedCatalogId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle extras = getIntent().getExtras();
        String keyword = "";
        long departmentId = -1;
        long selectedCatalogId = -1;
        if (extras != null) {
            keyword = extras.getString(KEYWORD);
            departmentId = extras.getLong(DEP_ID);
            selectedCatalogId = extras.getLong(CATALOG_ID);
        }
        return ProductAddCatalogPickerFragment.newInstance(keyword, departmentId, selectedCatalogId);
    }

    @Override
    public CatalogPickerComponent getComponent() {
        return DaggerCatalogPickerComponent
                .builder()
                .productComponent(((SellerModuleRouter) getApplication()).getProductComponent())
                .catalogPickerModule(new CatalogPickerModule())
                .build();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}