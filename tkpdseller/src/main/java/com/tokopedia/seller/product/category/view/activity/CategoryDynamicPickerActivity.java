package com.tokopedia.seller.product.category.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.product.category.view.fragment.CategoryPickerDynamicFragment;
import com.tokopedia.seller.product.category.view.fragment.CategoryPickerFragment;
import com.tokopedia.seller.product.manage.view.model.ProductManageCategoryViewModel;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 10/9/17.
 */

public class CategoryDynamicPickerActivity extends CategoryPickerActivity {

    public static final String ADDITIONAL_OPTION = "additional_option";

    public static Intent createIntent(Context context, long depId, ArrayList<ProductManageCategoryViewModel> categoryViewModels) {
        Intent intent = new Intent(context, CategoryDynamicPickerActivity.class);
        intent.putExtra(CATEGORY_ID_INIT_SELECTED, depId);
        intent.putParcelableArrayListExtra(ADDITIONAL_OPTION, categoryViewModels);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        long selectedCategoryId = getIntent().getLongExtra(CATEGORY_ID_INIT_SELECTED, CategoryPickerFragment.INIT_UNSELECTED);
        ArrayList<ProductManageCategoryViewModel> categoryViewModels = getIntent().getParcelableArrayListExtra(ADDITIONAL_OPTION);
        return CategoryPickerDynamicFragment.createInstance(selectedCategoryId, categoryViewModels);
    }
}
