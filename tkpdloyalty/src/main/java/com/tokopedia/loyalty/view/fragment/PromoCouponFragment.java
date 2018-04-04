package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoCouponComponent;
import com.tokopedia.loyalty.di.component.PromoCouponComponent;
import com.tokopedia.loyalty.di.module.PromoCouponViewModule;
import com.tokopedia.loyalty.view.adapter.CouponListAdapter;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.presenter.IPromoCouponPresenter;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import java.util.List;

import javax.inject.Inject;


/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponFragment extends BasePresenterFragment
        implements IPromoCouponView, CouponListAdapter.CouponListAdapterListener, RefreshHandler.OnRefreshHandlerListener {

    @Inject
    IPromoCouponPresenter dPresenter;

    private TkpdProgressDialog progressDialog;

    private RecyclerView couponListRecyclerView;

    private RefreshHandler refreshHandler;

    private ViewGroup mainView;

    private CouponListAdapter adapter;

    private ChooseCouponListener listener;

    private static final String PLATFORM_KEY = "PLATFORM_KEY";

    private static final String CATEGORY_KEY = "CATEGORY_KEY";

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
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        mainView = view.findViewById(R.id.main_view_coupon);
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
        refreshHandler.startRefresh();
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
        refreshHandler.finishRefresh();
        adapter = new CouponListAdapter(couponData, this);
        couponListRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderErrorGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }


    @Override
    public void renderErrorHttpGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }

    @Override
    public void renderErrorNoConnectionGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }

    @Override
    public void renderErrorTimeoutConnectionGetCouponList(String message) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.label_title_error_response),
                message,
                null, 0,
                getRetryGetCouponListErrorHandlerListener()
        );
    }

    @Override
    public void couponDataNoResult() {
        NetworkErrorHelper.showEmptyState(context, mainView,
                "Anda Tidak Memiliki Kupon",
                "Tukar poin Anda dengan kupon di halaman TokoPoints.",
                "",
                R.drawable.ic_coupon_image_big, null);
    }

    @Override
    public void receiveResult(CouponViewModel couponViewModel) {
        listener.onCouponSuccess(couponViewModel.getCode(),
                couponViewModel.getMessage(),
                couponViewModel.getAmount(),
                couponViewModel.getTitle());
    }

    @Override
    public void receiveDigitalResult(CouponViewModel couponViewModel) {
        listener.onDigitalCouponSuccess(couponViewModel.getCode(),
                couponViewModel.getMessage(),
                couponViewModel.getTitle(),
                couponViewModel.getRawDiscount(),
                couponViewModel.getRawCashback());
    }

    @Override
    public void onErrorFetchCouponList(String errorMessage) {
        refreshHandler.finishRefresh();
        NetworkErrorHelper.showEmptyState(getActivity(),
                mainView,
                errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                });
    }

    @Override
    public void couponError() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSnackbarError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
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
        progressDialog.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
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

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }

    public static PromoCouponFragment newInstance(String platform, String categoryKey) {
        PromoCouponFragment fragment = new PromoCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platform);
        bundle.putString(CATEGORY_KEY, categoryKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onVoucherChosen(CouponData data) {
        adapter.clearError();
        UnifyTracking.eventCouponChosen(data.getTitle());
        if (getArguments().getString(PLATFORM_KEY).equals(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING)) {
            dPresenter.submitDigitalVoucher(data, getArguments().getString(CATEGORY_KEY));
        } else {
            dPresenter.submitVoucher(data);
        }
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

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryGetCouponListErrorHandlerListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshHandler.startRefresh();
            }
        };
    }

    @Override
    public void onRefresh(View view) {
        if (refreshHandler.isRefreshing())
            dPresenter.processGetCouponList(getArguments().getString(PLATFORM_KEY));
    }

    @Override
    public void onDestroy() {
        dPresenter.detachView();
        super.onDestroy();
    }

    public interface ChooseCouponListener {

        void onCouponSuccess(
                String promoCode,
                String promoMessage,
                String amount,
                String couponTitle);

        void onDigitalCouponSuccess(
                String promoCode,
                String promoMessage,
                String CouponTitle,
                long discountAmount,
                long cashbackAmount);

    }


}
