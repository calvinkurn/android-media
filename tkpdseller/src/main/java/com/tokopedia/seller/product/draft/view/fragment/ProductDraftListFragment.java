package com.tokopedia.seller.product.draft.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.common.imageeditor.GalleryCropWatermarkActivity;
import com.tokopedia.seller.instoped.InstopedSellerActivity;
import com.tokopedia.seller.instoped.InstopedSellerCropWatermarkActivity;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSellerFragment;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.draft.di.component.DaggerProductDraftListComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftListModule;
import com.tokopedia.seller.product.draft.view.adapter.ProductDraftAdapter;
import com.tokopedia.seller.product.draft.view.adapter.ProductEmptyDataBinder;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListView;
import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ResolutionImageException;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftEditActivity;
import com.tokopedia.seller.product.edit.view.service.UploadProductService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;

/**
 * Created by Hendry on 6/19/2017.
 */

@RuntimePermissions
public class ProductDraftListFragment extends BaseListFragment<BlankPresenter, ProductDraftViewModel>
        implements ProductEmptyDataBinder.Callback, ProductDraftListView {

    private FabSpeedDial fabAdd;

    @Inject
    ProductDraftListPresenter productDraftListPresenter;

    private BroadcastReceiver draftBroadCastReceiver;
    private TkpdProgressDialog progressDialog;
    private MenuItem menuDelete;

    OnProductDraftListFragmentListener onProductDraftListFragmentListener;
    public interface OnProductDraftListFragmentListener{
        void saveValidImagesToDraft(ArrayList<String> localPaths, @NonNull ArrayList<String> imageDescriptionList);
    }

    public static ProductDraftListFragment newInstance() {
        return new ProductDraftListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        final ProductDraftAdapter adapter = new ProductDraftAdapter();
        adapter.setOnDraftDeleteListener(new ProductDraftAdapter.OnDraftDeleteListener() {
            @Override
            public void onDelete(final ProductDraftViewModel draftViewModel, final int position) {
                String message;
                if (TextUtils.isEmpty(draftViewModel.getProductName())) {
                    message = getString(R.string.product_draft_dialog_delete_message);
                } else {
                    message = getString(R.string.product_draft_dialog_delete_name_message, draftViewModel.getProductName());
                }
                AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                        .setMessage(MethodChecker.fromHtml(message))
                        .setPositiveButton(getString(R.string.action_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.confirmDelete(position);
                                productDraftListPresenter.deleteProductDraft(draftViewModel.getProductDraftId());
                                // update total item value so scrolllistener won't retrieve next page.
                                totalItem--;
                                if (totalItem == 0) {
                                    // go to empty state if all data has been deleted
                                    resetPageAndSearch();
                                }
                                UnifyTracking.eventDraftProductClicked(AppEventTracking.EventLabel.DELETE_DRAFT);
                            }
                        }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // no op
                            }
                        }).create();
                dialog.show();
            }
        });
        return adapter;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_draft_list;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductDraftListComponent
                .builder()
                .productDraftListModule(new ProductDraftListModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productDraftListPresenter.attachView(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_product_draft_list,menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        menuDelete.setVisible(totalItem > 0);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductDraftViewModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        if (menuDelete!= null) {
            menuDelete.setVisible(totalItem > 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(R.string.product_draft_delete_all_draft_dialog_message))
                    .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            productDraftListPresenter.clearAllDraftData();
                        }
                    }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        fabAdd = (FabSpeedDial) view.findViewById(R.id.fab_speed_dial);
        fabAdd.setListenerFabClick(new ListenerFabClick() {
            @Override
            public void onFabClick() {
                if (!fabAdd.isShown()) {
                    fabAdd.setVisibility(View.VISIBLE);
                }
            }
        });
        fabAdd.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == com.tokopedia.core.R.id.action_gallery) {
                    UnifyTracking.eventDraftProductClicked(AppEventTracking.EventLabel.ADD_PRODUCT);
                    ProductDraftListFragmentPermissionsDispatcher.onAddFromGalleryWithCheck(ProductDraftListFragment.this);
                } else if (id == com.tokopedia.core.R.id.action_camera) {
                    UnifyTracking.eventDraftProductClicked(AppEventTracking.EventLabel.ADD_PRODUCT);
                    ProductDraftListFragmentPermissionsDispatcher.onAddFromCameraWithCheck(ProductDraftListFragment.this);
                } else if (id == R.id.action_instagram) {
                    ProductDraftListFragmentPermissionsDispatcher.onInstagramClickedWithCheck(ProductDraftListFragment.this);
                }
                return false;
            }
        });
        fabAdd.setVisibility(View.GONE);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (fabAdd.isShown()) {
                        fabAdd.hide();
                    }
                } else if (dy < 0) {
                    if (!fabAdd.isShown() || fabAdd.getTop() >= getView().getBottom()) {
                        fabAdd.show();
                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onAddFromGallery() {
        GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), ProductDraftListFragment.this, 0, false, 5);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onAddFromCamera() {
        GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), ProductDraftListFragment.this, 0, true, -1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProductDraftListFragmentPermissionsDispatcher.onRequestPermissionsResult(ProductDraftListFragment.this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onInstagramClicked() {
        InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), ProductDraftListFragment.this,
                INSTAGRAM_SELECT_REQUEST_CODE, ProductManageSellerFragment.MAX_INSTAGRAM_SELECT);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }


    @Override
    public void onItemClicked(ProductDraftViewModel productDraftViewModel) {
        Intent intent;
        if (productDraftViewModel.isEdit()) {
            intent = ProductDraftEditActivity.createInstance(getActivity(),
                    String.valueOf(productDraftViewModel.getProductDraftId()));
        } else {
            intent = ProductDraftAddActivity.createInstance(getActivity(),
                    String.valueOf(productDraftViewModel.getProductDraftId()));
        }
        UnifyTracking.eventDraftProductClicked(AppEventTracking.EventLabel.EDIT_DRAFT);
        startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case INSTAGRAM_SELECT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && intent!= null) {
                    ArrayList<String> imageUrls = intent.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
                    ArrayList<String> imageDescList = intent.getStringArrayListExtra(InstopedSellerActivity.EXTRA_IMAGE_DESC_LIST);
                    if (imageUrls != null) {
                        onProductDraftListFragmentListener.saveValidImagesToDraft(imageUrls, imageDescList);
                    }
                }
                break;
            default: {
                ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
                    @Override
                    public void onSuccess(ArrayList<String> imageUrls) {
                        ProductAddActivity.start(ProductDraftListFragment.this, getActivity(), imageUrls);
                    }

                    @Override
                    public void onSuccess(String path) {
                        ArrayList<String> imageUrls = new ArrayList<>();
                        imageUrls.add(path);
                        ProductAddActivity.start(ProductDraftListFragment.this, getActivity(), imageUrls);
                    }

                    @Override
                    public void onFailed(String message) {
                        NetworkErrorHelper.showSnackbar(getActivity(), message);
                    }

                    @Override
                    public Context getContext() {
                        return getActivity();
                    }
                }, requestCode, resultCode, intent);
            }
        }
    }

    private void showProgressDialog(){
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.setCancelable(false);
        }
        if (! progressDialog.isProgress()) {
            progressDialog.showDialog();
        }
    }

    private void hideProgressDialog(){
        if (progressDialog != null && progressDialog.isProgress()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resetPageAndSearch();
        registerDraftReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterDraftReceiver();
    }

    private void registerDraftReceiver() {
        if (draftBroadCastReceiver == null) {
            draftBroadCastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(UploadProductService.ACTION_DRAFT_CHANGED)) {
                        resetPageAndSearch();
                    }
                }
            };
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                draftBroadCastReceiver, new IntentFilter(UploadProductService.ACTION_DRAFT_CHANGED));
    }

    private void unregisterDraftReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(draftBroadCastReceiver);
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        ProductEmptyDataBinder emptyGroupAdsDataBinder = new ProductEmptyDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.product_draft_draft_product_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(null);
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.product_draft_add_product));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
    }

    @Override
    protected void searchForPage(int page) {
        // page is always 0 for draft
        if (isMyServiceRunning(UploadProductService.class)) {
            productDraftListPresenter.fetchAllDraftData();
        } else {
            productDraftListPresenter.fetchAllDraftDataWithUpdateUploading();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            return true;
        }
        return false;
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // no op
    }

    @Override
    public void onEmptyButtonClicked() {
        UnifyTracking.eventDraftProductClicked(AppEventTracking.EventLabel.ADD_PRODUCT);
        ProductAddActivity.start(getActivity());
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        fabAdd.hide();
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        fabAdd.hide();
    }

    @Override
    protected void showViewList(@NonNull List list) {
        super.showViewList(list);
        fabAdd.setVisibility(View.VISIBLE);
        fabAdd.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productDraftListPresenter.detachView();
    }

    @Override
    public void onSaveBulkDraftSuccess(List<Long> productIds) {
        hideProgressDialog();
        if (productIds.size() == 1) {
            ProductDraftAddActivity.start(getContext(),
                    ProductDraftListFragment.this,
                    productIds.get(0).toString());
        } else {
            resetPageAndSearch();
            CommonUtils.UniversalToast(getActivity(),
                    getString(R.string.product_draft_instagram_save_success, productIds.size()));
        }
    }

    @Override
    public void onErrorSaveBulkDraft(Throwable throwable) {
        hideProgressDialog();
        if (throwable instanceof ResolutionImageException) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(),
                    getString(R.string.product_instagram_draft_error_save_resolution));
        } else {
            NetworkErrorHelper.showCloseSnackbar(getActivity(),
                    getString(R.string.product_instagram_draft_error_save_unknown));
        }
    }

    @Override
    public void hideDraftLoading() {
        hideProgressDialog();
    }

    @Override
    public void onSuccessDeleteAllDraft() {
        NetworkErrorHelper.showCloseSnackbar(getActivity(),getString(R.string.product_draft_success_delete_draft));
        resetPageAndSearch();
    }

    @Override
    public void onErrorDeleteAllDraft() {
        NetworkErrorHelper.showCloseSnackbar(getActivity(),getString(R.string.product_draft_error_delete_draft));
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        onProductDraftListFragmentListener = (OnProductDraftListFragmentListener) context;
    }
}