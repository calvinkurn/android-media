package com.tokopedia.tkpdpdp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterNoLayoutActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.intentservice.ProductInfoResultReceiver;
import com.tokopedia.core.product.listener.DetailFragmentInteractionListener;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.core.share.ShareBottomSheet;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.tkpdpdp.customview.YoutubeThumbnailViewHolder;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;
import com.tokopedia.tkpdpdp.listener.ProductInfoView;
import com.tokopedia.tkpdpdp.presenter.ProductInfoPresenter;
import com.tokopedia.tkpdpdp.presenter.ProductInfoPresenterImpl;

public class ProductInfoActivity extends BasePresenterNoLayoutActivity<ProductInfoPresenter> implements
        DeepLinkWebViewHandleListener,
        ProductInfoView,
        DetailFragmentInteractionListener,
        ProductInfoResultReceiver.Receiver,YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess,
        BottomSheets.BottomSheetDismissListener {

    public static final String SHARE_DATA = "SHARE_DATA";
    public static final String IS_ADDING_PRODUCT = "IS_ADDING_PRODUCT";
    private static final String KEY_FROM_EXPLORE_AFFILIATE = "is_from_explore_affiliate";

    private Uri uriData;
    private Bundle bundleData;

    ProductInfoResultReceiver mReceiver;

    @Override
    protected void forceRotation() {

    }

    @DeepLink(ApplinkConst.AFFILIATE_PRODUCT)
    public static Intent getAffiliateIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        extras.putBoolean(KEY_FROM_EXPLORE_AFFILIATE, true);
        return new Intent(context, ProductInfoActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    //Deprecated
    //@DeepLink(Constants.Applinks.PRODUCT_INFO)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ProductInfoActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info_fragmented);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_INFO;
    }

    public static Intent createInstance(Context context, @NonNull String productId) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ProductDetailRouter.EXTRA_PRODUCT_ID, productId);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createInstance(Context context, @NonNull ProductPass productPass) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_PASS, productPass);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createInstance(Context context, @NonNull ShareData shareData) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SHARE_DATA, shareData);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createInstance(Context context, String productId,
                                        int adapterPosition) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ProductDetailRouter.EXTRA_PRODUCT_ID, productId);
        bundle.putInt(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * Author : Sebast
     * Adding this for uploading product from product share activity
     *
     * @param context
     * @return
     */
    public static Intent createInstance(Context context) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_ADDING_PRODUCT, true);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    public void onResume() {
        super.onResume();
        unregisterShake();
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductInfoPresenterImpl(this);
    }

    @Override
    protected void setViewListener() {
        if (!share()) {
            presenter.initialFragment(this, uriData, bundleData);
        }
    }

    private boolean share() {

        Bundle bundle = this.bundleData;
        boolean isAddingProduct = bundle.getBoolean(ProductInfoActivity.IS_ADDING_PRODUCT);
        ShareData shareData = bundle.getParcelable(ProductInfoActivity.SHARE_DATA);

        if (isAddingProduct) {
            ShareBottomSheet share = ShareBottomSheet.newInstance(shareData, true);
            share.setDismissListener(this);
            share.show(getSupportFragmentManager(), ShareBottomSheet.TITLE_EN);
        } else if (shareData != null) {
            ShareBottomSheet share = ShareBottomSheet.newInstance(shareData, false);
            share.setDismissListener(this);
            share.show(getSupportFragmentManager(), ShareBottomSheet.TITLE_EN);
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected void initVar() {
        mReceiver = new ProductInfoResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void shareProductInfo(@NonNull ShareData shareData) {
        presenter.processToShareProduct(this, shareData);
        new DefaultShare(this, shareData).show();
    }

    @Override
    public void onProductDetailLoaded(@NonNull ProductDetailData data) {
        this.invalidateOptionsMenu();
    }

    @Override
    public void onNullResponseData(ProductPass productPass) {

    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void jumpOtherProductDetail(ProductPass productPass) {
        inflateNewFragment(ProductDetailFragment.newInstance(productPass),
                ProductDetailFragment.class.getSimpleName());
    }

    private void inflateNewFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {
        CommonUtils.UniversalToast(this, message);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void closeView() {
        finish();
    }

    @Override
    public void onBackPressed() {
            if(thumbnailIntializing) {
                isBackPressed = true;
                return;
            }


        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            this.finish();
        } else if (isTaskRoot()) {
            startActivity(HomeRouter.getHomeActivity(this));
            this.finish();
        } else {
            this.finish();
        }
    }

    public void doReport(Bundle bundle) {
        ProductInfoIntentService.startAction(this, bundle, mReceiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            switch (resultCode) {
                case ProductInfoIntentService.STATUS_SUCCESS_REPORT_PRODUCT:
                    onReceiveResultSuccess(fragment, resultData, resultCode);
                    break;
                case ProductInfoIntentService.STATUS_ERROR_REPORT_PRODUCT:
                    onReceiveResultError(fragment, resultData, resultCode);
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle stateBundle) {
        int osVersion = android.os.Build.VERSION.SDK_INT;
        if ( osVersion < Build.VERSION_CODES.N) {
            super.onSaveInstanceState(stateBundle);
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData, int resultCode) {
        ((ProductDetailFragment) fragment).onErrorAction(resultData, resultCode);
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData, int resultCode) {
        ((ProductDetailFragment) fragment).onSuccessAction(resultData, resultCode);
    }

    @Override
    public void catchToWebView(String url) {
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntent(this, url);
        startActivity(intent);
        finish();
    }

    // { Work Around IF your press back and
    //      youtube thumbnail doesn't intalized yet

    boolean isBackPressed;

    boolean thumbnailIntializing = false;
    @Override
    public void onIntializationStart() {
        thumbnailIntializing = true;
    }

    @Override
    public void onIntializationComplete() {

        thumbnailIntializing = false;
        if(isBackPressed) {
            onBackPressed();
        }
    }

    @Override
    public void onDismiss() {
        closeView();
    }

    // Work Around IF your press back and youtube thumbnail doesn't intalized yet }
}