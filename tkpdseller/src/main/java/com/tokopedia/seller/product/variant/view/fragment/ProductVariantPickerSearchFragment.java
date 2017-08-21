package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantSubmitOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerSearchListAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ProductVariantValue>
        implements BasePickerItemSearchList<ProductVariantViewModel>, BaseMultipleCheckListAdapter.CheckedCallback<ProductVariantValue> {

    private static final int MINIMUM_SHOW_UNIT_SIZE = 2;

    private ProductVariantPickerMultipleItem<ProductVariantViewModel> pickerMultipleItem;

    private List<ProductVariantUnit> productVariantUnitList;
    private List<ProductVariantValue> productVariantValueList;
    private List<ProductVariantValue> filteredProductVariantValueList;
    private VariantUnitSubmit variantUnitSubmit;
    private SpinnerTextView unitSpinnerTextView;

    private long currentVariantUnitId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (ProductVariantPickerMultipleItem<ProductVariantViewModel>) getActivity();
        }
        ProductVariantByCatModel productVariantByCatModel = getActivity().getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_CATEGORY);
        if (productVariantByCatModel != null) {
            productVariantUnitList = productVariantByCatModel.getUnitList();
            productVariantValueList = productVariantUnitList.get(0).getProductVariantValueList();
            currentVariantUnitId = productVariantUnitList.get(0).getUnitId();
            filteredProductVariantValueList = productVariantValueList;
        }
        variantUnitSubmit = getActivity().getIntent().getParcelableExtra(ProductVariantConstant.EXTRA_PRODUCT_VARIANT_UNIT_SUBMIT);
        if (variantUnitSubmit != null) {
            currentVariantUnitId = variantUnitSubmit.getVariantUnitId();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_variant_picker_search_list;
    }

    @Override
    protected BaseMultipleCheckListAdapter<ProductVariantValue> getNewAdapter() {
        return new ProductVariantPickerSearchListAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unitSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_variant_unit);
        unitSpinnerTextView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
                    if (value.equalsIgnoreCase(String.valueOf(productVariantUnit.getUnitId()))) {
                        currentVariantUnitId = productVariantUnit.getUnitId();
                        productVariantValueList = productVariantUnit.getProductVariantValueList();
                        filteredProductVariantValueList = productVariantValueList;
                        pickerMultipleItem.removeAllItemFromSearch();
                        ((BaseMultipleCheckListAdapter<ProductVariantValue>) adapter).clearCheck();
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
            unitSpinnerTextView.setSpinnerValue(String.valueOf(currentVariantUnitId));
        }
        initCheckedItem();
        resetPageAndSearch();
    }

    private void initCheckedItem() {
        if (variantUnitSubmit == null) {
            return;
        }
        for (VariantSubmitOption variantSubmitOption : variantUnitSubmit.getVariantSubmitOptionList()) {
            if (TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
                ProductVariantValue productVariantValue = ProductVariantUtils.getProductVariantValue(variantSubmitOption, productVariantValueList);
                if (productVariantValue != null) {
                    ((BaseMultipleCheckListAdapter<ProductVariantValue>) adapter).setChecked(productVariantValue.getId(), true);
                    onItemChecked(productVariantValue, true);
                }
            } else {
                ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
                productVariantViewModel.setUnitValueId(variantSubmitOption.getVariantUnitValueId());
                productVariantViewModel.setTitle(variantSubmitOption.getCustomText());
                pickerMultipleItem.addItemFromSearch(productVariantViewModel);
            }
        }
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        ((BaseMultipleCheckListAdapter<ProductVariantValue>) adapter).setCheckedCallback(this);
    }

    @Override
    protected void searchForPage(int page) {
        if (filteredProductVariantValueList != null) {
            onSearchLoaded(filteredProductVariantValueList, filteredProductVariantValueList.size());
        }
    }

    @Override
    public void onItemClicked(ProductVariantValue productVariantValue) {
        // Already handled onItemChecked
    }

    @Override
    public void onItemChecked(ProductVariantValue productVariantValue, boolean checked) {
        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
        productVariantViewModel.setUnitValueId(Long.parseLong(productVariantValue.getId()));
        productVariantViewModel.setHexCode(productVariantValue.getHexCode());
        productVariantViewModel.setTitle(productVariantValue.getValue());
        productVariantViewModel.setImageUrl(productVariantValue.getIcon());
        if (checked) {
            pickerMultipleItem.addItemFromSearch(productVariantViewModel);
        } else {
            pickerMultipleItem.removeItemFromSearch(productVariantViewModel);
        }
    }

    @Override
    public void deselectItem(ProductVariantViewModel productVariantViewModel) {
        ((BaseMultipleCheckListAdapter<ProductVariantValue>) adapter).setChecked(String.valueOf(productVariantViewModel.getUnitValueId()), false);
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

    private void filterSearch(String text) {
        if (TextUtils.isEmpty(text)) {
            filteredProductVariantValueList = productVariantValueList;
            return;
        }
        List<ProductVariantValue> productVariantValueListTemp = new ArrayList<>();
        for (ProductVariantValue productVariantUnit : productVariantValueList) {
            if (productVariantUnit.getValue().toLowerCase().contains(text.toLowerCase())) {
                productVariantValueListTemp.add(productVariantUnit);
            }
        }
        filteredProductVariantValueList = productVariantValueListTemp;
    }

    public long getCurrentUnitId() {
        return currentVariantUnitId;
    }
}