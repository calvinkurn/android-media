package com.tokopedia.seller.product.manage.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.product.manage.view.listener.ManageProductView;
import com.tokopedia.seller.product.manage.view.model.ProductListManageModelView;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;
import com.tokopedia.seller.product.manage.view.presenter.ManageProductPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ManageProductFragment extends BaseSearchListFragment<ManageProductPresenter, ProductManageViewModel> implements ManageProductView {

    @Inject
    ManageProductPresenter manageProductPresenter;

    @Override
    protected void initInjector() {
        super.initInjector();
        manageProductPresenter.attachView(this);
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    protected BaseListAdapter<ProductManageViewModel> getNewAdapter() {
        return null;
    }

    @Override
    protected void searchForPage(int page) {
        manageProductPresenter.getListProduct();
    }

    @Override
    public void onItemClicked(ProductManageViewModel productManageViewModel) {

    }

    @Override
    public void onErrorEditPrice() {

    }

    @Override
    public void onSuccessEditPrice() {

    }

    @Override
    public void onSuccessDeleteProduct() {

    }

    @Override
    public void onErrorDeleteProduct() {

    }

    @Override
    public void onGetProductList(ProductListManageModelView transform) {

    }

    @Override
    public void onErrorGetProductList(Throwable e) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manageProductPresenter.detachView();
    }
}
