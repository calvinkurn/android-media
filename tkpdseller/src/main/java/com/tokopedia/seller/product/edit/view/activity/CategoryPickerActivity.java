package com.tokopedia.seller.product.edit.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.view.fragment.CategoryPickerFragment;
import com.tokopedia.seller.product.edit.view.listener.CategoryPickerFragmentListener;
import com.tokopedia.seller.product.edit.view.model.CategoryViewModel;

import org.parceler.Parcels;

import java.util.List;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerActivity
        extends BaseActivity
        implements CategoryPickerFragmentListener, HasComponent<ProductComponent>{

    public static final String CATEGORY_ID_INIT_SELECTED = "CATEGORY_ID_INIT_SELECTED";
    public static final String CATEGORY_RESULT_LEVEL = "CATEGORY_RESULT_LEVEL";
    public static final String CATEGORY_RESULT_ID = "CATEGORY_RESULT_ID";
    private FragmentManager fragmentManager;

    public static void start(Activity activity, int requestCode, long depId){
        Intent intent = createIntent(activity, depId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(android.app.Fragment fragment, Context context, int requestCode, long depId){
        Intent intent = createIntent(context, depId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(android.support.v4.app.Fragment fragment, Context context, int requestCode, long depId){
        Intent intent = createIntent(context, depId);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context, long departmentId){
        Intent intent = new Intent(context, CategoryPickerActivity.class);
        if (departmentId > 0) {
            intent.putExtra(CATEGORY_ID_INIT_SELECTED, departmentId);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fragmentManager = getSupportFragmentManager();
        long currentSelected = getIntent().getLongExtra(CATEGORY_ID_INIT_SELECTED, CategoryPickerFragment.INIT_UNSELECTED);
        inflateCategoryFragment(currentSelected);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    private void inflateCategoryFragment(long currentSelected) {
        Fragment fragment = fragmentManager.findFragmentByTag(CategoryPickerFragment.TAG);
        if (fragment == null) {
            fragment = CategoryPickerFragment.createInstance(currentSelected);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, CategoryPickerFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent(getActivityModule());
    }

    @Override
    public void selectSetCategory(List<CategoryViewModel> listCategory) {
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_RESULT_LEVEL, Parcels.wrap(listCategory));
        intent.putExtra(CATEGORY_RESULT_ID, listCategory.get(listCategory.size()-1).getId());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
