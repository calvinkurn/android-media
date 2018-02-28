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

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;
import com.tokopedia.seller.product.variant.view.dialog.ProductVariantItemPickerAddDialogFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheNewFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchNewFragment;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public class ProductVariantPickerNewActivity extends BasePickerMultipleItemActivity<ProductVariantOption>
        implements ProductVariantPickerMultipleItem<ProductVariantOption>,
        ProductVariantItemPickerAddDialogFragment.Listener,
        ProductVariantPickerCacheNewFragment.OnProductVariantPickerCacheNewFragmentListener,
        ProductVariantPickerSearchNewFragment.OnProductVariantPickerSearchNewFragmentListener,
        ProductVariantItemPickerAddDialogFragment.OnProductVariantItemPickerAddDialogFragmentListener {

    private static final String DIALOG_ADD_VARIANT_TAG = "DIALOG_ADD_VARIANT_TAG";
    public static final String EXTRA_PRODUCT_VARIANT_CATEGORY_LEVEL = "extra_product_variant_cat_level";
    public static final String EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL = "extra_product_variant_smt_level";

    // store the catalog
    private ProductVariantByCatModel productVariantByCatModel;

    // store what currently selected
    private ProductVariantOptionParent productVariantOptionParent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        productVariantByCatModel = getIntent().getParcelableExtra(EXTRA_PRODUCT_VARIANT_CATEGORY_LEVEL);
        if (savedInstanceState == null) {
            productVariantOptionParent = getIntent().getParcelableExtra(EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL);
            if (productVariantOptionParent == null) {
                productVariantOptionParent = new ProductVariantOptionParent();
                productVariantOptionParent.setProductVariantOptionChild(new ArrayList<ProductVariantOptionChild>());
                productVariantOptionParent.setVu(productVariantByCatModel.getUnitList().get(0).getUnitId());
                productVariantOptionParent.setV(productVariantByCatModel.getVariantId());
                productVariantOptionParent.setPosition(productVariantByCatModel.getStatus() == 2 ? 1 : 0);
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
        return ProductVariantPickerSearchNewFragment.newInstance();
    }

    @Override
    protected Fragment getInitialCacheListFragment() {
        return ProductVariantPickerCacheNewFragment.newInstance();
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
        ((ProductVariantPickerCacheNewFragment) getCacheListFragment()).removeAllItem();
        updateBottomSheetInfo();
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
        ((ProductVariantPickerSearchNewFragment) getSearchFragment()).addCustomOption(productVariantOption);
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
        for (int i = 0, sizei = productVariantOptionList.size(); i < sizei; i++) {
            ProductVariantOption productVariantOption = productVariantOptionList.get(i);

            //we get the original to get the pvo id and original images (in case we edit, so we do not lose the original attribute)
            ProductVariantOptionChild originalProductOptionChild = getOriginalProductOptionChild(productVariantOption.getValue());
            int originalPvo = 0;
            List<ProductPictureViewModel> originalPictureList = null;
            if (originalProductOptionChild!= null) {
                originalPvo = originalProductOptionChild.getPvo();
                originalPictureList = originalProductOptionChild.getProductPictureViewModelList();
            }
            ProductVariantOptionChild productVariantOptionChild =
                    new ProductVariantOptionChild(
                            originalPvo,
                            productVariantOption.getValueId(),
                            productVariantOption.getValue(),
                            productVariantOption.getHexCode(),
                            originalPictureList);
            productVariantOptionChildList.add(productVariantOptionChild);
        }
        productVariantOptionParent.setProductVariantOptionChild(productVariantOptionChildList);
        return productVariantOptionParent;
    }

    /**
     * get original selection nbased on value
     * for example: "Merah" -> pvo:1002, image:...
     */
    private ProductVariantOptionChild getOriginalProductOptionChild(String value) {
        List<ProductVariantOptionChild> productVariantOptionChildList = productVariantOptionParent.getProductVariantOptionChild();
        for (int i = 0, sizei = productVariantOptionChildList.size(); i< sizei; i++) {
            if (value.equalsIgnoreCase(productVariantOptionChildList.get(i).getValue())) {
                return productVariantOptionChildList.get(i);
            }
        }
        return null;
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
            selectedItemSize = ((ProductVariantPickerSearchNewFragment) fragment).getFilteredList().size();
        }
        return selectedItemSize;
    }

    private int getCacheListSize() {
        Fragment fragment = getCacheListFragment();
        int selectedItemSize = 0;
        if ((fragment) != null) {
            selectedItemSize = ((ProductVariantPickerCacheNewFragment) fragment).getItemList().size();
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
        return ((ProductVariantPickerCacheNewFragment) getCacheListFragment()).getItemList().size()
                >= ProductVariantConstant.MAX_LIMIT_VARIANT;
    }

    private void showMaxVariantReachedMessage() {
        NetworkErrorHelper.showCloseSnackbar(this, getString(R.string.product_variant_max_variant_has_been_reached));
    }

    @Override
    public boolean doesVariantOptionAlreadyExist(@NonNull String trimmedStringToAdd) {
        List<ProductVariantOption> productVariantOptionList =
                ((ProductVariantPickerSearchNewFragment) getSearchFragment()).getAllList();
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
        showAddDialog(((ProductVariantPickerSearchNewFragment) getSearchFragment()).getSearchText());
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