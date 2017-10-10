package com.tokopedia.seller.product.category.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.category.view.fragment.CategoryPickerFragment;
import com.tokopedia.seller.product.edit.view.listener.CategoryPickerFragmentListener;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import org.parceler.Parcels;

import java.util.List;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerActivity extends BaseSimpleActivity implements
        CategoryPickerFragmentListener, HasComponent<ProductComponent> {

    public static final String CATEGORY_ID_INIT_SELECTED = "CATEGORY_ID_INIT_SELECTED";
    public static final String CATEGORY_RESULT_LEVEL = "CATEGORY_RESULT_LEVEL";
    public static final String CATEGORY_RESULT_ID = "CATEGORY_RESULT_ID";
    public static final String CATEGORY_RESULT_NAME = "CATEGORY_RESULT_NAME";

    public static void start(Activity activity, int requestCode, long depId) {
        Intent intent = createIntent(activity, depId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(android.app.Fragment fragment, Context context, int requestCode, long depId) {
        Intent intent = createIntent(context, depId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(android.support.v4.app.Fragment fragment, Context context, int requestCode, long depId) {
        Intent intent = createIntent(context, depId);
        fragment.startActivityForResult(intent, requestCode);
    }

    protected static Intent createIntent(Context context, long departmentId) {
        Intent intent = new Intent(context, CategoryPickerActivity.class);
        if (departmentId > 0) {
            intent.putExtra(CATEGORY_ID_INIT_SELECTED, departmentId);
        }
        return intent;
    }

    @Override
    public void selectSetCategory(List<CategoryViewModel> listCategory) {
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_RESULT_LEVEL, Parcels.wrap(listCategory));
        CategoryViewModel chosenCategory = listCategory.get(listCategory.size() - 1);
        intent.putExtra(CATEGORY_RESULT_ID, chosenCategory.getId());
        intent.putExtra(CATEGORY_RESULT_NAME, chosenCategory.getName());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected Fragment getNewFragment() {
        long selectedCategoryId = getIntent().getLongExtra(CATEGORY_ID_INIT_SELECTED, CategoryPickerFragment.INIT_UNSELECTED);
        return CategoryPickerFragment.createInstance(selectedCategoryId);
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}