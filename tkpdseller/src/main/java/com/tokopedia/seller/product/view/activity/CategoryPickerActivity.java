package com.tokopedia.seller.product.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.CategoryPickerComponent;
import com.tokopedia.seller.product.di.component.DaggerCategoryPickerComponent;
import com.tokopedia.seller.product.di.module.CategoryPickerModule;
import com.tokopedia.seller.product.view.fragment.CategoryPickerFragment;
import com.tokopedia.seller.product.view.fragment.CategoryPickerFragmentListener;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import org.parceler.Parcels;

import java.util.List;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerActivity
        extends TActivity
        implements CategoryPickerFragmentListener, HasComponent<CategoryPickerComponent>{

    public static final String CATEGORY_ID_INIT_SELECTED = "CATEGORY_ID_INIT_SELECTED";
    public static final String CATEGORY_RESULT_LEVEL = "CATEGORY_RESULT_LEVEL";
    private FragmentManager fragmentManager;
    private CategoryPickerComponent component;

    public static void start(Activity activity, int requestCode, int depId){
        Intent intent = createIntent(activity, depId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(android.app.Fragment fragment, Context context, int requestCode, int depId){
        Intent intent = createIntent(context, depId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(android.support.v4.app.Fragment fragment, Context context, int requestCode,
                             int depId){
        Intent intent = createIntent(context, depId);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context,
                                       int departmentId){
        Intent intent = new Intent(context, CategoryPickerActivity.class);
        if (departmentId > 0) {
            intent.putExtra(CATEGORY_ID_INIT_SELECTED, departmentId);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        fragmentManager = getSupportFragmentManager();
        initInjection();
        int currentSelected =
                getIntent()
                        .getIntExtra(
                                CATEGORY_ID_INIT_SELECTED,
                                CategoryPickerFragment.INIT_UNSELECTED
                        );
        inflateCategoryFragment(currentSelected);

    }

    private void initInjection() {
        component = DaggerCategoryPickerComponent
                .builder()
                .appComponent(getApplicationComponent())
                .categoryPickerModule(new CategoryPickerModule())
                .build();

    }

    private void inflateCategoryFragment(int currentSelected) {
        Fragment fragment = fragmentManager.findFragmentByTag(CategoryPickerFragment.TAG);
        if (fragment == null) {
            fragment = CategoryPickerFragment.createInstance(currentSelected);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, CategoryPickerFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public CategoryPickerComponent getComponent() {
        return component;
    }

    @Override
    public void selectSetCategory(List<CategoryViewModel> listCategory) {
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_RESULT_LEVEL, Parcels.wrap(listCategory));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
