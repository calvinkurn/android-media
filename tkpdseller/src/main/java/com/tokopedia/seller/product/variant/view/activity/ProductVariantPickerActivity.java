package com.tokopedia.seller.product.variant.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
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
@Deprecated
public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity<ProductVariantViewModel>
        implements ProductVariantPickerMultipleItem<ProductVariantViewModel>, ProductVariantItemPickerAddDialogFragment.Listener {

    private static final String DIALOG_ADD_VARIANT_TAG = "DIALOG_ADD_VARIANT_TAG";

    private ProductVariantByCatModel productVariantByCatModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        productVariantByCatModel = getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_CATEGORY);
        super.onCreate(savedInstanceState);
        toolbar.setTitle(getString(R.string.product_variant_option_x, productVariantByCatModel.getName()));
        updateBottomSheetInfo();
    }

    public String getVariantName() {
        return productVariantByCatModel.getName();
    }

    @Override
    protected Fragment getInitialSearchListFragment() {
        return new ProductVariantPickerSearchFragment();
    }

    @Override
    protected Fragment getInitialCacheListFragment() {
        return ProductVariantPickerCacheFragment.newInstance(productVariantByCatModel.getVariantId() == ProductVariantConstant.COLOR_ID);
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
        expandBottomSheet();
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

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        bottomSheetContentTextView.setText(getString(R.string.product_variant_max_limit, ProductVariantConstant.MAX_LIMIT_VARIANT));
        bottomSheetContentTextView.setVisibility(View.VISIBLE);
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
    public boolean allowAddItem() {
        if (isMaxVariantReached()) {
            showMaxVariantReachedMessage();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_variant_item_picker, menu);
        updateOptionMenuColor(menu);
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

    private boolean isMaxVariantReached() {
        return ((ProductVariantPickerCacheFragment) getCacheListFragment()).getItemList().size()
                >= ProductVariantConstant.MAX_LIMIT_VARIANT;
    }

    private void showMaxVariantReachedMessage() {
        NetworkErrorHelper.showCloseSnackbar(this, getString(R.string.product_variant_max_variant_has_been_reached));
    }

    public void showAddDialog(String stringToAdd) {
        if (isMaxVariantReached()) {
            showMaxVariantReachedMessage();
            return;
        }
        ProductVariantItemPickerAddDialogFragment dialogFragment = new ProductVariantItemPickerAddDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProductVariantItemPickerAddDialogFragment.EXTRA_VARIANT_TITLE, productVariantByCatModel.getName());
        bundle.putString(ProductVariantItemPickerAddDialogFragment.EXTRA_STRING_TO_INPUT, stringToAdd);
//        long selectedUnitId = ((ProductVariantPickerSearchFragment) getSearchListFragment()).getSelectedUnitId();
//        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = ((ProductVariantPickerCacheFragment) getCacheListFragment()).getVariantSubmitOptionList();
//        ArrayList<String> titleList = new ArrayList<>();
//        titleList.addAll(ProductVariantUtils.getAllVariantOptionNameList(selectedUnitId, productVariantOptionSubmitList, productVariantByCatModel.getUnitList()));
//        bundle.putStringArrayList(ProductVariantItemPickerAddDialogFragment.EXTRA_VARIANT_RESERVED_TITLE_LIST, titleList);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), DIALOG_ADD_VARIANT_TAG);
    }

    public void showAddDialog() {
        showAddDialog("");
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}