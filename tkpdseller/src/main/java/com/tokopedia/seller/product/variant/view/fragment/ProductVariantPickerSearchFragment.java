package com.tokopedia.seller.product.variant.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerSearchListNewAdapter;
import com.tokopedia.seller.product.variant.view.listener.ProductVariantPickerMultipleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ProductVariantOption>
        implements BasePickerItemSearchList<ProductVariantOption>,
        BaseMultipleCheckListAdapter.CheckedCallback<ProductVariantOption>,
        BaseEmptyDataBinder.Callback {

    private static final int MINIMUM_SHOW_SEARCH_BOX = 20;

    private ProductVariantPickerMultipleItem<ProductVariantOption> pickerListener;
    private ProductVariantPickerSearchListNewAdapter productVariantPickerSearchListNewAdapter;

    private List<ProductVariantUnit> productVariantUnitList;
    private List<ProductVariantOption> productVariantOptionList;
    private List<ProductVariantOption> filteredProductVariantOptionList;

    private int selectedVariantUnitId;
    private String unitName;
    private ProductVariantByCatModel productVariantByCatModel;
    private String prevSearchText;

    public OnProductVariantPickerSearchNewFragmentListener onProductVariantPickerSearchNewFragmentListener;
    private ProductVariantOptionParent productVariantOptionParent;

    public interface OnProductVariantPickerSearchNewFragmentListener {
        boolean isDataColorType();

        void showAddDialog(String stringToAdd);

        String getVariantName();

        ProductVariantByCatModel getProductVariantByCatModel();

        ProductVariantOptionParent getProductVariantOptionParent();

        void removeAllItemFromSearch();

    }

    public static ProductVariantPickerSearchFragment newInstance() {
        return new ProductVariantPickerSearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productVariantPickerSearchListNewAdapter = new ProductVariantPickerSearchListNewAdapter(
                onProductVariantPickerSearchNewFragmentListener.isDataColorType());

        productVariantByCatModel = onProductVariantPickerSearchNewFragmentListener.getProductVariantByCatModel();
        unitName = productVariantByCatModel.getName();
        productVariantUnitList = productVariantByCatModel.getUnitList();

        productVariantOptionParent = onProductVariantPickerSearchNewFragmentListener.getProductVariantOptionParent();
        if (productVariantOptionParent == null) {
            int defaultUnitId = productVariantUnitList.get(0).getUnitId();
            // if user has not selected the unit before, as default select the first unit from catalog
            selectVariantUnitId(defaultUnitId);
        } else {
            // select the unit from the user's previous selection
            selectVariantUnitId(productVariantOptionParent.getVu());
        }
    }

    private void selectVariantUnitId(int variantUnitId) {
        selectedVariantUnitId = variantUnitId;
        for (int i = 0, sizei = productVariantUnitList.size(); i < sizei; i++) {
            if (productVariantUnitList.get(i).getUnitId() == selectedVariantUnitId) {
                productVariantOptionList = productVariantUnitList.get(i).getProductVariantOptionList();
                break;
            }
        }
        // if the selection unit is not found, default to select the first unit in the catalog
        if (productVariantOptionList == null) {
            productVariantOptionList = productVariantUnitList.get(0).getProductVariantOptionList();
        }
    }

    private void populateCheckedData(){
        productVariantPickerSearchListNewAdapter.resetCheckedItemSet();
        // if there is user selection, then add to the checked list.
        if (productVariantOptionParent != null && productVariantOptionParent.hasProductVariantOptionChild()) {
            List<ProductVariantOptionChild> productVariantOptionChildList =
                    productVariantOptionParent.getProductVariantOptionChild();
            for (int i = 0, sizei = productVariantOptionChildList.size(); i < sizei; i++) {
                ProductVariantOptionChild productVariantOptionChild = productVariantOptionChildList.get(i);
                // add the custom value to the list (if any)
                if (productVariantOptionChild.isCustomVariant()) {
                    // if it is custom variant, create new option, since the original catalog doesn't have it.
                    ProductVariantOption productVariantOption = new ProductVariantOption(
                            0,
                            productVariantOptionChild.getValue(),
                            productVariantOptionChild.getHex(), null);
                    productVariantOptionList.add(productVariantOption);

                    // to make the item in cache to be checked
                    productVariantPickerSearchListNewAdapter.setChecked(productVariantOptionChild.getValue(), true);
                    pickerListener.addItemFromSearch(productVariantOption);
                } else {
                    // to make the item in cache to be checked
                    productVariantPickerSearchListNewAdapter.setChecked(productVariantOptionChild.getValue(), true);
                    ProductVariantOption productVariantOption = getProductVariantOption(productVariantOptionChild.getValue());
                    if (productVariantOption != null) {
                        pickerListener.addItemFromSearch(productVariantOption);
                    }
                }

            }
        }
    }

    private ProductVariantOption getProductVariantOption(String value) {
        for (int j = 0, sizej = productVariantOptionList.size(); j < sizej; j++) {
            if (productVariantOptionList.get(j).getValue().equalsIgnoreCase(value)) {
                return productVariantOptionList.get(j);
            }
        }
        return null;
    }

    public void addCustomOption(ProductVariantOption productVariantOption) {
        productVariantOptionList.add(productVariantOption);
        productVariantPickerSearchListNewAdapter.setChecked(productVariantOption.getValue(), true);
        searchInputView.getSearchTextView().setText("");
        resetPageAndSearch();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.requestFocus();
        return view;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_variant_picker_search_list;
    }

    @Override
    protected BaseMultipleCheckListAdapter<ProductVariantOption> getNewAdapter() {
        return productVariantPickerSearchListNewAdapter;
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_variant_empty);
        emptyDataBinder.setEmptyTitleText(null);
        emptyDataBinder.setEmptyContentText(getString(R.string.title_no_result));
        emptyDataBinder.setEmptyContentItemText(null);
        emptyDataBinder.setEmptyButtonItemText(getString(R.string.product_variant_add_new_x, productVariantByCatModel.getName()));
        emptyDataBinder.setCallback(new BaseEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {

            }

            @Override
            public void onEmptyButtonClicked() {
                onProductVariantPickerSearchNewFragmentListener.showAddDialog(searchInputView.getSearchText());
            }
        });
        return emptyDataBinder;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateCheckedData();

        searchInputView.setSearchHint(
                getString(R.string.product_variant_search_x, onProductVariantPickerSearchNewFragmentListener.getVariantName()));

        SpinnerTextView unitSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_variant_unit);
        // if catalog has unit, then make the unit spinner visible, so user can select the unit.
        if (productVariantByCatModel.hasUnit()) {
            unitSpinnerTextView.setHint(getString(R.string.product_variant_standard_unit_x, unitName));

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

            unitSpinnerTextView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
                @Override
                public void onItemChanged(int position, String entry, String value) {
                    int unitIdTarget = Integer.parseInt(value);
                    if (selectedVariantUnitId != unitIdTarget) {
                        searchInputView.getSearchTextView().setText("");
                        productVariantOptionParent.setProductVariantOptionChild(new ArrayList<ProductVariantOptionChild>());
                        productVariantOptionParent.setVu(unitIdTarget);
                        onProductVariantPickerSearchNewFragmentListener.removeAllItemFromSearch();
                        productVariantPickerSearchListNewAdapter.resetCheckedItemSet();
                        selectVariantUnitId(unitIdTarget);
                        resetPageAndSearch();
                    }
                }
            });
        } else {
            unitSpinnerTextView.setVisibility(View.GONE);
        }

        pickerListener.validateFooterAndInfoView();
        resetPageAndSearch();
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        productVariantPickerSearchListNewAdapter.setCheckedCallback(this);
    }

    @Override
    protected void searchForPage(int page) {
        String textToSearch = searchInputView.getSearchText();
        filterSearch(textToSearch);
        prevSearchText = textToSearch;
        onSearchLoaded(filteredProductVariantOptionList, filteredProductVariantOptionList.size());
    }

    @Override
    protected void showSearchView(boolean show) {
        if (productVariantOptionList != null && productVariantOptionList.size() >= MINIMUM_SHOW_SEARCH_BOX) {
            super.showSearchView(show);
            return;
        }
        super.showSearchView(false);
    }

    @Override
    public void onItemClicked(ProductVariantOption productVariantOption) {
        // Already handled onItemChecked
    }

    @Override
    public void onItemChecked(ProductVariantOption productVariantOption, boolean checked) {
        if (checked) {
            if (pickerListener.allowAddItem()) {
                pickerListener.addItemFromSearch(productVariantOption);
            } else {
                productVariantPickerSearchListNewAdapter.setChecked(productVariantOption.getValue(), false);
                productVariantPickerSearchListNewAdapter.notifyDataSetChanged();
            }
        } else {
            pickerListener.removeItemFromSearch(productVariantOption);
        }
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        pickerListener.validateFooterAndInfoView();
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        pickerListener.validateFooterAndInfoView();
    }

    @Override
    protected void showViewList(@NonNull List<ProductVariantOption> list) {
        super.showViewList(list);
        pickerListener.validateFooterAndInfoView();
    }

    @Override
    public void deselectItem(ProductVariantOption productVariantOption) {
        productVariantPickerSearchListNewAdapter.setChecked(productVariantOption.getValue(), false);
        resetPageAndSearch();
    }

    @Override
    public void onSearchSubmitted(String text) {
        resetPageAndSearch();
        super.onSearchSubmitted(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        resetPageAndSearch();
        super.onSearchTextChanged(text);
    }

    public String getSearchText() {
        return searchInputView.getSearchText();
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // Do nothing
    }

    @Override
    public void onEmptyButtonClicked() {

    }

    public List<ProductVariantOption> getFilteredList() {
        return filteredProductVariantOptionList;
    }

    public List<ProductVariantOption> getAllList() {
        return productVariantOptionList;
    }

    private void filterSearch(String text) {
        if (TextUtils.isEmpty(text)) {
            filteredProductVariantOptionList = productVariantOptionList;
            return;
        }
        if (text.equalsIgnoreCase(prevSearchText)) {
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

    public int getSelectedUnitId() {
        return selectedVariantUnitId;
    }

    @Override
    protected long getDelayTextChanged() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        pickerListener = (ProductVariantPickerMultipleItem<ProductVariantOption>) context;
        onProductVariantPickerSearchNewFragmentListener =
                (OnProductVariantPickerSearchNewFragmentListener) context;
    }
}