package com.tokopedia.seller.shop.setting.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.open.view.adapter.ShopCourierAdapter;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSetingLogisticComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSetingLogisticModule;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLogisticView;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenter;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingLogisticFragment extends BaseDaggerFragment implements ShopSettingLogisticView{

    private StepperListener<ShopOpenStepperModel> onShopStepperListener;

    @Inject
    public ShopSettingLogisticPresenter presenter;
    private View vContent;
    private View vLoading;

    //private ShopCourierAdapter shopCourierAdapter;

    public static ShopSettingLogisticFragment getInstance() {
        return new ShopSettingLogisticFragment();
    }

    @Override
    protected void initInjector() {
        ShopSetingLogisticComponent component = DaggerShopSetingLogisticComponent
                .builder()
                .shopSetingLogisticModule(new ShopSetingLogisticModule())
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
        component.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //shopCourierAdapter = new ShopCourierAdapter(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_logistic, container, false);
        vContent = view.findViewById(R.id.vg_content);
        vLoading = view.findViewById(R.id.loading);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(shopCourierAdapter);

        // changeDistrictCode(4528);
        return view;
    }

    public void updateLogistic() {
        showLoading();
        presenter.getCouriers(getDistrictId());
    }

    private int getDistrictId(){
        return onShopStepperListener.getStepperModel().getDistrictID();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        updateLogistic();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachView();
    }

    private void showLoading() {
        vLoading.setVisibility(View.VISIBLE);
        vContent.setVisibility(View.GONE);
    }

    private void hideLoading() {
        vLoading.setVisibility(View.GONE);
        vContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessLoadLogistic(OpenShopCouriersModel openShopCouriersModel) {
        hideLoading();
        //shopCourierAdapter.setData(openShopCouriersModel.getCourier());
        //shopCourierAdapter.notifyDataSetChanged();
        // TODO in Success
    }

    @Override
    public void onErrorLoadLogistic(Throwable t) {
        hideLoading();
        String message = ErrorHandler.getErrorMessage(t);
        if (!TextUtils.isEmpty(message)) {
            showMessageError(message);
        }
    }

    private void showMessageError(String messsage) {
        NetworkErrorHelper.showEmptyState(getActivity(),
                        getView().findViewById(R.id.vg_root), messsage,
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                updateLogistic();
                            }
                        }
                );
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onAttachListener(Context context){
        onShopStepperListener = (StepperListener<ShopOpenStepperModel>) context;
    }
}
