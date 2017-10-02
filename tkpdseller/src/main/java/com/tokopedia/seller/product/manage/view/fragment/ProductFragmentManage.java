package com.tokopedia.seller.product.manage.view.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.view.activity.ProductDuplicateActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductEditActivity;
import com.tokopedia.seller.product.manage.di.DaggerProductManageComponent;
import com.tokopedia.seller.product.manage.di.ProductManageModule;
import com.tokopedia.seller.product.manage.view.adapter.ProductManageListAdapter;
import com.tokopedia.seller.product.manage.view.listener.ProductManageView;
import com.tokopedia.seller.product.manage.view.model.ProductListManageModelView;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;
import com.tokopedia.seller.product.manage.view.presenter.ProductManagePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductFragmentManage extends BaseSearchListFragment<ProductManagePresenter, ProductManageViewModel>
        implements ProductManageView, ProductManageListAdapter.ClickOptionCallback {

    @Inject
    ProductManagePresenter productManagePresenter;

    private ProgressDialog progressDialog;

    private boolean hasNextPage;
    private String keywordFilter = "";

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductManageComponent.builder()
                .manageProductModule(new ProductManageModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productManagePresenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    public void onSearchSubmitted(String text) {
        keywordFilter = text;
        resetPageAndSearch();
    }

    @Override
    public void onSearchTextChanged(String text) {
        keywordFilter = text;
        resetPageAndSearch();
    }

    @Override
    protected BaseListAdapter<ProductManageViewModel> getNewAdapter() {
        ProductManageListAdapter productManageListAdapter = new ProductManageListAdapter();
        productManageListAdapter.setClickOptionCallback(this);
        return productManageListAdapter;
    }

    @Override
    protected void searchForPage(int page) {
        if(page == getStartPage()){
            productManagePresenter.getListFeaturedProduct();
        }
        productManagePresenter.getListProduct(page, keywordFilter);
        hasNextPage = false;
    }

    @Override
    public void onItemClicked(ProductManageViewModel productManageViewModel) {
        ((PdpRouter) getActivity().getApplication()).goToProductDetail(getActivity(), productManageViewModel.getProductUrl());
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
    public void onGetProductList(ProductListManageModelView productListManageModelView) {
        onSearchLoaded(productListManageModelView.getProductManageViewModels(), productListManageModelView.getProductManageViewModels().size());
        hasNextPage = productListManageModelView.isHasNextPage();
    }

    @Override
    protected boolean hasNextPage() {
        return hasNextPage;
    }

    @Override
    public void onErrorGetProductList(Throwable e) {
        onLoadSearchError(e);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productManagePresenter.detachView();
    }

    @Override
    public void onClickOptionItem(ProductManageViewModel productManageViewModel) {
        showActionProductDialog(productManageViewModel);
    }

    @Override
    public void showLoadingProgress() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingProgress() {
        progressDialog.hide();
    }

    @Override
    public void onGetFeaturedProductList(List<String> data) {
        ((ProductManageListAdapter)adapter).setFeaturedProduct(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetFeaturedProductList() {

    }

    private void showActionProductDialog(ProductManageViewModel productManageViewModel) {
        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_manage_product_action_item)
                .addTitleItem(productManageViewModel.getProductName());

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionBottomSheetClicked(productManageViewModel))
                .createDialog();
        bottomSheetDialog.show();
    }

    @NonNull
    private BottomSheetItemClickListener onOptionBottomSheetClicked(final ProductManageViewModel productManageViewModel) {
        return new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.edit_product_menu){
                    goToEditProduct(productManageViewModel.getId());
                }else if(itemId == R.id.duplicat_product_menu){
                    goToDuplicateProduct(productManageViewModel.getId());
                }else if(itemId == R.id.delete_product_menu){
                    showDialogActionDeleteProduct(productManageViewModel.getId());
                }else if(itemId == R.id.change_price_product_menu){
                    showDialogChangeProductPrice(productManageViewModel.getProductId(), productManageViewModel.getProductPrice(), productManageViewModel.getProductCurrencyId());
                }else if(itemId == R.id.share_product_menu){
                    goToShareProduct(productManageViewModel);
                }
            }
        };
    }

    private void goToShareProduct(ProductManageViewModel productManageViewModel) {

    }

    private void showDialogChangeProductPrice(final String productId, String productPrice, String productCurrencyId) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.layout_prompt_change_price_product, null);
        final SpinnerCounterInputView priceInputView = (SpinnerCounterInputView) promptsView.findViewById(R.id.spinner_counter_input_view_price);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(promptsView);
        alertDialog.setTitle(R.string.title_edit_price);
        alertDialog.setPositiveButton(R.string.title_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productManagePresenter.editPrice(productId, String.valueOf(priceInputView.getCounterValue()), priceInputView.getSpinnerValue());
            }
        });
        alertDialog.setNegativeButton(R.string.title_cancel, null);
        alertDialog.show();
    }

    private void showDialogActionDeleteProduct(final String productId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.label_delete);
        alertDialog.setMessage(R.string.dialog_delete_product);
        alertDialog.setPositiveButton(R.string.label_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productManagePresenter.deleteProduct(productId);
            }
        });
        alertDialog.setNegativeButton(R.string.title_cancel, null);
        alertDialog.show();
    }

    private void goToDuplicateProduct(String productId) {
        Intent intent = ProductDuplicateActivity.createInstance(getActivity(), productId);
        startActivity(intent);
    }

    private void goToEditProduct(String productId) {
        Intent intent = ProductEditActivity.createInstance(getActivity(), productId);
        startActivity(intent);
    }
}
