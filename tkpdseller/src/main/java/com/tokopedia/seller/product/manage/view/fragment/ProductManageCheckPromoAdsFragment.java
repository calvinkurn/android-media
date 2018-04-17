package com.tokopedia.seller.product.manage.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.di.DaggerProductManageComponent;
import com.tokopedia.seller.product.manage.di.ProductManageModule;
import com.tokopedia.seller.product.manage.view.listener.ProductManageCheckPromoAdsView;
import com.tokopedia.seller.product.manage.view.presenter.ProductManageCheckPromoAdsPresenter;
import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.common.data.TopAdsSourceTracking;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hadi.putra on 12/04/18.
 */

public class ProductManageCheckPromoAdsFragment extends BasePresenterFragment<ProductManageCheckPromoAdsPresenter>
        implements ProductManageCheckPromoAdsView {

    @Inject ProductManageCheckPromoAdsPresenter presenter;
    @Inject
    TopAdsSourceTracking topAdsSourceTracking;
    String shopId;
    private String itemId;

    TkpdProgressDialog progressDialog;

    public static ProductManageCheckPromoAdsFragment createInstance(String shopId, String itemId){
        ProductManageCheckPromoAdsFragment fragment = new ProductManageCheckPromoAdsFragment();
        Bundle bundle = new Bundle();

        bundle.putString(TopAdsConstant.PARAM_EXTRA_SHOP_ID, shopId);
        bundle.putString(TopAdsConstant.PARAM_EXTRA_ITEM_ID, itemId);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductManageComponent.builder()
                .productManageModule(new ProductManageModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        shopId = getArguments().getString(TopAdsConstant.PARAM_EXTRA_SHOP_ID, "");
        itemId = getArguments().getString(TopAdsConstant.PARAM_EXTRA_ITEM_ID, "");
        reloadData();
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null && getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void renderErrorView(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
    }

    @Override
    public void renderRetryRefresh() {
        NetworkErrorHelper.showEmptyState(
                getActivity(),
                getView(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        reloadData();
                    }
                });
    }

    private void reloadData() {
        showLoadingProgress();
        presenter.checkPromoAds(shopId, itemId, SessionHandler.getLoginID(getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsSourceTracking.deleteSource();
        presenter.detachView();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void moveToCreateAds() {
        openPromoteAds(getActivity(), String.format("%s?user_id=%s&item_id=%s",
                Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE,
                SessionHandler.getLoginID(getActivity()), itemId));
    }

    @Override
    public void moveToAdsDetail(String adsId) {
        topAdsSourceTracking.deleteSource();
        openPromoteAds(getActivity(), String.format("%s/%s?user_id=%s",
                Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL_CONSTS,
                adsId, SessionHandler.getLoginID(getActivity())));
    }

    private void openPromoteAds(Context context, String url) {
        Intent topadsIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);
        if (topadsIntent != null) {
            Intent intentActionView = new Intent(Intent.ACTION_VIEW);
            intentActionView.setData(Uri.parse(url));
            intentActionView.putExtra(Constants.EXTRA_APPLINK, url);
            PackageManager manager = context.getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intentActionView, 0);
            if (infos.size() > 0) {
                topadsIntent = intentActionView;
            }
            context.startActivity(topadsIntent);
        } else if (context.getApplicationContext() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) context.getApplicationContext()).goToCreateMerchantRedirect(context);
            UnifyTracking.eventTopAdsSwitcher(AppEventTracking.Category.SWITCHER);
        }
        getActivity().finish();
    }
}
