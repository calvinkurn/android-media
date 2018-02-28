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
import android.widget.Toast;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;
import com.tokopedia.seller.product.variant.view.dialog.ProductVariantItemPickerAddDialogFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerCacheNewFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantPickerSearchNewFragment;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;

import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

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
        productVariantOptionParent = getIntent().getParcelableExtra(EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL);

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
    public void onVariantUnitChanged(int variantUnit) {
        productVariantOptionParent.setVu(variantUnit);
        removeAllItemFromSearch();
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
        Toast.makeText(getContext(),"remove item from search", Toast.LENGTH_LONG).show();
//        super.removeItemFromSearch(productVariantViewModel);
//        updateBottomSheetInfo();
    }

    @Override
    public void removeAllItemFromSearch() {
        ((ProductVariantPickerCacheNewFragment) getCacheListFragment()).removeAllItem();
        updateBottomSheetInfo();
    }

    @Override
    public void addItemFromSearch(ProductVariantOption productVariantOption) {
        Toast.makeText(getContext(),"add item from search", Toast.LENGTH_LONG).show();
//        super.addItemFromSearch(productVariantViewModel);
//        updateBottomSheetInfo();
    }

    @Override
    public void removeItemFromCache(ProductVariantOption productVariantOption) {
        Toast.makeText(getContext(),"removeItemFromCache", Toast.LENGTH_LONG).show();
//        super.removeItemFromCache(productVariantViewModel);
//        updateBottomSheetInfo();
    }

    @Override
    public void onTextPickerSubmitted(String text) {
        Toast.makeText(getContext(),"onTextPickerSubmitted", Toast.LENGTH_LONG).show();
//        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
//        productVariantViewModel.setTitle(text);
//        addItemFromSearch(productVariantViewModel);
//        validateFooterAndInfoView();
//        expandBottomSheet();
    }

    @Override
    protected Intent getDefaultIntentResult() {
//        ProductVariantUnitSubmit productVariantUnitSubmit = new ProductVariantUnitSubmit();
//        productVariantUnitSubmit.setVariantId(productVariantByCatModel.getVariantId());
//        productVariantUnitSubmit.setVariantUnitId(((ProductVariantPickerSearchNewFragment) getSearchListFragment()).getSelectedUnitId());
//        productVariantUnitSubmit.setPosition(ProductVariantViewConverter.getVariantPositionByStatus(productVariantByCatModel.getStatus()));
//        productVariantUnitSubmit.setProductVariantOptionSubmitList(((ProductVariantPickerCacheNewFragment) getCacheListFragment()).getVariantSubmitOptionList());
        // TODO collect data from view to productVariantOptionParent
        // TODO setUnitId
        // TODO get all product Option
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRODUCT_VARIANT_SUBMIT_LEVEL, productVariantOptionParent);
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
            selectedItemSize = ((ProductVariantPickerSearchNewFragment) fragment).getItemList().size();
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

    protected int getSubmitTextRes(){
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
        // Catalog can have many units
        // get the selected unit from submitModel, then get the unitList from the catalog
        // after get the selectedcatalog by unitId, loop each value, compare with the text
        // also the text must be compared with the custom value from the submitModel, since the catalog only has the predefined variant option

        int selectedUnitId = productVariantOptionParent == null ? 0 : productVariantOptionParent.getVu();
        ProductVariantUnit productVariantUnit = null;
        if (productVariantByCatModel.hasUnit()) {
            List <ProductVariantUnit> productVariantUnitList = productVariantByCatModel.getUnitList();
            for (int i = 0, sizei = productVariantUnitList.size(); i<sizei; i++) {
                if (productVariantUnitList.get(i).getUnitId() == selectedUnitId) {
                    productVariantUnit = productVariantUnitList.get(i);
                    break;
                }
            }
        } else {
            productVariantUnit = productVariantByCatModel.getUnitList().get(0);
        }

        // check value from catalog
        if (productVariantUnit!= null && productVariantUnit.getProductVariantOptionList()!= null) {
            List<ProductVariantOption> productVariantOptionList = productVariantUnit.getProductVariantOptionList();
            for (int i = 0, sizei = productVariantOptionList.size(); i < sizei; i++) {
                ProductVariantOption productVariantOption = productVariantOptionList.get(i);
                if (productVariantOption.getValue().equalsIgnoreCase(trimmedStringToAdd)){
                    return true;
                }
            }
        }

        // check value from submit model (custom only)
        if (productVariantOptionParent!= null) {
            List<ProductVariantOptionChild> productVariantOptionChildren = productVariantOptionParent.getProductVariantOptionChild();
            for (int i = 0, sizei = productVariantOptionChildren.size(); i < sizei; i++) {
                ProductVariantOptionChild productVariantOptionChild = productVariantOptionChildren.get(i);
                if (productVariantOptionChild.getValue().equalsIgnoreCase(trimmedStringToAdd)){
                    return true;
                }
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
        showAddDialog("");
    }


    @Override
    protected boolean isToolbarWhite() {
        return true;
    }


}