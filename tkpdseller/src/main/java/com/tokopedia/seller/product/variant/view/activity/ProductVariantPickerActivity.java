package com.tokopedia.seller.product.variant.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantSubmitOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.view.dialog.ProductVariantItemPickerAddDialogFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchFragment;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity<ProductVariantViewModel>
        implements ProductVariantPickerMultipleItem<ProductVariantViewModel>, ProductVariantItemPickerAddDialogFragment.Listener {

    private static final String DIALOG_ADD_VARIANT_TAG = "DIALOG_ADD_VARIANT_TAG";

    private ProductVariantByCatModel productVariantByCatModel;
    private List<ProductVariantValue> productVariantValueList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productVariantByCatModel = getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_CATEGORY);
        updateBottomSheetInfo();
    }

    @Override
    protected Fragment getInitialSearchListFragment() {
        return new ProductVariantPickerSearchFragment();
    }

    @Override
    protected Fragment getInitialCacheListFragment() {
        return new ProductVariantPickerCacheFragment();
    }

    @Override
    public void removeItemFromSearch(ProductVariantViewModel productVariantViewModel) {
        super.removeItemFromSearch(productVariantViewModel);
        updateBottomSheetInfo();
    }

    @Override
    public void removeAllItemFromSearch() {
        ((ProductVariantPickerCacheFragment) getCacheListFragment()).removeAllItem();
        updateBottomSheetInfo();
    }

    @Override
    public void addItemFromSearch(ProductVariantViewModel productVariantViewModel) {
        super.addItemFromSearch(productVariantViewModel);
        updateBottomSheetInfo();
    }

    @Override
    public void removeItemFromCache(ProductVariantViewModel productVariantViewModel) {
        super.removeItemFromCache(productVariantViewModel);
        updateBottomSheetInfo();
    }

    @Override
    public void onTextPickerSubmitted(String text) {
        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
        productVariantViewModel.setTitle(text);
        addItemFromSearch(productVariantViewModel);
        validateFooterAndInfoView();
    }

    @Override
    protected Intent getDefaultIntentResult() {
        VariantUnitSubmit variantUnitSubmit = new VariantUnitSubmit();
        variantUnitSubmit.setVariantId(productVariantByCatModel.getVariantId());
        variantUnitSubmit.setVariantUnitId(((ProductVariantPickerSearchFragment) getSearchListFragment()).getCurrentUnitId());
        variantUnitSubmit.setPosition(ProductVariantUtils.getVariantPositionByStatus(productVariantByCatModel.getStatus()));
        variantUnitSubmit.setVariantSubmitOptionList(((ProductVariantPickerCacheFragment) getCacheListFragment()).getVariantSubmitOptionList());
        Intent intent = new Intent();
        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT, variantUnitSubmit);
        return intent;
    }

    private void updateBottomSheetInfo() {
        String variantCategoryName = "";
        if (productVariantByCatModel != null && !TextUtils.isEmpty(productVariantByCatModel.getName())) {
            variantCategoryName = productVariantByCatModel.getName();
        }
        bottomSheetTitleTextView.setText(getString(R.string.product_variant_item_picker_selected_title,
                String.valueOf(getCacheListSize()), variantCategoryName));
        if (getCacheListSize() > 0) {
            showBottomSheetInfo(true);
        } else {
            showBottomSheetInfo(false);
        }
    }

    private int getSearchListSize() {
        Fragment fragment = getSearchListFragment();
        int selectedItemSize = 0;
        if ((fragment) != null) {
            selectedItemSize = ((ProductVariantPickerSearchFragment) fragment).getItemList().size();
        }
        return selectedItemSize;
    }

    private int getCacheListSize() {
        Fragment fragment = getCacheListFragment();
        int selectedItemSize = 0;
        if ((fragment) != null) {
            selectedItemSize = ((ProductVariantPickerCacheFragment) fragment).getItemList().size();
        }
        return selectedItemSize;
    }

    @Override
    public void validateFooterAndInfoView() {
        if (getSearchListSize() < 1 && getCacheListSize() < 1) {
            showFooterAndInfo(false);
        } else {
            showFooterAndInfo(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_variant_item_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            showAddDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showAddDialog() {
        ProductVariantItemPickerAddDialogFragment dialogFragment = new ProductVariantItemPickerAddDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProductVariantItemPickerAddDialogFragment.EXTRA_VARIANT_TITLE, productVariantByCatModel.getName());
        List<VariantSubmitOption> variantSubmitOptionList = ((ProductVariantPickerCacheFragment) getCacheListFragment()).getVariantSubmitOptionList();
        ArrayList<String> titleList = new ArrayList<>();
        titleList.addAll(ProductVariantUtils.getTitleList(variantSubmitOptionList, productVariantByCatModel.getUnitList()));
        bundle.putStringArrayList(ProductVariantItemPickerAddDialogFragment.EXTRA_VARIANT_RESERVED_TITLE_LIST, titleList);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), DIALOG_ADD_VARIANT_TAG);
    }
}