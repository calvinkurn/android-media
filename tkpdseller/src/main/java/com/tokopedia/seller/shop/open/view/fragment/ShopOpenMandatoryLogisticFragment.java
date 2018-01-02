package com.tokopedia.seller.shop.open.view.fragment;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.util.ShopErrorHandler;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.Shipment;
import com.tokopedia.seller.shop.open.view.CourierListViewGroup;
import com.tokopedia.seller.shop.open.view.ShopCourierExpandableOption;
import com.tokopedia.seller.shop.open.view.listener.ShopSettingLogisticView;
import com.tokopedia.seller.shop.open.view.presenter.ShopSettingLogisticPresenterImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 10/21/17.
 */

public class ShopOpenMandatoryLogisticFragment extends BaseDaggerFragment implements ShopSettingLogisticView, ShopCourierExpandableOption.OnShopCourierExpandableOptionListener {
    private StepperListener<ShopOpenStepperModel> onShopStepperListener;

    private CourierServiceIdWrapper selectedCourierServiceIdWrapper;

    public static final int DEFAULT_DISTRICT_ID = -1; // 2253 for jakarta
    public static final String SAVED_SELECTED_COURIER = "svd_sel_couriers";

    @Inject
    public ShopSettingLogisticPresenterImpl presenter;
    private View vContent;
    private View vLoading;
    private CourierListViewGroup courierListViewGroup;
    private TkpdProgressDialog tkpdProgressDialog;

    public static ShopOpenMandatoryLogisticFragment newInstance() {
        return new ShopOpenMandatoryLogisticFragment();
    }

    @Override
    protected void initInjector() {
        getComponent(ShopOpenDomainComponent.class).inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            selectedCourierServiceIdWrapper = onShopStepperListener.getStepperModel().getSelectedCourierServices();
        } else {
            selectedCourierServiceIdWrapper = savedInstanceState.getParcelable(SAVED_SELECTED_COURIER);
        }
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
        courierListViewGroup.setCourierList(null, selectedCourierServiceIdWrapper);
        courierListViewGroup.setOnShopCourierExpandableOptionListener(this);

        View continueButton = view.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinueButtonClicked();
            }
        });

        return view;
    }

    private void onContinueButtonClicked(){
        //check validity
        selectedCourierServiceIdWrapper = courierListViewGroup.getSelectedCourierList();
        List<String> courierIdList = selectedCourierServiceIdWrapper.getSelectedServiceIdList();
        if (courierIdList.size() == 0) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(),getString(R.string.min_1_courier_must_be_selected));
            return;
        }
        showSubmitLoading();
        presenter.saveCourier(selectedCourierServiceIdWrapper);
    }

    public void updateLogistic() {
        showLoading();
        presenter.getCouriers(getDistrictId());
    }

    private int getDistrictId(){
        ShopOpenStepperModel shopOpenStepperModel = onShopStepperListener.getStepperModel();
        if (shopOpenStepperModel == null) {
            return DEFAULT_DISTRICT_ID;
        }
        ResponseIsReserveDomain responseIsReserveDomain = shopOpenStepperModel.getResponseIsReserveDomain();
        if (responseIsReserveDomain == null) {
            return DEFAULT_DISTRICT_ID;
        }
        Shipment shipment = responseIsReserveDomain.getShipment();
        if (shipment == null) {
            return DEFAULT_DISTRICT_ID;
        }
        return shipment.getDistrictId();
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

    private void hideSubmitLoading() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    private void showSubmitLoading() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS,
                    getString(R.string.title_loading));
        }
        tkpdProgressDialog.showDialog();
    }

    @Override
    public void onDisabledHeaderClicked() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                .setTitle(getString(R.string.courier_cannot_activate_title))
                .setMessage(getString(R.string.courier_cannot_activate_desc))
                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // no op, just dismiss
                    }
                });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onInfoIconClicked(String title, String description) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.shipping_info_bottom_sheet);

        TextView tvInfo = dialog.findViewById(R.id.courier_information);
        TextView tvServiceName = dialog.findViewById(R.id.courier_name_service);

        tvServiceName.setText(title);
        tvInfo.setText(description);
        dialog.show();
    }

    @Override
    public void onSuccessLoadLogistic(OpenShopCouriersModel openShopCouriersModel) {
        hideLoading();
        courierListViewGroup.setCourierList(openShopCouriersModel.getCourier(), selectedCourierServiceIdWrapper);
    }

    @Override
    public void onErrorLoadLogistic(Throwable t) {
        hideLoading();
        String message = ShopErrorHandler.getErrorMessage(t);
        if (!TextUtils.isEmpty(message)) {
            showMessageError(message);
        }
    }

    @Override
    public void onErrorSaveCourier(Throwable t) {
        hideSubmitLoading();
        SnackbarRetry snackbarSubmitRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                ShopErrorHandler.getErrorMessage(t), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        onContinueButtonClicked();
                    }
                });
        snackbarSubmitRetry.showRetrySnackbar();
    }

    @Override
    public void onErrorCreateShop(Throwable t) {
        onErrorSaveCourier(t);
    }

    @Override
    public void onSuccessCreateShop() {
        hideSubmitLoading();
        onShopStepperListener.finishPage();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_SELECTED_COURIER, courierListViewGroup.getSelectedCourierList());
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
