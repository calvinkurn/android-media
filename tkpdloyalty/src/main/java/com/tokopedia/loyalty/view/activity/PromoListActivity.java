package com.tokopedia.loyalty.view.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoListActivityComponent;
import com.tokopedia.loyalty.di.component.PromoListActivityComponent;
import com.tokopedia.loyalty.di.module.PromoListActivityModule;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;
import com.tokopedia.loyalty.view.presenter.IPromoListActivityPresenter;
import com.tokopedia.loyalty.view.view.IPromoListActivityView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListActivity extends BasePresenterActivity implements HasComponent<AppComponent>, IPromoListActivityView {
    @Inject
    IPromoListActivityPresenter dPresenter;

    private static Intent newInstance(Context context) {
        return new Intent(context, PromoListActivity.class);
    }

    @SuppressWarnings("unused")
    @DeepLink(Constants.Applinks.PROMO_LIST)
    public static Intent getAppLinkIntent(Context context, Bundle extras) {
        return PromoListActivity.newInstance(context);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        PromoListActivityComponent promoListActivityComponent = DaggerPromoListActivityComponent.builder()
                .appComponent(getComponent())
                .promoListActivityModule(new PromoListActivityModule(this))
                .build();
        promoListActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_promo_list;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof PromoListFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    PromoListFragment.newInstance()).commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        dPresenter.processGetPromoMenu();
    }

    @Override
    public void renderPromoMenuDataList(List<PromoMenuData> promoMenuDataList) {
        for (PromoMenuData promoMenuData : promoMenuDataList) {
            Toast.makeText(this, promoMenuData.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void renderErrorGetPromoMenuDataList(String message) {

    }

    @Override
    public void renderErrorHttpGetPromoMenuDataList(String message) {

    }

    @Override
    public void renderErrorNoConnectionGetPromoMenuDataList(String message) {

    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoMenuDataListt(String message) {

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }
}
