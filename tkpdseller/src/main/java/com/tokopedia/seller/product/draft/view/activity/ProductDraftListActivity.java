package com.tokopedia.seller.product.draft.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.utils.ImageDownloadHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.draft.di.component.DaggerProductDraftSaveBulkComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftSaveBulkModule;
import com.tokopedia.seller.product.draft.view.fragment.ProductDraftListFragment;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftSaveBulkView;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ResolutionImageException;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSellerFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by User on 6/19/2017.
 */

public class ProductDraftListActivity extends DrawerPresenterActivity
        implements HasComponent<ProductComponent>, ProductDraftSaveBulkView,
        ProductDraftListFragment.OnProductDraftListFragmentListener {
    public static final String TAG = ProductDraftListActivity.class.getSimpleName();

    private static final String INSTAGRAM_MEDIA_LIST = "insta_media_list";
    private static final String LOCAL_PATH_IMAGE_LIST = "loca_img_list";
    private static final String DESC_IMAGE_LIST = "desc_img_list";
    private static final String HAS_SAVED_INSTA_TO_DRAFT = "saved_insta_to_draft";

    private TkpdProgressDialog progressDialog;
    private boolean hasSaveInstagramToDraft;

    @Inject
    ProductDraftSaveBulkPresenter productDraftSaveBulkPresenter;

    public static void startInstagramSaveBulkFromLocal(Context context, ArrayList<String> instagramLocalPaths, ArrayList<String> instagramDescList) {
        Intent intent = createInstanceFromLocalPaths(context, instagramLocalPaths, instagramDescList);
        context.startActivity(intent);
    }

    public static void startInstagramSaveBulk(Context context, ArrayList<InstagramMediaModel> instagramMediaModelList) {
        Intent intent = createInstance(context, instagramMediaModelList);
        context.startActivity(intent);
    }

    public static Intent createInstance(Context context, ArrayList<InstagramMediaModel> instagramMediaModelList) {
        Intent intent = new Intent(context, ProductDraftListActivity.class);
        intent.putParcelableArrayListExtra(INSTAGRAM_MEDIA_LIST, instagramMediaModelList);
        return intent;
    }

    public static Intent createInstanceFromLocalPaths(Context context, ArrayList<String> localPathImagelist, ArrayList<String> instagramDescList) {
        Intent intent = new Intent(context, ProductDraftListActivity.class);
        intent.putStringArrayListExtra(LOCAL_PATH_IMAGE_LIST, localPathImagelist);
        intent.putStringArrayListExtra(DESC_IMAGE_LIST, instagramDescList);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProductDraftListFragment.newInstance(), TAG)
                    .commit();
        } else {
            hasSaveInstagramToDraft = savedInstanceState.getBoolean(HAS_SAVED_INSTA_TO_DRAFT);
        }

        if (!hasSaveInstagramToDraft) {
            if (getIntent().hasExtra(LOCAL_PATH_IMAGE_LIST)) {
                ArrayList<String> localPathList = getIntent().getStringArrayListExtra(LOCAL_PATH_IMAGE_LIST);
                ArrayList<String> descList = getIntent().getStringArrayListExtra(DESC_IMAGE_LIST);
                saveValidImagesToDraft(localPathList, descList);
            } else if (getIntent().hasExtra(INSTAGRAM_MEDIA_LIST)) {
                showProgressDialog();
                List<InstagramMediaModel> images = getIntent().getParcelableArrayListExtra(INSTAGRAM_MEDIA_LIST);
                final ArrayList<String> standardResoImageUrlList = new ArrayList<>();
                final ArrayList<String> imageDescriptionList = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    InstagramMediaModel instagramMediaModel = images.get(i);
                    standardResoImageUrlList.add(instagramMediaModel.standardResolution);
                    imageDescriptionList.add(instagramMediaModel.captionText);
                }
                ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(this);
                imageDownloadHelper.convertHttpPathToLocalPath(standardResoImageUrlList,
                        ProductManageSellerFragment.DEFAULT_NEED_COMPRESS_TKPD,
                        new ImageDownloadHelper.OnImageDownloadListener() {
                            @Override
                            public void onError(Throwable e) {
                                hideProgressDialog();
                                NetworkErrorHelper.showCloseSnackbar(
                                        getActivity(), ErrorHandler.getErrorMessage(e));
                            }

                            @Override
                            public void onSuccess(ArrayList<String> localPaths) {
                                hideProgressDialog();
                                // if the path is different with the original,
                                // means no all draft is saved to local for some reasons
                                if (localPaths == null || localPaths.size() == 0 ||
                                        localPaths.size() != standardResoImageUrlList.size()) {
                                    throw new NullPointerException();
                                }
                                saveValidImagesToDraft(localPaths, imageDescriptionList);
                            }
                        });
            }
        }
    }

    public void saveValidImagesToDraft(ArrayList<String> localPaths, @NonNull ArrayList<String> imageDescriptionList) {
        DaggerProductDraftSaveBulkComponent
                .builder()
                .productDraftSaveBulkModule(new ProductDraftSaveBulkModule())
                .productComponent(((SellerModuleRouter) getApplication()).getProductComponent())
                .build()
                .inject(ProductDraftListActivity.this);
        productDraftSaveBulkPresenter.attachView(ProductDraftListActivity.this);
        showProgressDialog();
        productDraftSaveBulkPresenter.saveInstagramToDraft(ProductDraftListActivity.this,
                localPaths, imageDescriptionList);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isProgress()) {
            progressDialog.showDialog();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isProgress()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onErrorGetDeposit(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetNotificationDrawer(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetProfile(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetTokoCash(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetTopPoints(String errorMessage) {
        // no op
    }

    @Override
    public void onServerError() {
        // no op
    }

    @Override
    public void onTimezoneError() {
        // no op
    }


    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.DRAFT_PRODUCT;
    }

    @Override
    protected void setupURIPass(Uri data) {
        // no op
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        // no op
    }

    @Override
    protected void initialPresenter() {
        // no op
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setViewListener() {
        // no op
    }

    @Override
    protected void initVar() {
        // no op
    }

    @Override
    protected void setActionVar() {
        // no op
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

    @Override
    public void onSaveBulkDraftSuccess(List<Long> draftProductIdList) {
        hideProgressDialog();
        hasSaveInstagramToDraft = true;
        if (draftProductIdList.size() == 1) {
            ProductDraftAddActivity.start(this, draftProductIdList.get(0));
        } else {
            CommonUtils.UniversalToast(this, getString(R.string.product_draft_instagram_save_success,
                    draftProductIdList.size()));
            ProductDraftListFragment productDraftListFragment = (ProductDraftListFragment) getSupportFragmentManager().findFragmentByTag(TAG);
            if (productDraftListFragment != null) {
                productDraftListFragment.resetPageAndSearch();
            }
        }
    }

    @Override
    public void hideDraftLoading() {
        hideProgressDialog();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HAS_SAVED_INSTA_TO_DRAFT, hasSaveInstagramToDraft);
    }
}
