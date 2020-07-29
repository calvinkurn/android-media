package com.tokopedia.seller.product.draft.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMechant;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.seller.ProductEditItemComponentInstance;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.draft.di.component.DaggerProductDraftSaveBulkComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftSaveBulkModule;
import com.tokopedia.seller.product.draft.utils.ImageDownloadHelper;
import com.tokopedia.seller.product.draft.view.fragment.ProductDraftListFragment;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftSaveBulkView;
import com.tokopedia.seller.product.draft.view.model.InstagramMediaModel;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ResolutionImageException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@DeepLink(ApplinkConst.PRODUCT_DRAFT)
public class ProductDraftListActivity extends BaseSimpleActivity
        implements HasComponent<ProductComponent>, ProductDraftSaveBulkView,
        ProductDraftListFragment.OnProductDraftListFragmentListener {
    public static final String TAG = ProductDraftListActivity.class.getSimpleName();

    public static final boolean DEFAULT_NEED_COMPRESS_TKPD = true;

    private static final String INSTAGRAM_MEDIA_LIST = "insta_media_list";
    private static final String LOCAL_PATH_IMAGE_LIST = "loca_img_list";
    private static final String DESC_IMAGE_LIST = "desc_img_list";
    private static final String HAS_SAVED_INSTA_TO_DRAFT = "saved_insta_to_draft";
    @Inject
    ProductDraftSaveBulkPresenter productDraftSaveBulkPresenter;
    private TkpdProgressDialog progressDialog;
    private boolean hasSaveInstagramToDraft;

    public static void startInstagramSaveBulkFromLocal(Context context, ArrayList<String> instagramLocalPaths, ArrayList<String> instagramDescList) {
        Intent intent = createInstanceFromLocalPaths(context, instagramLocalPaths, instagramDescList);
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

        if (null != savedInstanceState) {
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
                        DEFAULT_NEED_COMPRESS_TKPD,
                        new ImageDownloadHelper.OnImageDownloadListener() {
                            @Override
                            public void onError(Throwable e) {
                                hideProgressDialog();
                                NetworkErrorHelper.showCloseSnackbar(
                                        ProductDraftListActivity.this, ErrorHandler.getErrorMessage(e));
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

        setWhiteStatusBar();
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return ProductDraftListFragment.newInstance();
    }

    public void saveValidImagesToDraft(ArrayList<String> localPaths, @NonNull ArrayList<String> imageDescriptionList) {
        DaggerProductDraftSaveBulkComponent
                .builder()
                .productDraftSaveBulkModule(new ProductDraftSaveBulkModule())
                .productComponent(ProductEditItemComponentInstance.getComponent(getApplication()))
                .build()
                .inject(ProductDraftListActivity.this);
        productDraftSaveBulkPresenter.attachView(ProductDraftListActivity.this);
        showProgressDialog();
        productDraftSaveBulkPresenter.saveInstagramToDraft(ProductDraftListActivity.this,
                localPaths, imageDescriptionList);
    }

    private void setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
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
    public void onServerError() {
        // no op
    }

    @Override
    public void onTimezoneError() {
        // no op
    }

    @Override
    public ProductComponent getComponent() {
        return ProductEditItemComponentInstance.getComponent(getApplication());
    }

    @Override
    public void onSaveBulkDraftSuccess(List<Long> draftProductIdList) {
        hideProgressDialog();
        hasSaveInstagramToDraft = true;
        if (draftProductIdList.size() == 1) {
            //TODO milhamj remove comment
//            if(GlobalConfig.isSellerApp()) {
            String uri = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                    .buildUpon()
                    .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, draftProductIdList.get(0).toString())
                    .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_DRAFT)
                    .build()
                    .toString();
            Intent intent = RouteManager.getIntent(this, uri);
            startActivity(intent);
//            } else {
//                startActivity(ProductDraftAddActivity.Companion.createInstance(this,
//                draftProductIdList.get(0)));
//            }
        } else {
            Toast.makeText(this, MethodChecker.fromHtml(getString(R.string.product_draft_instagram_save_success, draftProductIdList.size())), Toast.LENGTH_LONG).show();
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
            NetworkErrorHelper.showCloseSnackbar(this,
                    getString(R.string.product_instagram_draft_error_save_resolution));
        } else {
            NetworkErrorHelper.showCloseSnackbar(this,
                    getString(R.string.product_instagram_draft_error_save_unknown));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HAS_SAVED_INSTA_TO_DRAFT, hasSaveInstagramToDraft);
    }
}
