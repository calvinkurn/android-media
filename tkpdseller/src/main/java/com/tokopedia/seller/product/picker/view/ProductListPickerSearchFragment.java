package com.tokopedia.seller.product.picker.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.picker.common.ProductListPickerConstant;
import com.tokopedia.seller.product.picker.di.DaggerProductListComponent;
import com.tokopedia.seller.product.picker.di.ProductListModule;
import com.tokopedia.seller.product.picker.view.adapter.ProductListPickerSearchAdapter;
import com.tokopedia.seller.product.picker.view.listener.ProductListPickerMultipleItem;
import com.tokopedia.seller.product.picker.view.listener.ProductListPickerSearchView;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;
import com.tokopedia.seller.product.picker.view.model.ProductListSellerModelView;
import com.tokopedia.seller.product.picker.view.presenter.ProductListPickerSearchPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ProductListPickerViewModel>
        implements BasePickerItemSearchList<ProductListPickerViewModel>, ProductListPickerSearchView,
        BaseMultipleCheckListAdapter.CheckedCallback<ProductListPickerViewModel> {

    @Inject
    ProductListPickerSearchPresenter productListPickerSearchPresenter;
    private ProductListPickerMultipleItem<ProductListPickerViewModel> productListPickerMultipleItem;

    private List<ProductListPickerViewModel> productListPickerViewModels;
    private String keywordFilter = "";
    private boolean hasNextPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof ProductListPickerMultipleItem) {
            productListPickerMultipleItem = (ProductListPickerMultipleItem<ProductListPickerViewModel>) getActivity();
        }

        productListPickerViewModels = getActivity().getIntent().getParcelableArrayListExtra(ProductListPickerConstant.PRODUCT_LIST_PICKER_MODEL_EXTRA);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productListPickerMultipleItem.validateFooterAndInfoView();
        initCheckedAdapter();
        resetPageAndSearch();
    }

    private void initCheckedAdapter() {
        if (productListPickerViewModels == null) {
            return;
        }
        for (ProductListPickerViewModel productListPickerViewModel : productListPickerViewModels) {
            ((ProductListPickerSearchAdapter) adapter).setChecked(productListPickerViewModel.getId(), true);
            productListPickerMultipleItem.addItemFromSearch(productListPickerViewModel);
        }
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        ((BaseMultipleCheckListAdapter<ProductListPickerViewModel>) adapter).setCheckedCallback(this);
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductListComponent.builder()
                .productListModule(new ProductListModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productListPickerSearchPresenter.attachView(this);
    }

    @Override
    public void onItemClicked(ProductListPickerViewModel productListPickerViewModel) {
        //do nothing, already handled
    }

    @Override
    public void onSearchSubmitted(String text) {
        this.keywordFilter = text;
        resetPageAndSearch();
    }

    @Override
    public void onSearchTextChanged(String text) {
        this.keywordFilter = text;
        resetPageAndSearch();
    }

    @Override
    public void resetPageAndSearch() {
        super.resetPageAndSearch();

    }

    @Override
    protected BaseListAdapter<ProductListPickerViewModel> getNewAdapter() {
        return new ProductListPickerSearchAdapter();
    }

    @Override
    protected void searchForPage(int page) {
        productListPickerSearchPresenter.getProductList(page, keywordFilter);
        hasNextPage = false;
    }

    @Override
    public void onErrorGetProductList(Throwable e) {
        onLoadSearchError(e);
        productListPickerMultipleItem.validateFooterAndInfoView();
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        productListPickerMultipleItem.validateFooterAndInfoView();
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        productListPickerMultipleItem.validateFooterAndInfoView();
    }

    @Override
    public void onLoadSearchError(Throwable t) {
        super.onLoadSearchError(t);
        if (adapter.getDataSize() < 1) {
            showSearchView(false);
        }
    }

    @Override
    public void onSuccessGetProductList(ProductListSellerModelView productListSellerModelView) {
        onSearchLoaded(productListSellerModelView.getProductListPickerViewModels(), productListSellerModelView.getProductListPickerViewModels().size());
        hasNextPage = productListSellerModelView.isHasNextPage();
    }

    @Override
    protected void showViewList(@NonNull List<ProductListPickerViewModel> list) {
        super.showViewList(list);
        showSearchView(true);
        productListPickerMultipleItem.validateFooterAndInfoView();
    }

    @Override
    protected boolean hasNextPage() {
        return hasNextPage;
    }

    @Override
    public void onItemChecked(ProductListPickerViewModel productListPickerViewModel, boolean checked) {
        if (checked) {
            if (productListPickerMultipleItem.allowAddItem(productListPickerViewModel)) {
                productListPickerMultipleItem.addItemFromSearch(productListPickerViewModel);
            } else {
                ((ProductListPickerSearchAdapter) adapter).setChecked(productListPickerViewModel.getId(), false);
                adapter.notifyDataSetChanged();
            }
        } else {
            productListPickerMultipleItem.removeItemFromSearch(productListPickerViewModel);
        }
    }

    @Override
    public void deselectItem(ProductListPickerViewModel productListPickerViewModel) {
        ((BaseMultipleCheckListAdapter<ProductListPickerViewModel>) adapter).setChecked(String.valueOf(productListPickerViewModel.getId()), false);
        adapter.notifyDataSetChanged();
    }

    public List<ProductListPickerViewModel> getItemList() {
        return adapter.getData();
    }

    @Override
    protected EmptyDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_variant_empty);
        emptyDataBinder.setEmptyTitleText(getString(R.string.title_no_result));
        emptyDataBinder.setEmptyContentText(getString(R.string.product_picker_empty_search_description));
        return emptyDataBinder;
    }
}