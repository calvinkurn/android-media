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

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.draft.view.fragment.ProductDraftListFragment;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListCountView;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.draft.di.component.DaggerProductDraftListCountComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftListCountModule;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.edit.view.service.UploadProductService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;

@RuntimePermissions
public class ManageProductSeller extends ManageProduct implements
        ProductDraftListCountView {
    public static final int MAX_INSTAGRAM_SELECT = 10;
    public static final boolean DEFAULT_NEED_COMPRESS_TKPD = true;
    private BroadcastReceiver draftBroadCastReceiver;

    private TkpdProgressDialog progressDialog;

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
                .productComponent(((SellerModuleRouter) getApplication()).getProductComponent(getActivityModule()))
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
                    INSTAGRAM_SELECT_REQUEST_CODE, MAX_INSTAGRAM_SELECT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTAGRAM_SELECT_REQUEST_CODE && resultCode == RESULT_OK && data!= null) {
            List<InstagramMediaModel> images = data.getParcelableArrayListExtra(GalleryActivity.PRODUCT_SOC_MED_DATA);
            if (images == null || images.size() == 0) {
                return;
            }
            final ArrayList<String> standardResoImageUrlList = new ArrayList<>();
            final ArrayList<String> imageDescriptionList = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                InstagramMediaModel instagramMediaModel = images.get(i);
                standardResoImageUrlList.add(instagramMediaModel.standardResolution);
                imageDescriptionList.add(instagramMediaModel.captionText);
            }
            showProgressDialog();
            ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(this);
            imageDownloadHelper.convertHttpPathToLocalPath(standardResoImageUrlList, DEFAULT_NEED_COMPRESS_TKPD,
                new ImageDownloadHelper.OnImageDownloadListener() {
                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        CommonUtils.UniversalToast(ManageProductSeller.this,
                                ErrorHandler.getErrorMessage(e, ManageProductSeller.this));
                    }

                    @Override
                    public void onSuccess(ArrayList<String> localPaths) {
                        // if the path is different with the original,
                        // means no all draft is saved to local for some reasons
                        if (localPaths == null || localPaths.size() == 0 ||
                                localPaths.size() != standardResoImageUrlList.size()) {
                            throw new NullPointerException();
                        }
                        productDraftListCountPresenter.saveInstagramToDraft(ManageProductSeller.this,
                                localPaths, imageDescriptionList);
                        // goto onSaveBulkDraftSuccess
                        // goto onSaveBulkDraftError
                    }
                });
        }
    }

    private void showProgressDialog(){
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
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

    @Override
    public void onSaveBulkDraftSuccess(List<Long> productIds) {
        hideProgressDialog();

        if (productIds.size() == 1) {
            ProductDraftAddActivity.start(this,
                    productIds.get(0).toString());
        } else {
            CommonUtils.UniversalToast(this,getString(R.string.product_draft_instagram_save_success,
                    productIds.size()));
        }
    }

    @Override
    public void onSaveBulkDraftError(Throwable throwable) {
        hideProgressDialog();
        NetworkErrorHelper.showCloseSnackbar(this, getString(R.string.product_instagram_draft_error_save_unknown));
    }

    @Override
    public void onSaveInstagramResolutionError(int position, String localPath) {
        CommonUtils.UniversalToast(ManageProductSeller.this,
                getString(R.string.product_instagram_draft_error_save_resolution, position));
    }
}