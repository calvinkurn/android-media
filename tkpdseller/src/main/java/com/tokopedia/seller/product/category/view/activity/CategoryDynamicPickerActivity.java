package com.tokopedia.seller.product.category.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.common.category.view.model.CategoryViewModel;
import com.tokopedia.seller.product.category.view.fragment.CategoryPickerDynamicFragment;
import com.tokopedia.seller.product.category.view.fragment.CategoryPickerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/9/17.
 */

public class CategoryDynamicPickerActivity extends CategoryPickerActivity {

    public static final String ADDITIONAL_OPTION = "additional_option";

    public static Intent createIntent(Context context, long depId, ArrayList<CategoryViewModel> categoryViewModels) {
        Intent intent = new Intent(context, CategoryDynamicPickerActivity.class);
        if (depId > 0) {
            intent.putExtra(CATEGORY_ID_INIT_SELECTED, depId);
        }
        intent.putParcelableArrayListExtra(ADDITIONAL_OPTION, categoryViewModels);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        long selectedCategoryId = getIntent().getLongExtra(CATEGORY_ID_INIT_SELECTED, CategoryPickerFragment.INIT_UNSELECTED);
        ArrayList<CategoryViewModel> categoryViewModels = getIntent().getParcelableArrayListExtra(ADDITIONAL_OPTION);
        return CategoryPickerDynamicFragment.createInstance(selectedCategoryId, categoryViewModels);
    }
}
