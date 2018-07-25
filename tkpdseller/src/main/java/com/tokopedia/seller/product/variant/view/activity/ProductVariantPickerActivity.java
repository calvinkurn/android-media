package com.tokopedia.seller.product.variant.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.product.edit.common.util.ProductVariantConstant;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.product.edit.common.model.edit.VariantPictureViewModel;
import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.edit.common.model.variantbycat.ProductVariantOption;
import com.tokopedia.product.edit.common.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.product.edit.common.model.variantbyprd.variantoption.ProductVariantOptionParent;
import com.tokopedia.seller.product.variant.view.dialog.ProductVariantItemPickerAddDialogFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchFragment;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerActivity extends BasePickerMultipleItemActivity<ProductVariantOption>
        implements ProductVariantPickerMultipleItem<ProductVariantOption>,
        ProductVariantItemPickerAddDialogFragment.Listener,
        ProductVariantPickerCacheFragment.OnProductVariantPickerCacheNewFragmentListener,
        ProductVariantPickerSearchFragment.OnProductVariantPickerSearchNewFragmentListener,
        ProductVariantItemPickerAddDialogFragment.OnProductVariantItemPickerAddDialogFragmentListener {

    private static final String DIALOG_ADD_VARIANT_TAG = "DIALOG_ADD_VARIANT_TAG";
    public static final String EXTRA_PRODUCT_VARIANT_CATEGORY_LEVEL = "extra_product_variant_cat_level";
    public static final String EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL = "extra_product_variant_smt_level";
    public static final String EXTRA_HAS_ORIGINAL_VARIANT = "extra_has_original_variant";

    // store the catalog
    private ProductVariantByCatModel productVariantByCatModel;

    // store what currently selected
    private ProductVariantOptionParent productVariantOptionParent;
    private boolean hasOriVariant;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        productVariantByCatModel = getIntent().getParcelableExtra(EXTRA_PRODUCT_VARIANT_CATEGORY_LEVEL);
        if (savedInstanceState == null) {
            productVariantOptionParent = getIntent().getParcelableExtra(EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL);
            hasOriVariant = getIntent().getBooleanExtra(EXTRA_HAS_ORIGINAL_VARIANT, false);
            if (productVariantOptionParent == null) {
                productVariantOptionParent = new ProductVariantOptionParent();
                productVariantOptionParent.setProductVariantOptionChild(new ArrayList<ProductVariantOptionChild>());
                productVariantOptionParent.setVu(productVariantByCatModel.getUnitList().get(0).getUnitId());
                productVariantOptionParent.setV(productVariantByCatModel.getVariantId());
                productVariantOptionParent.setPosition(productVariantByCatModel.getLevel());
                productVariantOptionParent.setName(productVariantByCatModel.getName());
                productVariantOptionParent.setIdentifier(productVariantByCatModel.getIdentifier());
                productVariantOptionParent.setUnitName(productVariantByCatModel.getUnitList().get(0).getName());
            }
        } else {
            productVariantOptionParent = savedInstanceState.getParcelable(EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL);
        }

        super.onCreate(savedInstanceState);
        toolbar.setTitle(getString(R.string.product_variant_option_x, productVariantByCatModel.getName()));
        updateBottomSheetInfo();
    }

    public String getVariantName() {
        return productVariantByCatModel.getName();
    }

    @Override
    public ProductVariantByCatModel getProductVariantByCatModel() {
        return productVariantByCatModel;
    }

    @Override
    public ProductVariantOptionParent getProductVariantOptionParent() {
        return productVariantOptionParent;
    }

    @Override
    protected Fragment getInitialSearchListFragment() {
        return ProductVariantPickerSearchFragment.newInstance();
    }

    @Override
    protected Fragment getInitialCacheListFragment() {
        return ProductVariantPickerCacheFragment.newInstance();
    }

    @Override
    public boolean isDataColorType() {
        return productVariantByCatModel.isDataColorType();
    }

    @Override
    public void removeItemFromSearch(ProductVariantOption productVariantOption) {
        super.removeItemFromSearch(productVariantOption);
        updateBottomSheetInfo();
    }

    @Override
    public void removeAllItemFromSearch() {
        ((ProductVariantPickerCacheFragment) getCacheListFragment()).removeAllItem();
        updateBottomSheetInfo();
    }

    @Override
    public boolean hasOriginalVariant() {
        return hasOriVariant;
    }

    @Override
    public void addItemFromSearch(ProductVariantOption productVariantOption) {
        super.addItemFromSearch(productVariantOption);
        updateBottomSheetInfo();
    }

    @Override
    public void removeItemFromCache(ProductVariantOption productVariantOption) {
        super.removeItemFromCache(productVariantOption);
        updateBottomSheetInfo();
    }

    @Override
    public void onTextPickerSubmitted(String text) {
        ProductVariantOption productVariantOption = new ProductVariantOption(0, text, null, null);
        ((ProductVariantPickerSearchFragment) getSearchFragment()).addCustomOption(productVariantOption);
        addItemFromSearch(productVariantOption);
        validateFooterAndInfoView();
        expandBottomSheet();
    }

    @Override
    protected Intent getDefaultIntentResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL, collectListDataFromView());
        return intent;
    }

    private ProductVariantOptionParent collectListDataFromView() {
        // get data from cache and map to the original source
        List<ProductVariantOption> productVariantOptionList = getCacheFragment().getItemList();
        List<ProductVariantOptionChild> productVariantOptionChildList = new ArrayList<>();

        HashMap<String, Integer> hashMapValueToIndex = new HashMap<>();
        createMap(productVariantOptionParent.getProductVariantOptionChild(), hashMapValueToIndex);
        for (int i = 0, sizei = productVariantOptionList.size(); i < sizei; i++) {
            ProductVariantOption productVariantOption = productVariantOptionList.get(i);

            //we get the original to get the pvo id and original images (in case we edit, so we do not lose the original attribute)
            Integer index = hashMapValueToIndex.get(productVariantOption.getValue());
            ProductVariantOptionChild productVariantOptionChild = null;
            if (index != null) {
                ProductVariantOptionChild originalProductOptionChild =
                        productVariantOptionParent.getProductVariantOptionChild().get(index);

                int originalPvo = 0;
                List<VariantPictureViewModel> originalPictureList = null;
                if (originalProductOptionChild != null) {
                    originalPvo = originalProductOptionChild.getPvo();
                    originalPictureList = originalProductOptionChild.getProductPictureViewModelList();
                }
                productVariantOptionChild =
                        new ProductVariantOptionChild(
                                originalPvo,
                                productVariantOption.getValueId(),
                                productVariantOption.getValue(),
                                productVariantOption.getHexCode(),
                                originalPictureList);
            } else {
                productVariantOptionChild =
                        new ProductVariantOptionChild(
                                0,
                                productVariantOption.getValueId(),
                                productVariantOption.getValue(),
                                productVariantOption.getHexCode(),
                                null);
            }
            productVariantOptionChildList.add(productVariantOptionChild);
        }
        productVariantOptionParent.setProductVariantOptionChild(productVariantOptionChildList);
        return productVariantOptionParent;
    }

    private void createMap(List<ProductVariantOptionChild> productVariantOptionChildList,
                           HashMap<String, Integer> hashMapValueToIndex) {
        if (productVariantOptionChildList == null) {
            return;
        }
        for (int i = 0, sizei = productVariantOptionChildList.size(); i < sizei; i++) {
            hashMapValueToIndex.put(productVariantOptionChildList.get(i).getValue(), i);
        }
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
            selectedItemSize = ((ProductVariantPickerSearchFragment) fragment).getFilteredList().size();
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

    protected int getSubmitTextRes() {
        return R.string.title_save;
    }

    @Override
    protected void submitButtonClicked() {
        //validate the variant, if it has already got the variant
        // (for example: user already select variant for "color", then for the next edit, this color minimum must be 1)
        if (hasOriVariant && getCacheListSize() == 0) {
            NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.product_variant_option_cannot_empty));
            return;
        }
        super.submitButtonClicked();
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

    @Override
    public boolean doesVariantOptionAlreadyExist(@NonNull String trimmedStringToAdd) {
        List<ProductVariantOption> productVariantOptionList =
                ((ProductVariantPickerSearchFragment) getSearchFragment()).getAllList();
        for (int i = 0, sizei = productVariantOptionList.size(); i < sizei; i++) {
            ProductVariantOption productVariantOption = productVariantOptionList.get(i);
            if (productVariantOption.getValue().equalsIgnoreCase(trimmedStringToAdd)) {
                return true;
            }
        }
        return false;
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
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), DIALOG_ADD_VARIANT_TAG);
    }

    public void showAddDialog() {
        showAddDialog(((ProductVariantPickerSearchFragment) getSearchFragment()).getSearchText());
    }


    @Override
    protected boolean isToolbarWhite() {
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL, collectListDataFromView());
    }
}