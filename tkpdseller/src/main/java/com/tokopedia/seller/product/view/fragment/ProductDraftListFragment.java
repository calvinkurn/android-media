package com.tokopedia.seller.product.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.ui.floatbutton.FabSpeedDial;
import com.tkpd.library.ui.floatbutton.ListenerFabClick;
import com.tkpd.library.ui.floatbutton.SimpleMenuListenerAdapter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerProductDraftListComponent;
import com.tokopedia.seller.product.di.module.ProductDraftListModule;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.view.activity.ProductDraftEditActivity;
import com.tokopedia.seller.product.view.adapter.ProductDraftAdapter;
import com.tokopedia.seller.product.view.listener.ProductDraftListView;
import com.tokopedia.seller.product.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Hendry on 6/19/2017.
 */

@RuntimePermissions
public class ProductDraftListFragment extends TopAdsBaseListFragment<ProductDraftListPresenter, ProductDraftViewModel>
        implements TopAdsEmptyAdDataBinder.Callback, ProductDraftListView {
    public static final String TAG = ProductDraftListFragment.class.getSimpleName();

    private FabSpeedDial fabAdd;

    @Inject
    ProductDraftListPresenter productDraftListPresenter;

    public static ProductDraftListFragment newInstance() {
        return new ProductDraftListFragment();
    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        final ProductDraftAdapter adapter = new ProductDraftAdapter();
        adapter.setOnDraftDeleteListener(new ProductDraftAdapter.OnDraftDeleteListener() {
            @Override
            public void onDelete(ProductDraftViewModel draftViewModel, int position) {
                // TODO hendry delete confirmation dialog
                adapter.confirmDelete(position);
                productDraftListPresenter.deleteProductDraft(draftViewModel.getProductId());
                // TODO hendry go to empty state if all data has been deleted
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
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        productDraftListPresenter.attachView(this);
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
                    ProductDraftListFragmentPermissionsDispatcher.onAddFromGalleryWithCheck(ProductDraftListFragment.this);

                } else if (id == com.tokopedia.core.R.id.action_camera) {
                    ProductDraftListFragmentPermissionsDispatcher.onAddFromCameraWithCheck(ProductDraftListFragment.this);
                }
                return false;
            }
        });
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
        GalleryActivity.moveToImageGalleryCamera(getActivity(),ProductDraftListFragment.this, 0, false, 5);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public void onAddFromCamera() {
        GalleryActivity.moveToImageGalleryCamera(getActivity(), ProductDraftListFragment.this, 0, true, -1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProductDraftListFragmentPermissionsDispatcher.onRequestPermissionsResult(ProductDraftListFragment.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

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

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }


    @Override
    public void onItemClicked(ProductDraftViewModel productDraftViewModel) {
        Intent intent;
        if (productDraftViewModel.isEdit()) {
            intent = ProductDraftEditActivity.createInstance(getActivity(),
                    String.valueOf( productDraftViewModel.getProductId()));
        } else {
            intent = ProductDraftAddActivity.createInstance(getActivity(),
                    String.valueOf(productDraftViewModel.getProductId()));
        }
        ProductDraftListFragment.this.startActivityForResult(intent, ProductAddActivity.PRODUCT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ImageGalleryEntry.onActivityForResult(new ImageGalleryEntry.GalleryListener() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls) {
                ProductAddActivity.start(ProductDraftListFragment.this, getActivity(),imageUrls);
            }

            @Override
            public void onSuccess(String path, int position) {
                ArrayList<String> imageUrls = new ArrayList<>();
                imageUrls.add(path);
                ProductAddActivity.start(ProductDraftListFragment.this, getActivity(),imageUrls);
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

    @Override
    public void onResume() {
        super.onResume();
        searchData();
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewDefaultBinder() {
        // TODO hendry empty state for draft product
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter) {
            @Override
            protected int getEmptyLayout() {
                return R.layout.listview_top_ads_empty_keyword_list;
            }
        };
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_keyword_your_keyword_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_keyword_please_use));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.top_ads_keyword_add_keyword));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
    }

    @Override
    protected void searchData() {
        super.searchData();
        productDraftListPresenter.fetchAllDraftData();
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // TODO hendry EmptyContentItemText clicked
    }

    @Override
    public void onEmptyButtonClicked() {
        // TODO hendry button empty clicked
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
        fabAdd.show();
    }

    @Override
    public void onSearchLoaded(List<ProductDraftViewModel> listDraft) {
        if (listDraft == null ) {
            onSearchLoaded(new ArrayList<ProductDraftViewModel>(), 0);
        } else {
            onSearchLoaded(listDraft, listDraft.size());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productDraftListPresenter.detachView();
    }
}
