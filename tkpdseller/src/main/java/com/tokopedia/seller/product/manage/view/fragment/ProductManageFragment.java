package com.tokopedia.seller.product.manage.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.product.model.share.ShareData;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductDuplicateActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductEditActivity;
import com.tokopedia.seller.product.manage.constant.CashbackOption;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.di.DaggerProductManageComponent;
import com.tokopedia.seller.product.manage.di.ProductManageModule;
import com.tokopedia.seller.product.manage.view.activity.ProductManageFilterActivity;
import com.tokopedia.seller.product.manage.view.activity.ProductManageSortActivity;
import com.tokopedia.seller.product.manage.view.adapter.ProductManageListAdapter;
import com.tokopedia.seller.product.manage.view.listener.ProductManageView;
import com.tokopedia.seller.product.manage.view.model.ProductManageFilterModel;
import com.tokopedia.seller.product.manage.view.model.ProductManageSortModel;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;
import com.tokopedia.seller.product.manage.view.presenter.ProductManagePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManageFragment extends BaseSearchListFragment<ProductManagePresenter, ProductManageViewModel>
        implements ProductManageView, ProductManageListAdapter.ClickOptionCallback, BaseMultipleCheckListAdapter.CheckedCallback<ProductManageViewModel> {

    @Inject
    ProductManagePresenter productManagePresenter;
    private BottomActionView bottomActionView;
    private ProgressDialog progressDialog;

    private boolean hasNextPage;
    private boolean filtered;
    @SortProductOption
    private String sortProductOption;
    private ProductManageFilterModel productManageFilterModel;
    private ActionMode actionMode;

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductManageComponent.builder()
                .productManageModule(new ProductManageModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productManagePresenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_manage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        bottomActionView = (BottomActionView) view.findViewById(R.id.bottom_action_view);
        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductManageSortActivity.createIntent(getActivity(), sortProductOption);
                startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_SORT);
            }
        });
        bottomActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductManageFilterActivity.createIntent(getActivity(), productManageFilterModel);
                startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_FILTER);
            }
        });
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        sortProductOption = SortProductOption.POSITION;
        productManageFilterModel = new ProductManageFilterModel();
        productManageFilterModel.reset();
        hasNextPage = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product_manage, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_product_menu) {
            startActivity(ProductAddActivity.getCallingIntent(getActivity()));
        } else if (itemId == R.id.checklist_product_menu) {
            getActivity().startActionMode(getCallbackActionMode());
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    protected ActionMode.Callback getCallbackActionMode() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(String.valueOf(((ProductManageListAdapter) adapter).getTotalChecked()));
                actionMode = mode;
                getActivity().getMenuInflater().inflate(R.menu.menu_product_manage_action_mode, menu);
                setAdapterActionMode(true);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.delete_product_menu) {
                    productManagePresenter.deleteListProduct(((ProductManageListAdapter) adapter).getListChecked());
                    mode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((ProductManageListAdapter) adapter).resetCheckedItemSet();
                setAdapterActionMode(false);
                actionMode = null;
            }
        };
    }

    protected void setAdapterActionMode(boolean isActionMode) {
        ((ProductManageListAdapter) adapter).setActionMode(isActionMode);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductManageConstant.REQUEST_CODE_FILTER:
                if (resultCode == Activity.RESULT_OK) {
                    productManageFilterModel = data.getParcelableExtra(ProductManageConstant.EXTRA_FILTER_SELECTED);
                    resetPageAndSearch();
                    swipeToRefresh.setRefreshing(true);
                    filtered = true;
                    setSearchMode(true);
                }
                break;
            case ProductManageConstant.REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    ProductManageSortModel productManageSortModel = data.getParcelableExtra(ProductManageConstant.EXTRA_SORT_SELECTED);
                    sortProductOption = productManageSortModel.getId();
                    resetPageAndSearch();
                    swipeToRefresh.setRefreshing(true);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void setSearchMode(boolean searchMode) {
        if (filtered) {
            super.setSearchMode(filtered);
            return;
        }
        super.setSearchMode(searchMode);
    }

    @Override
    protected void showSearchView(boolean show) {
        super.showSearchView(show);
        bottomActionView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected BaseListAdapter<ProductManageViewModel> getNewAdapter() {
        ProductManageListAdapter productManageListAdapter = new ProductManageListAdapter();
        productManageListAdapter.setClickOptionCallback(this);
        productManageListAdapter.setCheckedCallback(this);
        return productManageListAdapter;
    }

    @Override
    protected void searchForPage(int page) {
        if (page == getStartPage()) {
            productManagePresenter.getListFeaturedProduct();
        }
        productManagePresenter.getListProduct(page, searchInputView.getSearchText(),
                productManageFilterModel.getCatalogProductOption(), productManageFilterModel.getConditionProductOption(),
                productManageFilterModel.getEtalaseProductOption(), productManageFilterModel.getPictureStatusOption(),
                sortProductOption, productManageFilterModel.getCategoryId());
        hasNextPage = false;
    }

    @Override
    public void onItemClicked(ProductManageViewModel productManageViewModel) {
        if (actionMode == null) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(getActivity(), productManageViewModel.getProductUrl());
        }
    }

    @Override
    public void onItemChecked(ProductManageViewModel productManageViewModel, boolean checked) {
        if (actionMode != null) {
            actionMode.setTitle(String.valueOf(((ProductManageListAdapter) adapter).getTotalChecked()));
        }
    }

    @Override
    public void onErrorEditPrice() {

    }

    @Override
    public void onSuccessEditPrice() {
        resetPageAndSearch();
    }

    @Override
    public void onSuccessDeleteProduct() {
        resetPageAndSearch();
    }

    @Override
    public void onErrorDeleteProduct() {

    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNext) {
        onSearchLoaded(list, totalItem);
        this.hasNextPage = hasNext;
    }

    @Override
    protected boolean hasNextPage() {
        return hasNextPage;
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
        ((ProductManageListAdapter) adapter).setFeaturedProduct(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetFeaturedProductList() {

    }

    @Override
    public void onErrorSetCashback() {

    }

    @Override
    public void onSuccessSetCashback() {
        resetPageAndSearch();
    }

    @Override
    public void onErrorMultipleDeleteProduct(int countOfSuccess, int countOfError) {
        resetPageAndSearch();
    }

    @Override
    public void onSuccessMultipleDeleteProduct(int countOfSuccess, int countOfError) {
        resetPageAndSearch();
    }

    @Override
    public void onErrorMultipleDeleteProduct(Throwable e) {

    }

    private void showActionProductDialog(ProductManageViewModel productManageViewModel) {
        CommonUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());

        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(productManageViewModel.getProductName())
                .setMenu(R.menu.menu_product_manage_action_item);

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
                if (itemId == R.id.edit_product_menu) {
                    goToEditProduct(productManageViewModel.getId());
                } else if (itemId == R.id.duplicat_product_menu) {
                    goToDuplicateProduct(productManageViewModel.getId());
                } else if (itemId == R.id.delete_product_menu) {
                    showDialogActionDeleteProduct(productManageViewModel.getId());
                } else if (itemId == R.id.change_price_product_menu) {
                    showDialogChangeProductPrice(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(), productManageViewModel.getProductCurrencyId());
                } else if (itemId == R.id.share_product_menu) {
                    goToShareProduct(productManageViewModel);
                } else if (itemId == R.id.set_cashback_product_menu) {
                    showOptionCashback(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(), productManageViewModel.getProductCurrencySymbol());
                }
            }
        };
    }

    private void showOptionCashback(String productId, String productPrice, String productPriceSymbol) {
        double productPricePlain = Double.parseDouble(productPrice);

        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(getString(R.string.product_manage_cashback_title))
                .addItem(CashbackOption.CASHBACK_OPTION_3, getCashbackMenuText(CashbackOption.CASHBACK_OPTION_3, productPriceSymbol, productPricePlain), null)
                .addItem(CashbackOption.CASHBACK_OPTION_4, getCashbackMenuText(CashbackOption.CASHBACK_OPTION_4, productPriceSymbol, productPricePlain), null)
                .addItem(CashbackOption.CASHBACK_OPTION_5, getCashbackMenuText(CashbackOption.CASHBACK_OPTION_5, productPriceSymbol, productPricePlain), null)
                .addItem(CashbackOption.WITHOUT_CASHBACK_OPTION, getString(R.string.product_manage_cashback_option_none), null);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionCashbackClicked(productId))
                .createDialog();
        bottomSheetDialog.show();
    }

    private String getCashbackMenuText(int cashback, String productPriceSymbol, double productPricePlain) {
        return getString(R.string.product_manage_cashback_option, String.valueOf(cashback),
                productPriceSymbol, (int) ((cashback / 100.0f) * productPricePlain));
    }

    private BottomSheetItemClickListener onOptionCashbackClicked(final String productId) {
        return new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case CashbackOption.CASHBACK_OPTION_3:
                        productManagePresenter.setCashback(productId, String.valueOf(CashbackOption.CASHBACK_OPTION_3));
                        break;
                    case CashbackOption.CASHBACK_OPTION_4:
                        productManagePresenter.setCashback(productId, String.valueOf(CashbackOption.CASHBACK_OPTION_4));
                        break;
                    case CashbackOption.CASHBACK_OPTION_5:
                        productManagePresenter.setCashback(productId, String.valueOf(CashbackOption.CASHBACK_OPTION_5));
                        break;
                    case CashbackOption.WITHOUT_CASHBACK_OPTION:
                        productManagePresenter.setCashback(productId, String.valueOf(CashbackOption.WITHOUT_CASHBACK_OPTION));
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void goToShareProduct(ProductManageViewModel productManageViewModel) {
        ShareData shareData = ShareData.Builder.aShareData()
                .setName(productManageViewModel.getProductName())
                .setDescription(productManageViewModel.getProductName())
                .setImgUri(productManageViewModel.getImageUrl())
                .setPrice(productManageViewModel.getProductPrice())
                .setUri(productManageViewModel.getProductUrl())
                .setType(ShareData.PRODUCT_TYPE)
                .build();
        Intent intent = ShareActivity.createIntent(getActivity(), shareData);
        startActivity(intent);
    }

    private void showDialogChangeProductPrice(final String productId, String productPrice, String productCurrencyId) {
        ProductManageEditPriceDialogFragment productManageEditPriceDialogFragment = ProductManageEditPriceDialogFragment.createInstance(productId, productPrice, productCurrencyId, false);
        productManageEditPriceDialogFragment.setListenerDialogEditPrice(new ProductManageEditPriceDialogFragment.ListenerDialogEditPrice() {
            @Override
            public void onSubmitEditPrice(String productId, String price, String priceCurrency) {
                productManagePresenter.editPrice(productId, price, priceCurrency);
            }
        });
        productManageEditPriceDialogFragment.show(getActivity().getFragmentManager(), "");
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