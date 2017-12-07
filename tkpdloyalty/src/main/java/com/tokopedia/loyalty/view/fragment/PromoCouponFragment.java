package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoCouponComponent;
import com.tokopedia.loyalty.di.component.PromoCouponComponent;
import com.tokopedia.loyalty.di.module.PromoCouponViewModule;
import com.tokopedia.loyalty.view.adapter.CouponListAdapter;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.presenter.IPromoCouponPresenter;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponFragment extends BasePresenterFragment
        implements IPromoCouponView, CouponListAdapter.CouponListAdapterListener {

    @Inject
    IPromoCouponPresenter dPresenter;

    private RecyclerView couponListRecyclerView;

    private CouponListAdapter adapter;

    private ChooseCouponListener listener;

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
        return R.layout.fragment_promo_coupon;
    }

    @Override
    protected void initView(View view) {
        couponListRecyclerView = view.findViewById(R.id.coupon_recycler_view);
        couponListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        dPresenter.processGetCouponList();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        PromoCouponComponent promoCouponComponent = DaggerPromoCouponComponent.builder()
                .appComponent((AppComponent) getComponent(AppComponent.class))
                .promoCouponViewModule(new PromoCouponViewModule(this))
                .build();
        promoCouponComponent.inject(this);
    }

    @Override
    public void renderCouponListDataResult(List<CouponData> couponData) {
        adapter = new CouponListAdapter(couponData, this);
        couponListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void receiveResult(CouponViewModel couponViewModel) {
        listener.onCouponSuccess(couponViewModel.getCode(),
                couponViewModel.getMessage(),
                couponViewModel.getAmount(),
                couponViewModel.getTitle());
    }

    @Override
    public void couponError() {
        adapter.notifyDataSetChanged();
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
    public Context getContext() {
        return getActivity();
    }

    public static PromoCouponFragment newInstance() {
        return new PromoCouponFragment();
    }

    @Override
    public void onVoucherChosen(CouponData data) {
        dPresenter.submitVoucher(data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ChooseCouponListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ChooseCouponListener) context;
    }

    public interface ChooseCouponListener {

        void onCouponSuccess(
                String promoCode,
                String promoMessage,
                String amount,
                String couponTitle);

    }


    
}
