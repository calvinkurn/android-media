package com.tokopedia.seller.shop.open.view.fragment;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.view.CourierListViewGroup;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLogisticFragment;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLogisticView;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLogisticPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 10/21/17.
 */

public class ShopOpenMandatoryLogisticFragment extends BaseDaggerFragment implements ShopSettingLogisticView {
    private StepperListener<ShopOpenStepperModel> onShopStepperListener;

    //TODO save instance these!
    private List<SparseArray<List<Integer>>> selectedCourierService = new ArrayList<SparseArray<List<Integer>>>();

    @Inject
    public ShopSettingLogisticPresenterImpl presenter;
    private View vContent;
    private View vLoading;
    private CourierListViewGroup courierListViewGroup;

    public static ShopSettingLogisticFragment getInstance() {
        return new ShopSettingLogisticFragment();
    }

    @Override
    protected void initInjector() {
        getComponent(ShopOpenDomainComponent.class).inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_logistic, container, false);
        vContent = view.findViewById(R.id.vg_content);
        vLoading = view.findViewById(R.id.loading);

        LayoutTransition layoutTransition = ((ViewGroup) vContent).getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        courierListViewGroup = view.findViewById(R.id.vg_courier_list);
        courierListViewGroup.setCourierList(null, selectedCourierService);

        return view;
    }

    public void updateLogistic() {
        showLoading();
        presenter.getCouriers(getDistrictId());
    }

    private int getDistrictId(){
        //TODO change from stepper model
        return 2253;
        //return onShopStepperListener.getStepperModel().getDistrictID();
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
        courierListViewGroup.setCourierList(openShopCouriersModel.getCourier(), selectedCourierService);
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
