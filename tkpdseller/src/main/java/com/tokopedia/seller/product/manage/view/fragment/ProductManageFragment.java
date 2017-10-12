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
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.seller.BuildConfig;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
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
import com.tokopedia.seller.product.picker.view.adapter.ProductListPickerSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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

    private BroadcastReceiver addProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TkpdState.ProductService.BROADCAST_ADD_PRODUCT)) {
                if (intent.hasExtra(TkpdState.ProductService.STATUS_FLAG)) {
                    if (intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0) ==
                            TkpdState.ProductService.STATUS_DONE) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resetPageAndRefresh();
                            }
                        });
                    }
                }
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
                    return true;
                }
            });
            item.getSubMenu().findItem(R.id.label_view_added_from_gallery).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onAddFromGallery();
                    return true;
                }
            });
            item.getSubMenu().findItem(R.id.label_view_import_from_instagram).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    importFromInstagram();
                    return false;
                }
            });
        } else if (itemId == R.id.checklist_product_menu) {
            getActivity().startActionMode(getCallbackActionMode());
        }
        return super.onOptionsItemSelected(item);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onAddFromGallery() {
        GalleryActivity.moveToImageGalleryCamera(getActivity(), this, DEFAULT_IMAGE_GALLERY_POSITION,
                false, MAX_NUMBER_IMAGE_SELECTED_FROM_GALLERY);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void onAddFromCamera() {
        GalleryActivity.moveToImageGalleryCamera(getActivity(), this, DEFAULT_IMAGE_GALLERY_POSITION,
                true, MAX_NUMBER_IMAGE_SELECTED_FROM_CAMERA);
    }

    public void importFromInstagram() {
        if (getActivity().getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getActivity().getApplication()).startInstopedActivityForResult(getActivity(), this,
                    GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE, MAX_NUMBER_IMAGE_SELECTED_FROM_CAMERA);
        }
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
                    });
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
            case GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    onActivityResultFromInstagram(intent);
                }
                break;
            case ProductManageConstant.REQUEST_CODE_FILTER:
                if (resultCode == Activity.RESULT_OK) {
                    productManageFilterModel = intent.getParcelableExtra(ProductManageConstant.EXTRA_FILTER_SELECTED);
                    resetPageAndRefresh();
                    filtered = true;
                    setSearchMode(true);
                }
                break;
            case ProductManageConstant.REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    ProductManageSortModel productManageSortModel = intent.getParcelableExtra(ProductManageConstant.EXTRA_SORT_SELECTED);
                    sortProductOption = productManageSortModel.getId();
                    resetPageAndRefresh();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    protected void resetPageAndRefresh() {
        resetPageAndSearch();
        swipeToRefresh.setRefreshing(true);
    }

    private void onActivityResultFromGallery(Intent intent) {
        if (intent == null) {
            return;
        }
        int position = intent.getIntExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION, GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
        String imageUrl = intent.getStringExtra(GalleryActivity.IMAGE_URL);
        if (!TextUtils.isEmpty(imageUrl)) {
            ArrayList<String> imageUrls = new ArrayList<>();
            imageUrls.add(imageUrl);
            ProductAddActivity.start(getActivity(), imageUrls);
        }

        ArrayList<String> imageUrls = intent.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
        if (imageUrls != null) {
            ProductAddActivity.start(getActivity(), imageUrls);
        }
    }

    private void onActivityResultFromInstagram(Intent intent) {
        List<InstagramMediaModel> images = intent.getParcelableArrayListExtra(GalleryActivity.PRODUCT_SOC_MED_DATA);

        ArrayList<String> standardResoImageUrlList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            standardResoImageUrlList.add(images.get(i).standardResolution);
        }
        showLoadingProgress();
        ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(getActivity());
        imageDownloadHelper.convertHttpPathToLocalPath(standardResoImageUrlList, false,
                new ImageDownloadHelper.OnImageDownloadListener() {
                    @Override
                    public void onError(Throwable e) {
                        hideLoadingProgress();
                        CommonUtils.UniversalToast(getActivity(), ErrorHandler.getErrorMessage(e, getActivity()));
                    }

                    @Override
                    public void onSuccess(ArrayList<String> resultLocalPaths) {
                        showLoadingProgress();
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(GalleryActivity.IMAGE_URLS, resultLocalPaths);
                        ProductAddActivity.start(getActivity(), resultLocalPaths);
                        getActivity().finish();
                    }
                });
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
        }
    }

    @Override
    public void onItemChecked(ProductManageViewModel productManageViewModel, boolean checked) {
        if (actionMode != null) {
            int totalChecked = ((ProductManageListAdapter) adapter).getTotalChecked();
            actionMode.setTitle(String.valueOf(totalChecked));
            MenuItem deleteMenuItem = actionMode.getMenu().findItem(R.id.delete_product_menu);
            deleteMenuItem.setVisible(totalChecked > 0);
        }else{
            ((ProductManageListAdapter) adapter).setChecked(productManageViewModel.getId(), checked);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNextPage) {
        onSearchLoaded(list, totalItem);
        this.hasNextPage = hasNextPage;
    }

    @Override
    public void onSuccessLoadGoldMerchantFlag(boolean goldMerchant) {
        this.goldMerchant = goldMerchant;
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
                ViewUtils.getErrorMessage(getActivity(), t), new NetworkErrorHelper.RetryClickedListener() {
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
                ViewUtils.getErrorMessage(getActivity(), t), new NetworkErrorHelper.RetryClickedListener() {
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
                ViewUtils.getErrorMessage(getActivity(), t), new NetworkErrorHelper.RetryClickedListener() {
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
        if (addProductReceiver.isOrderedBroadcast()) getActivity().unregisterReceiver(addProductReceiver);
    }

    @Override
    public void onClickOptionItem(ProductManageViewModel productManageViewModel) {
        showActionProductDialog(productManageViewModel);
    }

    @Override
    public boolean isActionModeActive() {
        return actionMode!=null;
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
                    final List<String> productIdList = new ArrayList<>();
                    productIdList.add(productManageViewModel.getId());
                    showDialogActionDeleteProduct(productIdList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            productManagePresenter.deleteProduct(productIdList);
                        }
                    });
                } else if (itemId == R.id.change_price_product_menu) {
                    showDialogChangeProductPrice(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(), productManageViewModel.getProductCurrencyId());
                } else if (itemId == R.id.share_product_menu) {
                    goToShareProduct(productManageViewModel);
                } else if (itemId == R.id.set_cashback_product_menu) {
                    if (goldMerchant) {
                        showOptionCashback(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(), productManageViewModel.getProductCurrencySymbol());
                    } else {
                        showDialogActionGoToGMSubscribe();
                    }
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
                .addItem(CashbackOption.CASHBACK_OPTION_NONE, getString(R.string.product_manage_cashback_option_none), null);

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

    private void showDialogChangeProductPrice(final String productId, String productPrice, @CurrencyTypeDef int productCurrencyId) {
        ProductManageEditPriceDialogFragment productManageEditPriceDialogFragment =
                ProductManageEditPriceDialogFragment.createInstance(productId, productPrice, productCurrencyId, goldMerchant);
        productManageEditPriceDialogFragment.setListenerDialogEditPrice(new ProductManageEditPriceDialogFragment.ListenerDialogEditPrice() {
            @Override
            public void onSubmitEditPrice(String productId, String price, String currencyId, String currencyText) {
                productManagePresenter.editPrice(productId, price, currencyId, currencyText);
            }
        });
        productManageEditPriceDialogFragment.show(getActivity().getFragmentManager(), "");
    }

    private void showDialogActionDeleteProduct(final List<String> productIdList, Dialog.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.label_delete);
        alertDialog.setMessage(R.string.dialog_delete_product);
        alertDialog.setPositiveButton(R.string.label_delete, onClickListener);
        alertDialog.setNegativeButton(R.string.title_cancel, null);
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