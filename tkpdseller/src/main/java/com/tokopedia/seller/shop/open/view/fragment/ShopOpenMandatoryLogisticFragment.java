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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.logistic.model.Courier;
import com.tokopedia.seller.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.seller.logistic.model.CouriersModel;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.util.ShopErrorHandler;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.Shipment;
import com.tokopedia.seller.shop.open.view.widget.ShopOpenCourierListViewGroup;
import com.tokopedia.seller.shop.open.view.widget.ShopOpenCourierExpandableOption;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenLogisticView;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenLogisticPresenterImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 10/21/17.
 */

public class ShopOpenMandatoryLogisticFragment extends BaseDaggerFragment implements ShopOpenLogisticView, ShopOpenCourierExpandableOption.OnShopCourierExpandableOptionListener {
    private StepperListener<ShopOpenStepperModel> onShopStepperListener;
    private OnShopOpenLogisticFragmentListener onShopOpenLogisticFragmentListener;
    private TextView tvMakeSurePickupLoc;

    public interface OnShopOpenLogisticFragmentListener {
        void goToPickupLocation();
    }

    private CourierServiceIdWrapper selectedCourierServiceIdWrapper;

    public static final int DEFAULT_DISTRICT_ID = -1; // 2253 for jakarta
    public static final String SAVED_SELECTED_COURIER = "svd_sel_couriers";

    @Inject
    public ShopOpenLogisticPresenterImpl presenter;
    private View vContent;
    private View vLoading;
    private ShopOpenCourierListViewGroup courierListViewGroup;
    private TkpdProgressDialog tkpdProgressDialog;

    @Inject
    ShopOpenTracking trackingOpenShop;

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

        tvMakeSurePickupLoc = view.findViewById(R.id.tv_pickup_location);

        courierListViewGroup = view.findViewById(R.id.vg_courier_list);
        courierListViewGroup.setCourierList(null, selectedCourierServiceIdWrapper, hasPinPointLocation());
        courierListViewGroup.setOnShopCourierExpandableOptionListener(this);

