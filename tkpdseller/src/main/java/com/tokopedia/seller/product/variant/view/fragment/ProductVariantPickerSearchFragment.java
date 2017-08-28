package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerSearchListAdapter;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantItemPickerSearchEmptyDataBinder;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ProductVariantOption>
        implements BasePickerItemSearchList<ProductVariantViewModel>,
        BaseMultipleCheckListAdapter.CheckedCallback<ProductVariantOption>,
        BaseEmptyDataBinder.Callback {

    private static final int MINIMUM_SHOW_UNIT_SIZE = 2;
    private static final int MINIMUM_SHOW_SEARCH_BOX = 20;

    private ProductVariantPickerMultipleItem<ProductVariantViewModel> pickerMultipleItem;

    private List<ProductVariantUnit> productVariantUnitList;
    private List<ProductVariantOption> productVariantOptionList;
    private List<ProductVariantOption> filteredProductVariantOptionList;
    private ProductVariantUnitSubmit productVariantUnitSubmit;

    private long selectedVariantUnitId;
    private String unitName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (ProductVariantPickerMultipleItem<ProductVariantViewModel>) getActivity();
        }
        ProductVariantByCatModel productVariantByCatModel =
                getActivity().getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_CATEGORY);
        if (productVariantByCatModel != null) {
            unitName = productVariantByCatModel.getName();
            productVariantUnitList = productVariantByCatModel.getUnitList();
            productVariantOptionList = productVariantUnitList.get(0).getProductVariantOptionList();
            selectedVariantUnitId = productVariantUnitList.get(0).getUnitId();
            filteredProductVariantOptionList = productVariantOptionList;
        }
        productVariantUnitSubmit = getActivity().getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT);
        if (productVariantUnitSubmit != null) {
            selectedVariantUnitId = productVariantUnitSubmit.getVariantUnitId();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_variant_picker_search_list;
    }

    @Override
    protected BaseMultipleCheckListAdapter<ProductVariantOption> getNewAdapter() {
        return new ProductVariantPickerSearchListAdapter();
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        ProductVariantItemPickerSearchEmptyDataBinder emptyDataBinder = new ProductVariantItemPickerSearchEmptyDataBinder(adapter);
        emptyDataBinder.setEmptyTitleText(getString(R.string.label_empty_value));
        emptyDataBinder.setEmptyContentText(null);
        emptyDataBinder.setEmptyButtonItemText(getString(R.string.label_back));
        emptyDataBinder.setCallback(this);
        return emptyDataBinder;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SpinnerTextView unitSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_variant_unit);
        unitSpinnerTextView.setHint(getString(R.string.product_variant_standard_unit_x, unitName) );
        unitSpinnerTextView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
                    if (value.equalsIgnoreCase(String.valueOf(productVariantUnit.getUnitId()))) {
                        selectedVariantUnitId = productVariantUnit.getUnitId();
                        productVariantOptionList = productVariantUnit.getProductVariantOptionList();
                        filteredProductVariantOptionList = productVariantOptionList;
                        pickerMultipleItem.removeAllItemFromSearch();
                        ((BaseMultipleCheckListAdapter<ProductVariantOption>) adapter).clearCheck();
                        resetPageAndSearch();
                        break;
                    }
                }
            }
        });
        if (productVariantUnitList != null && productVariantUnitList.size() >= MINIMUM_SHOW_UNIT_SIZE) {
            String[] variantUnitTextArray = new String[productVariantUnitList.size()];
            String[] variantUnitValueArray = new String[productVariantUnitList.size()];
            int i = 0;
            for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
                variantUnitTextArray[i] = productVariantUnit.getName();
                variantUnitValueArray[i] = String.valueOf(productVariantUnit.getUnitId());
                i++;
            }
            unitSpinnerTextView.setEntries(variantUnitTextArray);
            unitSpinnerTextView.setValues(variantUnitValueArray);
            unitSpinnerTextView.setVisibility(View.VISIBLE);
            unitSpinnerTextView.setSpinnerValue(String.valueOf(selectedVariantUnitId));
        }
        if (productVariantOptionList != null && productVariantOptionList.size() >= MINIMUM_SHOW_SEARCH_BOX) {
            searchInputView.setVisibility(View.VISIBLE);
        }
        pickerMultipleItem.validateFooterAndInfoView();
        initCheckedItem();
        resetPageAndSearch();
    }

    private void initCheckedItem() {
        if (productVariantUnitSubmit == null) {
            return;
        }
        for (ProductVariantOptionSubmit productVariantOptionSubmit : productVariantUnitSubmit.getProductVariantOptionSubmitList()) {
            ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
            String title = "";
            if (TextUtils.isEmpty(productVariantOptionSubmit.getCustomText())) {
                ProductVariantOption productVariantOption = ProductVariantUtils.getProductVariantValue(productVariantOptionSubmit.getVariantUnitValueId(), productVariantOptionList);
                if (productVariantOption != null) {
                    ((BaseMultipleCheckListAdapter<ProductVariantOption>) adapter).setChecked(productVariantOption.getId(), true);
                    title = productVariantOption.getValue();
                    productVariantViewModel.setHexCode(productVariantOption.getHexCode());
                }
            } else {
                title = productVariantOptionSubmit.getCustomText();
            }
            productVariantViewModel.setTemporaryId(productVariantOptionSubmit.getTemporaryId());
            productVariantViewModel.setUnitValueId(productVariantOptionSubmit.getVariantUnitValueId());
            productVariantViewModel.setTitle(title);
            pickerMultipleItem.addItemFromSearch(productVariantViewModel, false);
        }
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        ((BaseMultipleCheckListAdapter<ProductVariantOption>) adapter).setCheckedCallback(this);
    }

    @Override
    protected void searchForPage(int page) {
        if (filteredProductVariantOptionList != null) {
            onSearchLoaded(filteredProductVariantOptionList, filteredProductVariantOptionList.size());
        }
    }

    @Override
    public void onItemClicked(ProductVariantOption productVariantOption) {
        // Already handled onItemChecked
    }

    @Override
    public void onItemChecked(ProductVariantOption productVariantOption, boolean checked) {
        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
        productVariantViewModel.setUnitValueId(Long.parseLong(productVariantOption.getId()));
        productVariantViewModel.setHexCode(productVariantOption.getHexCode());
        productVariantViewModel.setTitle(productVariantOption.getValue());
        productVariantViewModel.setImageUrl(productVariantOption.getIcon());
        if (checked) {
            pickerMultipleItem.addItemFromSearch(productVariantViewModel, true);
        } else {
            pickerMultipleItem.removeItemFromSearch(productVariantViewModel);
        }
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        pickerMultipleItem.validateFooterAndInfoView();
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        pickerMultipleItem.validateFooterAndInfoView();
    }

    @Override
    protected void showViewList(@NonNull List<ProductVariantOption> list) {
        super.showViewList(list);
        pickerMultipleItem.validateFooterAndInfoView();
    }

    @Override
    public void deselectItem(ProductVariantViewModel productVariantViewModel) {
        ((BaseMultipleCheckListAdapter<ProductVariantOption>) adapter).setChecked(String.valueOf(productVariantViewModel.getUnitValueId()), false);
        resetPageAndSearch();
    }

    @Override
    public void onSearchSubmitted(String text) {
        filterSearch(text);
        resetPageAndSearch();
    }

    @Override
    public void onSearchTextChanged(String text) {
        filterSearch(text);
        resetPageAndSearch();
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // Do nothing
    }

    @Override
    public void onEmptyButtonClicked() {

    }

    public List<ProductVariantOption> getItemList() {
        return filteredProductVariantOptionList;
    }

    private void filterSearch(String text) {
        if (TextUtils.isEmpty(text)) {
            filteredProductVariantOptionList = productVariantOptionList;
            return;
        }
        List<ProductVariantOption> productVariantOptionListTemp = new ArrayList<>();
        for (ProductVariantOption productVariantUnit : productVariantOptionList) {
            if (productVariantUnit.getValue().toLowerCase().contains(text.toLowerCase())) {
                productVariantOptionListTemp.add(productVariantUnit);
            }
        }
        filteredProductVariantOptionList = productVariantOptionListTemp;
    }

    public long getSelectedUnitId() {
        return selectedVariantUnitId;
    }
}