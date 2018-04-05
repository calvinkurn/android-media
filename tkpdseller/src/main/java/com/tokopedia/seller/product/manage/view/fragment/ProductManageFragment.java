package com.tokopedia.seller.product.manage.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseRetryDataBinder;
import com.tokopedia.seller.base.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.seller.common.imageeditor.GalleryCropActivity;
import com.tokopedia.seller.common.imageeditor.GalleryCropWatermarkActivity;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.seller.instoped.InstopedSellerCropWatermarkActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductDuplicateActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductEditActivity;
import com.tokopedia.seller.product.manage.constant.CashbackOption;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.constant.StatusProductOption;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;

import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManageFragment extends BaseSearchListFragment<ProductManagePresenter, ProductManageViewModel>
        implements ProductManageView, ProductManageListAdapter.ClickOptionCallback, BaseMultipleCheckListAdapter.CheckedCallback<ProductManageViewModel> {

    private static final int MAX_NUMBER_IMAGE_SELECTED_FROM_GALLERY = 5;
    private static final int MAX_NUMBER_IMAGE_SELECTED_FROM_CAMERA = -1;
    private static final int DEFAULT_IMAGE_GALLERY_POSITION = 0;

    @Inject
    ProductManagePresenter productManagePresenter;
    private BottomActionView bottomActionView;
    private ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;

    private boolean hasNextPage;
    private boolean filtered;
    @SortProductOption
    private String sortProductOption;
    private ProductManageFilterModel productManageFilterModel;
    private ActionMode actionMode;
    private Boolean goldMerchant;
    private boolean isOfficialStore;

    private BroadcastReceiver addProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TkpdState.ProductService.BROADCAST_ADD_PRODUCT) &&
                    intent.hasExtra(TkpdState.ProductService.STATUS_FLAG) &&
                    intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0) == TkpdState.ProductService.STATUS_DONE) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetPageAndRefresh();
                    }
                });

            }
        }
    };

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
        searchInputView.clearFocus();
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
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
    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_variant_empty);
        emptyDataBinder.setEmptyTitleText(getString(R.string.title_no_result));
        emptyDataBinder.setEmptyContentText(getString(R.string.product_manage_label_change_search));
        return emptyDataBinder;
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_empty_featured_product);
        emptyDataBinder.setEmptyTitleText(getString(R.string.product_manage_label_product_list_empty));
        emptyDataBinder.setEmptyContentText(getString(R.string.product_manage_label_add_product_to_sell));
        emptyDataBinder.setEmptyButtonItemText(getString(R.string.product_manage_label_add_product));
        emptyDataBinder.setCallback(new BaseEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {
                // do nothing
            }

            @Override
            public void onEmptyButtonClicked() {
                startActivity(ProductAddActivity.getCallingIntent(getActivity()));
            }
        });
        return emptyDataBinder;
    }

    @Override
    public RetryDataBinder getRetryViewDataBinder(BaseListAdapter adapter) {
        return new BaseRetryDataBinder(adapter, R.drawable.ic_cloud_error);
    }

    @Override
    public void onSearchSubmitted(String text) {
        UnifyTracking.eventProductManageSearch();
        super.onSearchSubmitted(text);
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
            item.getSubMenu().findItem(R.id.label_view_added_from_camera).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onAddFromCamera();
                    UnifyTracking.eventProductManageTopNav(item.getTitle().toString());
                    return true;
                }
            });
            item.getSubMenu().findItem(R.id.label_view_added_from_gallery).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onAddFromGallery();
                    UnifyTracking.eventProductManageTopNav(item.getTitle().toString());
                    return true;
                }
            });
            item.getSubMenu().findItem(R.id.label_view_import_from_instagram).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    importFromInstagram();
                    UnifyTracking.eventProductManageTopNav(item.getTitle().toString());
                    return false;
                }
            });
        } else if (itemId == R.id.checklist_product_menu) {
            UnifyTracking.eventProductManageTopNav(item.getTitle().toString());
            getActivity().startActionMode(getCallbackActionMode());
        }
        return super.onOptionsItemSelected(item);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onAddFromGallery() {
        GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), this, DEFAULT_IMAGE_GALLERY_POSITION,
                false, MAX_NUMBER_IMAGE_SELECTED_FROM_GALLERY, true);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onAddFromCamera() {
        GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), this, DEFAULT_IMAGE_GALLERY_POSITION,
                true, MAX_NUMBER_IMAGE_SELECTED_FROM_CAMERA, true);
    }

    public void importFromInstagram() {
        InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), ProductManageFragment.this,
                INSTAGRAM_SELECT_REQUEST_CODE, ProductManageSellerFragment.MAX_INSTAGRAM_SELECT);
        UnifyTracking.eventClickInstoped();
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
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.delete_product_menu) {
                    final List<String> productIdList = ((ProductManageListAdapter) adapter).getListChecked();
                    showDialogActionDeleteProduct(productIdList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mode.finish();
                            productManagePresenter.deleteProduct(productIdList);
                        }
                    }, null);
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case ImageGallery.TOKOPEDIA_GALLERY:
                onActivityResultFromGallery(intent);
                break;
            case ProductManageConstant.REQUEST_CODE_FILTER:
                if (resultCode == Activity.RESULT_OK) {
                    productManageFilterModel = intent.getParcelableExtra(ProductManageConstant.EXTRA_FILTER_SELECTED);
                    resetPageAndRefresh();
                    filtered = true;
                    setSearchMode(true);
                    trackingFilter(productManageFilterModel);
                }
                break;
            case ProductManageConstant.REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    ProductManageSortModel productManageSortModel = intent.getParcelableExtra(ProductManageConstant.EXTRA_SORT_SELECTED);
                    sortProductOption = productManageSortModel.getId();
                    resetPageAndRefresh();
                    trackingSort(productManageSortModel);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, intent);
                break;
        }
    }

    private void trackingSort(ProductManageSortModel productManageSortModel) {
        UnifyTracking.eventProductManageSortProduct(productManageSortModel.getTitleSort());
    }

    private void trackingFilter(ProductManageFilterModel productManageFilterModel) {
        List<String> filters = new ArrayList<>();
        if (!productManageFilterModel.getCategoryId().equals(String.valueOf(ProductManageConstant.FILTER_ALL_CATEGORY))) {
            filters.add(AppEventTracking.EventLabel.CATEGORY);
        }

        if (productManageFilterModel.getEtalaseProductOption() != ProductManageConstant.FILTER_ALL_PRODUK) {
            filters.add(AppEventTracking.EventLabel.ETALASE);
        }

        if (!productManageFilterModel.getCatalogProductOption().equals(CatalogProductOption.WITH_AND_WITHOUT)) {
            filters.add(AppEventTracking.EventLabel.CATALOG);
        }

        if (!productManageFilterModel.getConditionProductOption().equals(ConditionProductOption.ALL_CONDITION)) {
            filters.add(AppEventTracking.EventLabel.CONDITION);
        }

        if (!productManageFilterModel.getPictureStatusOption().equals(PictureStatusProductOption.WITH_AND_WITHOUT)) {
            filters.add(AppEventTracking.EventLabel.PICTURE_STATUS);
        }

        UnifyTracking.eventProductManageFilterProduct(TextUtils.join(",", filters));
    }

    protected void resetPageAndRefresh() {
        resetPageAndSearch();
        swipeToRefresh.setRefreshing(true);
    }

    private void onActivityResultFromGallery(Intent intent) {
        if (intent == null) {
            return;
        }
        int position = intent.getIntExtra(GalleryCropActivity.ADD_PRODUCT_IMAGE_LOCATION, GalleryCropActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
        String imageUrl = intent.getStringExtra(GalleryCropActivity.IMAGE_URL);
        if (!TextUtils.isEmpty(imageUrl)) {
            ArrayList<String> imageUrls = new ArrayList<>();
            imageUrls.add(imageUrl);
            ProductAddActivity.start(getActivity(), imageUrls);
        } else {
            ArrayList<String> imageUrls = intent.getStringArrayListExtra(GalleryCropActivity.IMAGE_URLS);
            if (imageUrls != null) {
                ProductAddActivity.start(getActivity(), imageUrls);
            }
        }
    }

    @Override
    protected void setSearchMode(boolean searchMode) {
        if (filtered) {
            super.setSearchMode(true);
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
    protected void onPullToRefresh() {
        goldMerchant = null;
        ((ProductManageListAdapter) adapter).setFeaturedProduct(null);
        super.onPullToRefresh();
    }

    @Override
    protected void searchForPage(int page) {
        if (goldMerchant == null) {
            productManagePresenter.getGoldMerchantStatus();
        }
        if (((ProductManageListAdapter) adapter).getFeaturedProduct() == null) {
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
            ((ProductManageListAdapter) adapter).setChecked(productManageViewModel.getId(), false);
            adapter.notifyDataSetChanged();
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(getActivity(), productManageViewModel.getProductUrl());
            UnifyTracking.eventProductManageClickDetail();
        }
    }

    @Override
    public void onItemChecked(ProductManageViewModel productManageViewModel, boolean checked) {
        if (checked && productManageViewModel.getProductVariant() == 1) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), getString(R.string.product_manage_label_snackbar_variant));
            ((ProductManageListAdapter) adapter).setChecked(productManageViewModel.getId(), false);
            adapter.notifyDataSetChanged();
        } else {
            if (actionMode != null) {
                int totalChecked = ((ProductManageListAdapter) adapter).getTotalChecked();
                actionMode.setTitle(String.valueOf(totalChecked));
                MenuItem deleteMenuItem = actionMode.getMenu().findItem(R.id.delete_product_menu);
                deleteMenuItem.setVisible(totalChecked > 0);
            } else {
                ((ProductManageListAdapter) adapter).setChecked(productManageViewModel.getId(), checked);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNextPage) {
        onSearchLoaded(list, totalItem);
        this.hasNextPage = hasNextPage;
    }

    @Override
    public void onSuccessGetShopInfo(boolean goldMerchant, boolean officialStore) {
        this.goldMerchant = goldMerchant;
        isOfficialStore = officialStore;
    }

    @Override
    public void onSuccessGetFeaturedProductList(List<String> data) {
        ((ProductManageListAdapter) adapter).setFeaturedProduct(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessEditPrice(String productId, String price, String currencyId, String currencyText) {
        ((ProductManageListAdapter) adapter).updatePrice(productId, price, currencyId, currencyText);
    }

    @Override
    public void onErrorEditPrice(Throwable t, final String productId, final String price, final String currencyId, final String currencyText) {
        NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout,
                ViewUtils.getErrorMessage(getActivity(), t), Snackbar.LENGTH_LONG, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        productManagePresenter.editPrice(productId, price, currencyId, currencyText);
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void onSuccessSetCashback(String productId, int cashback) {
        ((ProductManageListAdapter) adapter).updateCashback(productId, cashback);
    }

    @Override
    public void onErrorSetCashback(Throwable t, final String productId, final int cashback) {
        NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout,
                ViewUtils.getErrorMessage(getActivity(), t), Snackbar.LENGTH_LONG, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        productManagePresenter.setCashback(productId, cashback);
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void onSuccessMultipleDeleteProduct() {
        resetPageAndSearch();
    }

    @Override
    public void onErrorMultipleDeleteProduct(Throwable t, List<String> productIdDeletedList, final List<String> productIdFailToDeleteList) {
        if (productIdDeletedList.size() > 0) {
            resetPageAndSearch();
        }
        NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout,
                ViewUtils.getErrorMessage(getActivity(), t), Snackbar.LENGTH_LONG, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        productManagePresenter.deleteProduct(productIdFailToDeleteList);
                    }
                }).showRetrySnackbar();
    }

    @Override
    protected boolean hasNextPage() {
        return hasNextPage;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(addProductReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        getActivity().registerReceiver(addProductReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productManagePresenter.detachView();
        if (addProductReceiver.isOrderedBroadcast()) {
            getActivity().unregisterReceiver(addProductReceiver);
        }
    }

    @Override
    public void onClickOptionItem(ProductManageViewModel productManageViewModel) {
        showActionProductDialog(productManageViewModel);
    }

    @Override
    public boolean isActionModeActive() {
        return actionMode != null;
    }

    @Override
    public void showLoadingProgress() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingProgress() {
        progressDialog.hide();
    }

    private void showActionProductDialog(ProductManageViewModel productManageViewModel) {
        CommonUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());

        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(productManageViewModel.getProductName());
        bottomSheetBuilder.setMenu(R.menu.menu_product_manage_action_item);
        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionBottomSheetClicked(productManageViewModel))
                .createDialog();
        bottomSheetDialog.show();
    }

    @NonNull
    private BottomSheetItemClickListener onOptionBottomSheetClicked(final ProductManageViewModel productManageViewModel) {
        return new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(final MenuItem item) {
                if (productManageViewModel.getProductStatus().equals(StatusProductOption.UNDER_SUPERVISION)) {
                    NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.product_manage_desc_product_on_supervision, productManageViewModel.getProductName()));
                    return;
                }
                int itemId = item.getItemId();
                if (itemId == R.id.edit_product_menu) {
                    goToEditProduct(productManageViewModel.getId());
                    UnifyTracking.eventProductManageOverflowMenu(item.getTitle().toString());
                } else if (itemId == R.id.duplicat_product_menu) {
                    goToDuplicateProduct(productManageViewModel.getId());
                    UnifyTracking.eventProductManageOverflowMenu(item.getTitle().toString());
                } else if (itemId == R.id.delete_product_menu) {
                    final List<String> productIdList = new ArrayList<>();
                    productIdList.add(productManageViewModel.getId());
                    showDialogActionDeleteProduct(productIdList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UnifyTracking.eventProductManageOverflowMenu(item.getTitle().toString() + " - " + getString(R.string.label_delete));
                            productManagePresenter.deleteProduct(productIdList);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UnifyTracking.eventProductManageOverflowMenu(item.getTitle().toString() + " - " + getString(R.string.title_cancel));
                            dialog.dismiss();
                        }
                    });
                } else if (itemId == R.id.change_price_product_menu) {
                    if (productManageViewModel.isProductVariant()) {
                        showDialogVariantPriceLocked();
                    } else {
                        showDialogChangeProductPrice(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(), productManageViewModel.getProductCurrencyId());
                    }
                } else if (itemId == R.id.share_product_menu) {
                    goToShareProduct(productManageViewModel);
                } else if (itemId == R.id.set_cashback_product_menu) {
                    onSetCashbackClicked(productManageViewModel);
                }
            }
        };
    }

    private void showDialogVariantPriceLocked(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                .setTitle(getString(R.string.product_price_locked))
                .setMessage(getString(R.string.product_price_locked_manage_desc))
                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // no op, just dismiss
                    }
                });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    private void onSetCashbackClicked(ProductManageViewModel productManageViewModel) {
        if (goldMerchant == null) {
            return;
        }
        if (!GlobalConfig.isSellerApp() && getActivity().getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) getActivity().getApplication()).goToGMSubscribe(getActivity());
            return;
        }
        if (goldMerchant) {
            showOptionCashback(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(),
                    productManageViewModel.getProductCurrencySymbol(), productManageViewModel.getProductCashback());
        } else {
            showDialogActionGoToGMSubscribe();
        }
    }

    private void showOptionCashback(String productId, String productPrice, String productPriceSymbol, int productCashback) {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(getString(R.string.product_manage_cashback_title));

        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_3);
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_4);
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_5);
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_NONE);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionCashbackClicked(productId))
                .createDialog();
        bottomSheetDialog.show();
    }

    private void addCashbackBottomSheetItemMenu(BottomSheetBuilder bottomSheetBuilder,
                                                String productPrice, String productPriceSymbol, int productCashback, @CashbackOption int cashbackOption) {
        if (bottomSheetBuilder instanceof CheckedBottomSheetBuilder) {
            double productPricePlain = Double.parseDouble(productPrice);
            ((CheckedBottomSheetBuilder) bottomSheetBuilder).addItem(cashbackOption,
                    getCashbackMenuText(cashbackOption, productPriceSymbol, productPricePlain), null, productCashback == cashbackOption);
        }
    }

    private String getCashbackMenuText(int cashback, String productPriceSymbol, double productPricePlain) {
        String cashbackText = getString(R.string.product_manage_cashback_option_none);
        if (cashback > 0) {
            cashbackText = getString(R.string.product_manage_cashback_option, String.valueOf(cashback),
                    productPriceSymbol,
                    KMNumbers.formatDouble2PCheckRound(((double) cashback * productPricePlain / 100f), !productPriceSymbol.equals("Rp")));
        }
        return cashbackText;
    }

    private BottomSheetItemClickListener onOptionCashbackClicked(final String productId) {
        return new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case CashbackOption.CASHBACK_OPTION_3:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_3);
                        break;
                    case CashbackOption.CASHBACK_OPTION_4:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_4);
                        break;
                    case CashbackOption.CASHBACK_OPTION_5:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_5);
                        break;
                    case CashbackOption.CASHBACK_OPTION_NONE:
                        productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_NONE);
                        break;
                    default:
                        break;
                }
                UnifyTracking.eventProductManageOverflowMenu(getString(R.string.product_manage_cashback_title) + " - " + item.getTitle());
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
                .setId(productManageViewModel.getProductId())
                .build();
        Intent intent = ShareActivity.createIntent(getActivity(), shareData);
        startActivity(intent);
    }

    private void showDialogChangeProductPrice(final String productId, String productPrice, @CurrencyTypeDef int productCurrencyId) {
        if (!isAdded() || goldMerchant == null) {
            return;
        }
        ProductManageEditPriceDialogFragment productManageEditPriceDialogFragment =
                ProductManageEditPriceDialogFragment.createInstance(productId, productPrice, productCurrencyId, goldMerchant, isOfficialStore);
        productManageEditPriceDialogFragment.setListenerDialogEditPrice(new ProductManageEditPriceDialogFragment.ListenerDialogEditPrice() {
            @Override
            public void onSubmitEditPrice(String productId, String price, String currencyId, String currencyText) {
                productManagePresenter.editPrice(productId, price, currencyId, currencyText);
            }
        });
        productManageEditPriceDialogFragment.show(getActivity().getFragmentManager(), "");
    }

    private void showDialogActionDeleteProduct(final List<String> productIdList, Dialog.OnClickListener onClickListener, Dialog.OnClickListener onCancelListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.label_delete);
        alertDialog.setMessage(R.string.dialog_delete_product);
        alertDialog.setPositiveButton(R.string.label_delete, onClickListener);
        alertDialog.setNegativeButton(R.string.title_cancel, onCancelListener);
        alertDialog.show();
    }

    private void showDialogActionGoToGMSubscribe() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.product_manage_cashback_title);
        alertDialog.setMessage(R.string.product_manage_cashback_not_subscribe_message);
        alertDialog.setPositiveButton(R.string.label_subscribe, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getActivity().getApplication() instanceof SellerModuleRouter) {
                    ((SellerModuleRouter) getActivity().getApplication()).goToGMSubscribe(getActivity());
                }
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