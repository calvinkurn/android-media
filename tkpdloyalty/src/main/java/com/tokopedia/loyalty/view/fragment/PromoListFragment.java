package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.view.IPromoListView;

import java.util.List;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoListFragment extends BasePresenterFragment implements IPromoListView {
    @Override
    public void renderPromoDataList(List<PromoData> couponData) {

    }

    @Override
    public void renderErrorGetPromoDataList(String message) {

    }

    @Override
    public void renderErrorHttpGetPromoDataList(String message) {

    }

    @Override
    public void renderErrorNoConnectionGetPromoDataList(String message) {

    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoDataListt(String message) {

    }

    @Override
    public void renderPromoMenuDataList(List<PromoData> couponData) {

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

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_promo_list;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    public static Fragment newInstance() {
        return new PromoListFragment();
    }
}
