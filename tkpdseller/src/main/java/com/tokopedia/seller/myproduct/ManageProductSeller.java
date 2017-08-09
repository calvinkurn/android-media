package com.tokopedia.seller.myproduct;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerProductDraftListCountComponent;
import com.tokopedia.seller.product.di.module.ProductDraftListCountModule;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListCountView;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.view.service.UploadProductService;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ManageProductSeller extends ManageProduct implements
        ProductDraftListCountView {
    public static final int MAX_INSTAGRAM_SELECT = 10;
    private BroadcastReceiver draftBroadCastReceiver;

    @Inject
    ProductDraftListCountPresenter productDraftListCountPresenter;
    private TextView tvDraftProductInfo;

    @Override
    protected int getLayoutResource (){
        return R.layout.activity_manage_product_seller;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvDraftProductInfo = (TextView) findViewById(R.id.tv_draft_product);
        tvDraftProductInfo.setVisibility(View.GONE);

        DaggerProductDraftListCountComponent
                .builder()
                .productDraftListCountModule(new ProductDraftListCountModule())
                .appComponent(getComponent())
                .build()
                .inject(this);
        productDraftListCountPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productDraftListCountPresenter.detachView();
    }

    @Override
    protected void onFabMenuItemClicked(int menuItemId) {
        super.onFabMenuItemClicked(menuItemId);
        if (menuItemId == R.id.action_instagram) {
            ManageProductSellerPermissionsDispatcher.onInstagramClickedWithCheck(ManageProductSeller.this);
        }
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onInstagramClicked() {
        if (getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getApplication()).startInstopedActivityForResult(ManageProductSeller.this,
                    GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE, MAX_INSTAGRAM_SELECT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerDraftReceiver();
        if (isMyServiceRunning(UploadProductService.class)) {
            productDraftListCountPresenter.fetchAllDraftCount();
        } else {
            productDraftListCountPresenter.fetchAllDraftCountWithUpdateUploading();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void registerDraftReceiver(){
        if (draftBroadCastReceiver == null) {
            draftBroadCastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(UploadProductService.ACTION_DRAFT_CHANGED)) {
                        productDraftListCountPresenter.fetchAllDraftCount();
                    }
                }
            };
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
                draftBroadCastReceiver,new IntentFilter(UploadProductService.ACTION_DRAFT_CHANGED));
    }

    private void unregisterDraftReceiver(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(draftBroadCastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterDraftReceiver();
    }

    @Override
    public void onDraftCountLoaded(long rowCount) {
        if (rowCount == 0) {
            tvDraftProductInfo.setVisibility(View.GONE);
        } else {
            tvDraftProductInfo.setText(
                    MethodChecker.fromHtml(getString(R.string.product_manage_you_have_x_unfinished_product, rowCount)));
            tvDraftProductInfo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnifyTracking.eventManageProductClicked(AppEventTracking.EventLabel.DRAFT_PRODUCT);
                    startActivity(new Intent(ManageProductSeller.this, ProductDraftListActivity.class));
                }
            });
            tvDraftProductInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDraftCountLoadError() {
        // delete all draft when error loading draft
        productDraftListCountPresenter.clearAllDraft();
        tvDraftProductInfo.setVisibility(View.GONE);
    }

}