        View continueButton = view.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinueButtonClicked();
            }
        });

        // start hotfix, comment pickup location
        // because the new logistic API (if user click cargo or express) make the user cannot checkout
        // setPickupLocationText();
        // end hotfix

        return view;
    }

    private void onContinueButtonClicked() {
        //check validity
        if (!isInputValid()) {
            return;
        }
        showSubmitLoading();
        presenter.saveCourier(selectedCourierServiceIdWrapper);
    }

    private boolean isInputValid() {
        selectedCourierServiceIdWrapper = courierListViewGroup.getSelectedCourierList();
        List<String> courierIdList = selectedCourierServiceIdWrapper.getSelectedServiceIdList();
        if (courierIdList.size() == 0) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), getString(R.string.shop_open_error_min_1_courier_must_be_selected));
            return false;
        }
        return true;
    }

    public void updateLogistic() {
        showLoading();
        presenter.getCouriers(getDistrictId());
    }

    private void setPickupLocationText() {
        SpannableString spannableString = new SpannableString(getString(R.string.openshop_make_sure_pickup_location));
        final String locationPickUpString = getString(R.string.label_pickup_location);
        ClickableSpan clickablePickup = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                goToPickupLocation();
            }
        };
        int locationPickUpStringIndex = spannableString.toString().indexOf(locationPickUpString);
        spannableString.setSpan(clickablePickup,
                locationPickUpStringIndex, locationPickUpStringIndex + locationPickUpString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(),
                R.color.tkpd_main_green)), locationPickUpStringIndex,
                locationPickUpStringIndex + locationPickUpString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMakeSurePickupLoc.setMovementMethod(LinkMovementMethod.getInstance());
        tvMakeSurePickupLoc.setText(spannableString);
        tvMakeSurePickupLoc.setVisibility(View.VISIBLE);
    }

    private int getDistrictId() {
        Shipment shipment = getShipment();
        if (shipment == null) {
            return DEFAULT_DISTRICT_ID;
        }
        return shipment.getDistrictId();
    }

    private boolean hasPinPointLocation() {
        Shipment shipment = getShipment();
        if (shipment == null) {
            return false;
        }
        if (TextUtils.isEmpty(shipment.getLongitude()) || "0".equals(shipment.getLongitude())) {
            return false;
        }
        if (TextUtils.isEmpty(shipment.getLatitude()) || "0".equals(shipment.getLatitude())) {
            return false;
        }
        return true;
    }

    private Shipment getShipment() {
        ShopOpenStepperModel shopOpenStepperModel = onShopStepperListener.getStepperModel();
        if (shopOpenStepperModel == null) {
            return null;
        }
        ResponseIsReserveDomain responseIsReserveDomain = shopOpenStepperModel.getResponseIsReserveDomain();
        if (responseIsReserveDomain == null) {
            return null;
        }
        return responseIsReserveDomain.getShipment();
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
    public void onDisabledHeaderClicked(Courier courier) {
        AlertDialog.Builder alertDialogBuilder;
        if (courier.isExpressCourierId()) {
            alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.shop_open_error_courier_cannot_activate_pinpoint_title))
                    .setMessage(getString(R.string.shop_open_error_courier_cannot_activate_pinpoint_desc))
                    .setNegativeButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // no op, just dismiss
                        }
                    })
                    .setPositiveButton(getString(R.string.select_pickup_location), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goToPickupLocation();
                        }
                    });
        } else {
            alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.shop_open_error_courier_cannot_activate_title))
                    .setMessage(getString(R.string.shop_open_error_courier_cannot_activate_desc))
                    .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // no op, just dismiss
                        }
                    });
        }
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    private void goToPickupLocation() {
        onShopOpenLogisticFragmentListener.goToPickupLocation();
    }

    @Override
    public void onCourierServiceInfoIconClicked(String title, String description) {
        trackingOpenShop.eventOpenShopShippingServices(title);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.shipping_info_bottom_sheet);

        TextView tvInfo = dialog.findViewById(R.id.courier_information);
        TextView tvServiceName = dialog.findViewById(R.id.courier_name_service);

        tvServiceName.setText(title);
        tvInfo.setText(description);
        dialog.show();
    }

    @Override
    public void onSuccessLoadLogistic(CouriersModel couriersModel) {
        hideLoading();

        // this is temporary hotfix,
        // because the new logistic API (if user click cargo or express) make the user cannot checkout
        if (couriersModel.getCourier()!= null && couriersModel.getCourier().size() > 0) {
            for (int i = couriersModel.getCourier().size() - 1; i >= 0; i--) {
                Courier courier = couriersModel.getCourier().get(i);
                if (courier.isExpressCourierId() || courier.isCargoCourierId()) {
                    couriersModel.getCourier().remove(i);
                }
            }
        }
        // end hotfix

        courierListViewGroup.setCourierList(couriersModel.getCourier(), selectedCourierServiceIdWrapper,
                hasPinPointLocation());
    }

    @Override
    public void onErrorLoadLogistic(Throwable t) {
        hideLoading();
        String message = ShopErrorHandler.getErrorMessage(getActivity(), t);
        if (!TextUtils.isEmpty(message)) {
            showMessageError(message);
        }
    }

    @Override
    public void onErrorSaveCourier(Throwable t) {
        hideSubmitLoading();
        if(!GlobalConfig.DEBUG) Crashlytics.logException(t);
        trackingOpenShop.eventOpenShopShippingError(ShopErrorHandler.getErrorMessage(getActivity(), t));
        NetworkErrorHelper.showSnackbar(getActivity(), ShopErrorHandler.getErrorMessage(getActivity(), t));
    }

    @Override
    public void onErrorCreateShop(Throwable t) {
        onErrorSaveCourier(t);
    }

    @Override
    public void onSuccessCreateShop() {
        hideSubmitLoading();
        AppWidgetUtil.sendBroadcastToAppWidget(getActivity());
        trackingOpenShop.eventOpenShopShippingSuccess();
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

    protected void onAttachListener(Context context) {
        onShopStepperListener = (StepperListener<ShopOpenStepperModel>) context;
        onShopOpenLogisticFragmentListener = (OnShopOpenLogisticFragmentListener) context;
    }

}
