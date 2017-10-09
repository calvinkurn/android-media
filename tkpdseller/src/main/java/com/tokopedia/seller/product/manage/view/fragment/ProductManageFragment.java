package com.tokopedia.seller.product.manage.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
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
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductDuplicateActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductEditActivity;
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

    private static final int CASHBACK_OPTION_3 = 1;
    private static final int CASHBACK_OPTION_4 = 2;
    private static final int CASHBACK_OPTION_5 = 3;
    private static final int WITHOUT_CASHBACK_OPTION = 4;

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

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

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
            View view = getActivity().findViewById(R.id.add_product_menu);
            if (view != null) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getActivity()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);

                popupView.findViewById(R.id.label_view_added_from_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddFromCamera();
                    }
                });
                popupView.findViewById(R.id.label_view_added_from_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddFromGallery();
                    }
                });
                popupView.findViewById(R.id.label_view_import_from_instagram).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        importFromInstagram();
                    }
                });
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Rect location = locateView(view);
                popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, location.left, location.top);
            }
        } else if (itemId == R.id.checklist_product_menu) {
            getActivity().startActionMode(getCallbackActionMode());
        }
        return super.onOptionsItemSelected(item);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onAddFromGallery() {
        GalleryActivity.moveToImageGalleryCamera(getActivity(), this, 0, false, 5);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void onAddFromCamera() {
        GalleryActivity.moveToImageGalleryCamera(getActivity(), this, 0, true, -1);
    }

    public void importFromInstagram() {
        if (getActivity().getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getActivity().getApplication()).startInstopedActivityForResult(getActivity(), this,
                    GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE, 5);
        }

        // analytic below : https://phab.tokopedia.com/T18496
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
            case ImageGallery.TOKOPEDIA_GALLERY:
                if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
                    int position = data.getIntExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION, GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
                    String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
                    if (!TextUtils.isEmpty(imageUrl)) {
                        ArrayList<String> imageUrls = new ArrayList<>();
                        imageUrls.add(imageUrl);
                        ProductAddActivity.start(getActivity(), imageUrls);
                    }

                    ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
                    if (imageUrls != null) {
                        ProductAddActivity.start(getActivity(),
                                imageUrls);
                    }
                }
                break;
            case GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        List<InstagramMediaModel> images = data.getParcelableArrayListExtra(GalleryActivity.PRODUCT_SOC_MED_DATA);

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
                                        CommonUtils.UniversalToast(getActivity(),
                                                ErrorHandler.getErrorMessage(e, getActivity()));
                                    }

                                    @Override
                                    public void onSuccess(ArrayList<String> resultLocalPaths) {
                                        showLoadingProgress();
                                        Intent intent = new Intent();
                                        intent.putStringArrayListExtra(GalleryActivity.IMAGE_URLS, resultLocalPaths);
                                        ProductAddActivity.start(getActivity(),
                                                resultLocalPaths);
                                        getActivity().finish();
                                    }
                                });
                        break;
                    default:
                        // no op
                        break;
                }
                break;
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
                .setMenu(R.menu.menu_manage_product_action_item);

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
                } else if (itemId == R.id.set_cashback_product_menu){
                    showOptionCashback(productManageViewModel.getProductId(), productManageViewModel.getProductPricePlain(), productManageViewModel.getProductCurrencySymbol());
                }
            }
        };
    }

    private void showOptionCashback(String productId, String productPrice, String productPriceSymbol) {
        double productPricePlain = Double.parseDouble(productPrice);

        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(getString(R.string.product_manage_title_set_cashback))
                .addItem(CASHBACK_OPTION_3, getString(R.string.product_manage_label_option_cashback_3, productPriceSymbol, (int) ((3/100.0f) * productPricePlain)), null)
                .addItem(CASHBACK_OPTION_4, getString(R.string.product_manage_label_option_cashback_4, productPriceSymbol, (int) ((4/100.0f) * productPricePlain)), null)
                .addItem(CASHBACK_OPTION_5, getString(R.string.product_manage_label_option_cashback_5, productPriceSymbol, (int) ((5/100.0f) * productPricePlain)), null)
                .addItem(WITHOUT_CASHBACK_OPTION, getString(R.string.product_manage_label_option_without_cashback), null);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionCashbackClicked(productId))
                .createDialog();
        bottomSheetDialog.show();
    }

    private BottomSheetItemClickListener onOptionCashbackClicked(final String productId) {
        return new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case CASHBACK_OPTION_3:
                        productManagePresenter.setCashback(productId, "3");
                        break;
                    case CASHBACK_OPTION_4:
                        productManagePresenter.setCashback(productId, "4");
                        break;
                    case CASHBACK_OPTION_5:
                        productManagePresenter.setCashback(productId, "5");
                        break;
                    case WITHOUT_CASHBACK_OPTION:
                        productManagePresenter.setCashback(productId, "0");
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