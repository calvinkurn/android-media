package com.tokopedia.seller.product.variant.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.util.ProductVariantViewConverter;
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
    private boolean hasAnyUpdate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productVariantByCatModel = getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_CATEGORY);
        toolbar.setTitle(getString(R.string.product_variant_option_x, productVariantByCatModel.getName()) );
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
        hasAnyUpdate = true;
    }

    @Override
    public void removeAllItemFromSearch() {
        ((ProductVariantPickerCacheFragment) getCacheListFragment()).removeAllItem();
        updateBottomSheetInfo();
    }

    @Override
    public void addItemFromSearch(ProductVariantViewModel productVariantViewModel, boolean isManual) {
        super.addItemFromSearch(productVariantViewModel, isManual);
        updateBottomSheetInfo();
        if (isManual) {
            hasAnyUpdate = true;
        }
    }

    @Override
    public void removeItemFromCache(ProductVariantViewModel productVariantViewModel) {
        super.removeItemFromCache(productVariantViewModel);
        updateBottomSheetInfo();
        hasAnyUpdate = true;
    }

    @Override
    public void onTextPickerSubmitted(String text) {
        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
        productVariantViewModel.setTitle(text);
        addItemFromSearch(productVariantViewModel, true);
        validateFooterAndInfoView();
    }

    @Override
    protected Intent getDefaultIntentResult() {
        ProductVariantUnitSubmit productVariantUnitSubmit = new ProductVariantUnitSubmit();
        productVariantUnitSubmit.setVariantId(productVariantByCatModel.getVariantId());
        productVariantUnitSubmit.setVariantUnitId(((ProductVariantPickerSearchFragment) getSearchListFragment()).getSelectedUnitId());
        productVariantUnitSubmit.setPosition(ProductVariantViewConverter.getVariantPositionByStatus(productVariantByCatModel.getStatus()));
        productVariantUnitSubmit.setProductVariantOptionSubmitList(((ProductVariantPickerCacheFragment) getCacheListFragment()).getVariantSubmitOptionList());
        Intent intent = new Intent();
        intent.putExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT, productVariantUnitSubmit);
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
        long selectedUnitId = ((ProductVariantPickerSearchFragment) getSearchListFragment()).getSelectedUnitId();
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = ((ProductVariantPickerCacheFragment) getCacheListFragment()).getVariantSubmitOptionList();
        ArrayList<String> titleList = new ArrayList<>();
        titleList.addAll(ProductVariantUtils.getAllVariantOptionNameList(selectedUnitId, productVariantOptionSubmitList, productVariantByCatModel.getUnitList()));
        bundle.putStringArrayList(ProductVariantItemPickerAddDialogFragment.EXTRA_VARIANT_RESERVED_TITLE_LIST, titleList);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), DIALOG_ADD_VARIANT_TAG);
    }

    @Override
    public void onBackPressed() {
        if (hasAnyUpdate) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.product_variant_dialog_cancel_title))
                    .setMessage(getString(R.string.product_variant_dialog_cancel_message))
                    .setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ProductVariantPickerActivity.super.onBackPressed();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }
}