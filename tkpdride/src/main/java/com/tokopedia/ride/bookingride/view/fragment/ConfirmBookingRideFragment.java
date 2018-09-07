package com.tokopedia.ride.bookingride.view.fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.view.ConfirmBookingContract;
import com.tokopedia.ride.bookingride.view.ConfirmBookingPresenter;
import com.tokopedia.ride.bookingride.view.activity.ApplyPromoActivity;
import com.tokopedia.ride.bookingride.view.activity.ManagePaymentOptionsActivity;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.SeatViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingPassData;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.ontrip.view.fragment.InterruptConfirmationDialogFragment;
import com.tokopedia.ride.ontrip.view.fragment.InterruptDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmBookingRideFragment extends BaseFragment implements ConfirmBookingContract.View {
    public static final int WALLET_WEB_VIEW_REQUEST_CODE = 1012;
    private static final int APPLY_PROMO_ACTIVITY_REQUEST_CODE = 1013;
    private static final int REQUEST_CODE_REMOVE_PROMO = 1014;
    private static final int REQUEST_CODE_INTERRUPT_DIALOG = 1015;
    private static final int REQUEST_CODE_INTERRUPT_TOKOPEDIA_DIALOG = 1016;
    private static final int REQUEST_CODE_CHANGE_PAYMENT_METHOD = 1017;
    private static final String INTERRUPT_DIALOG_TAG = "interrupt_dialog";
    private static final String INTERRUPT_TOKOPEDIA_DIALOG_TAG = "interrupt_tokopedia_dialog";


    public static String EXTRA_CONFIRM_BOOKING_DATA = "EXTRA_CONFIRM_BOOKING_DATA";
    public static String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    ImageView productIconImageView;
    TextView headerTextView;
    TextView priceTextView;
    TextView seatsTextView;
    TextView seatsLabelTextView;
    TextView surgeRateTextView;
    Button bookingConfirmationButton;
    LinearLayout selectSeatContainer;
    ImageView seatArrowDownImageView;
    TextView errortextView;
    LinearLayout mPromoResultLayout;
    TextView mPromoResultTextView;
    LinearLayout mApplyPromoLayout;
    LinearLayout promoLayout;
    ProgressBar progressLayout;
    LinearLayout confirmPageContainer;
    private SnackbarRetry snackbarRetry;

    @Inject
    ConfirmBookingPresenter presenter;

    OnFragmentInteractionListener mListener;
    ConfirmBookingViewModel confirmBookingViewModel;
    ConfirmBookingPassData confirmBookingPassData;
    private boolean isOpenInterruptWebviewDialog;
    private View paymentMethodLayout;
    private ImageView paymentMethodImage;
    private TextView paymentMethodTextView;
    private TextView tokocashBalanceTextView;

    public interface OnFragmentInteractionListener {
        void actionChangeSeatCount(List<SeatViewModel> seatViewModels);

        void actionRequestRide(ConfirmBookingViewModel confirmBookingViewModel);

        void expandSlidingPanel();

        void actionBackToProductList();
    }

    public ConfirmBookingRideFragment() {
    }

    public static ConfirmBookingRideFragment newInstance(ConfirmBookingViewModel confirmBookingViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIRM_BOOKING_DATA, confirmBookingViewModel);
        ConfirmBookingRideFragment fragment = new ConfirmBookingRideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ConfirmBookingRideFragment newInstance(ConfirmBookingPassData passData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASS_DATA, passData);
        ConfirmBookingRideFragment fragment = new ConfirmBookingRideFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_booking_ride;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmBookingPassData = getArguments().getParcelable(EXTRA_PASS_DATA);
        confirmBookingViewModel = ConfirmBookingViewModel.createInitial();
        confirmBookingViewModel.setSeatCount(confirmBookingPassData.getSeatCount());
        initView(view);
        presenter.attachView(this);
        presenter.initialize();

        //set the bottom panel as expanded initially
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.expandSlidingPanel();
            }
        }, 100);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PASS_DATA, confirmBookingPassData);
        outState.putParcelable(EXTRA_CONFIRM_BOOKING_DATA, confirmBookingViewModel);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            confirmBookingPassData = savedInstanceState.getParcelable(EXTRA_PASS_DATA);
            confirmBookingViewModel = savedInstanceState.getParcelable(EXTRA_CONFIRM_BOOKING_DATA);
        }
    }

    private void initView(View view) {
        productIconImageView = (ImageView) view.findViewById(R.id.cabAppIcon);
        headerTextView = (TextView) view.findViewById(R.id.topHeaderConfirmBooking);
        priceTextView = (TextView) view.findViewById(R.id.tv_pool_price);
        seatsTextView = (TextView) view.findViewById(R.id.tv_seats);
        seatsLabelTextView = (TextView) view.findViewById(R.id.tv_seats_needed);
        surgeRateTextView = (TextView) view.findViewById(R.id.tvSurgeRate);
        bookingConfirmationButton = (Button) view.findViewById(R.id.cab_confirmation);
        selectSeatContainer = (LinearLayout) view.findViewById(R.id.cab_select_seat);
        seatArrowDownImageView = (ImageView) view.findViewById(R.id.iv_seat_arrow_down);
        errortextView = (TextView) view.findViewById(R.id.tv_error);
        mPromoResultLayout = (LinearLayout) view.findViewById(R.id.promo_result_layout);
        mPromoResultTextView = (TextView) view.findViewById(R.id.tv_promo_desc);
        mApplyPromoLayout = (LinearLayout) view.findViewById(R.id.promo_info_layout);
        promoLayout = (LinearLayout) view.findViewById(R.id.layout_promo);
        progressLayout = (ProgressBar) view.findViewById(R.id.indeterminate_progress_bar);
        confirmPageContainer = (LinearLayout) view.findViewById(R.id.confirm_page_container);
        paymentMethodLayout = (View) view.findViewById(R.id.layout_payment_method);
        paymentMethodImage = (ImageView) view.findViewById(R.id.img_payment_method);
        paymentMethodTextView = (TextView) view.findViewById(R.id.tv_payment_method);
        tokocashBalanceTextView = (TextView) view.findViewById(R.id.tv_tokocash_balance);
    }

    @Override
    public void renderInitialView() {
        Glide.with(getActivity()).load(confirmBookingPassData.getProductImage())
                .asBitmap()
                .fitCenter()
                .dontAnimate()
                .error(R.mipmap.ic_launcher)
                .into(productIconImageView);

        headerTextView.setText(confirmBookingPassData.getHeaderTitle());
        priceTextView.setText(confirmBookingPassData.getPriceFmt());

        if (confirmBookingPassData.getProductDisplayName().equalsIgnoreCase(getString(R.string.confirm_booking_uber_pool_key))) {
            seatsTextView.setText(String.valueOf(confirmBookingPassData.getSeatCount()));
            seatsLabelTextView.setText(getString(R.string.confirm_booking_seats_needed));
            seatArrowDownImageView.setVisibility(View.VISIBLE);
            selectSeatContainer.setEnabled(true);
        } else {
            seatsTextView.setText(String.valueOf(confirmBookingPassData.getMaxCapacity()));
            seatsLabelTextView.setText(R.string.confirm_booking_capacity);
            seatArrowDownImageView.setVisibility(View.GONE);
            selectSeatContainer.setEnabled(false);
        }

        bookingConfirmationButton.setText(getString(R.string.btn_request) + " " + confirmBookingPassData.getProductDisplayName());
        surgeRateTextView.setVisibility(View.GONE);
        mPromoResultLayout.setVisibility(View.GONE);
        mApplyPromoLayout.setVisibility(View.GONE);
        paymentMethodLayout.setVisibility(View.GONE);
        bookingConfirmationButton.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bookingConfirmationButton.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        } else {
            bookingConfirmationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @OnClick(R2.id.cab_select_seat)
    public void actionSelectSeatButtonClicked() {
        hideErrorMessage();
        if (confirmBookingViewModel != null &&
                confirmBookingViewModel.getProductDisplayName() != null &&
                confirmBookingPassData.getProductDisplayName().equalsIgnoreCase(getString(R.string.confirm_booking_uber_pool_key))) {
            if (confirmBookingPassData.getMaxCapacity() > 1) {
                List<SeatViewModel> seatViewModels = new ArrayList<>();
                for (int i = 1; i <= confirmBookingPassData.getMaxCapacity(); i++) {
                    String seatTitle = null;
                    if (i == 1) {
                        seatTitle = String.format(getString(R.string.confirm_booking_seat_format), i);
                    } else {
                        seatTitle = String.format(getString(R.string.confirm_booking_seats_format), i);
                    }
                    seatViewModels.add(new SeatViewModel(i, seatTitle));
                }
                mListener.actionChangeSeatCount(seatViewModels);
            }
        }
    }

    public void updateSeatCount(int seat) {
        confirmBookingViewModel.setSeatCount(seat);
        presenter.actionGetFareAndEstimate(getParam());
    }

    @Override
    public RequestParams getParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(confirmBookingPassData.getSource().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(confirmBookingPassData.getSource().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(confirmBookingPassData.getDestination().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(confirmBookingPassData.getDestination().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_ID, String.valueOf(confirmBookingPassData.getProductId()));

        //add seat count for Uber Pool only
        if (confirmBookingPassData.getProductDisplayName().equalsIgnoreCase(getString(R.string.confirm_booking_uber_pool_key))) {
            requestParams.putString(GetFareEstimateUseCase.PARAM_SEAT_COUNT, String.valueOf(confirmBookingViewModel.getSeatCount()));
        }
        return requestParams;
    }

    @OnClick(R2.id.promo_info_layout)
    public void actionApplyPromoLayoutClicked() {
        startActivityForResult(
                ApplyPromoActivity.getCallingActivity(getActivity(), confirmBookingViewModel),
                APPLY_PROMO_ACTIVITY_REQUEST_CODE
        );
    }


    @OnClick(R2.id.cab_confirmation)
    public void actionConfirmButtonClicked() {
        hideErrorMessage();
        mListener.actionRequestRide(confirmBookingViewModel);
    }

    @Override
    public void renderFareEstimate(String fareId, String display, float price, float surgeMultiplier, String surgeConfirmationHref, String code, String promoSuccessMessage) {
        if (isRemoving()) {
            return;
        }
        confirmBookingViewModel.setFareId(fareId);
        confirmBookingViewModel.setPriceFmt(display);
        confirmBookingViewModel.setPrice(price);
        confirmBookingViewModel.setSurgeMultiplier(surgeMultiplier);
        confirmBookingViewModel.setSurgeConfirmationHref(surgeConfirmationHref);
        confirmBookingViewModel.setPromoCode(code);
        confirmBookingViewModel.setPromoDescription(promoSuccessMessage);
        confirmBookingViewModel.setSource(confirmBookingPassData.getSource());
        confirmBookingViewModel.setDestination(confirmBookingPassData.getDestination());
        confirmBookingViewModel.setProductDisplayName(confirmBookingPassData.getProductDisplayName());
        confirmBookingViewModel.setProductId(confirmBookingPassData.getProductId());
        confirmBookingViewModel.setProductImage(confirmBookingPassData.getProductImage());
        confirmBookingViewModel.setHeaderTitle(
                confirmBookingPassData.getHeaderTitle()
        );
        confirmBookingViewModel.setMaxCapacity(confirmBookingPassData.getMaxCapacity());
        confirmBookingViewModel.setCancellationFee(confirmBookingPassData.getCancellationFee());

        priceTextView.setText(display);
        if (surgeMultiplier > 0) {
            surgeRateTextView.setText(String.format("%sx", surgeMultiplier));
            surgeRateTextView.setVisibility(View.VISIBLE);
        } else {
            surgeRateTextView.setVisibility(View.GONE);
        }
        //show promo message if promo code is auto applied
        if (!TextUtils.isEmpty(confirmBookingViewModel.getPromoDescription())) {
            mPromoResultLayout.setVisibility(View.VISIBLE);
            mApplyPromoLayout.setVisibility(View.GONE);
            mPromoResultTextView.setText(confirmBookingViewModel.getPromoDescription());
        } else {
            mPromoResultLayout.setVisibility(View.GONE);
            mApplyPromoLayout.setVisibility(View.VISIBLE);
        }

        updateSeatCountUi();
    }

    private void updateSeatCountUi() {
        if (confirmBookingViewModel.getProductDisplayName().equalsIgnoreCase(getString(R.string.confirm_booking_uber_pool_key))) {
            seatsTextView.setText(String.valueOf(confirmBookingViewModel.getSeatCount()));
            seatsLabelTextView.setText(getString(R.string.confirm_booking_seats_needed));
            seatArrowDownImageView.setVisibility(View.VISIBLE);
            selectSeatContainer.setEnabled(true);
        } else {
            seatsTextView.setText(String.valueOf(confirmBookingViewModel.getMaxCapacity()));
            seatsLabelTextView.setText(R.string.confirm_booking_capacity);
            seatArrowDownImageView.setVisibility(View.GONE);
            selectSeatContainer.setEnabled(false);
        }
    }

    public ConfirmBookingPassData getActiveConfirmBooking() {
        return confirmBookingPassData;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R2.id.tv_promo_edit)
    public void actionEditPromo() {
        startActivityForResult(
                ApplyPromoActivity.getCallingActivity(getActivity(), confirmBookingViewModel),
                APPLY_PROMO_ACTIVITY_REQUEST_CODE
        );
    }

    @OnClick(R2.id.tv_change_payment_method)
    public void actionChangePaymentMethod() {
        //open manage payment activity
        startActivityForResult(ManagePaymentOptionsActivity.getCallingActivity(getActivity(), ManagePaymentOptionsActivity.TYPE_CHANGE_PAYMENT_OPTION), REQUEST_CODE_CHANGE_PAYMENT_METHOD);
    }

    @Override
    public void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String message) {
        errortextView.setVisibility(View.VISIBLE);
        errortextView.setText(message);
    }

    @Override
    public void hideErrorMessage() {
        errortextView.setVisibility(View.GONE);
    }

    @Override
    public void goToProductList() {
        mListener.actionBackToProductList();
    }

    @Override
    public void showConfirmLayout() {
        confirmPageContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPromoLayout() {
        promoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideConfirmLayout() {
        confirmPageContainer.setVisibility(View.GONE);
    }

    @Override
    public void hidePromoLayout() {
        promoLayout.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_INTERRUPT_DIALOG:
                isOpenInterruptWebviewDialog = false;
                if (resultCode == Activity.RESULT_OK) {
                    snackbarRetry.hideRetrySnackbar();
                    showProgress();
                    String id = data.getStringExtra(InterruptConfirmationDialogFragment.EXTRA_ID);
                    String key = data.getStringExtra(InterruptConfirmationDialogFragment.EXTRA_KEY);
                    RequestParams requestParams = getParam();
                    if (key != null && key.length() > 0) {
                        requestParams.putString(key, id);
                    }
                    presenter.actionGetFareAndEstimate(requestParams);
                }
                break;
            case REQUEST_CODE_INTERRUPT_TOKOPEDIA_DIALOG:
                isOpenInterruptWebviewDialog = false;
                if (resultCode == Activity.RESULT_OK) {
                    snackbarRetry.hideRetrySnackbar();
                    showProgress();
                    String id = data.getStringExtra(InterruptDialogFragment.EXTRA_KEY);
                    String key = data.getStringExtra(InterruptDialogFragment.EXTRA_VALUE);
                    RequestParams requestParams = getParam();
                    if (key != null && key.length() > 0) {
                        requestParams.putString(key, id);
                    }
                    presenter.actionGetFareAndEstimate(requestParams);
                }
                break;
            case APPLY_PROMO_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    confirmBookingViewModel = data.getParcelableExtra(ConfirmBookingViewModel.EXTRA_CONFIRM_PARAM);
                    if (!TextUtils.isEmpty(confirmBookingViewModel.getPromoCode())) {
                        mPromoResultLayout.setVisibility(View.VISIBLE);
                        mApplyPromoLayout.setVisibility(View.GONE);
                        mPromoResultTextView.setText(confirmBookingViewModel.getPromoDescription());
                    } else {
                        mPromoResultLayout.setVisibility(View.GONE);
                        mApplyPromoLayout.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case REQUEST_CODE_REMOVE_PROMO:
                if (resultCode == Activity.RESULT_OK) {
                    confirmBookingViewModel.setPromoCode("");
                    confirmBookingViewModel.setPromoDescription("");
                    mPromoResultLayout.setVisibility(View.GONE);
                    mApplyPromoLayout.setVisibility(View.VISIBLE);
                }
                break;

            case REQUEST_CODE_CHANGE_PAYMENT_METHOD:
                if (resultCode == Activity.RESULT_OK) {
                    PaymentMethodViewModel paymentMethodViewModel = data.getParcelableExtra(ManagePaymentOptionsFragment.KEY_CHANGE_PAYMENT_RESULT);
                    if (paymentMethodViewModel != null) {
                        showPaymentMethod(paymentMethodViewModel.getName(), paymentMethodViewModel.getImageUrl());
                    } else {
                        hidePaymentMethod();
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getPaymentMethodListFromCache();
        presenter.getPaymentMethodList();
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        BookingRideComponent bookingRideComponent = DaggerBookingRideComponent
                .builder()
                .rideComponent(component)
                .build();
        bookingRideComponent.inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    public void updateLocations(PlacePassViewModel source, PlacePassViewModel destination) {
        confirmBookingViewModel.setSource(source);
        confirmBookingViewModel.setDestination(destination);
        confirmBookingPassData.setSource(source);
        confirmBookingPassData.setDestination(destination);
        presenter.actionGetFareAndEstimate(getParam());
    }

    @OnClick(R2.id.confirm_booking_header)
    public void actionBookingHeaderClicked() {
        mListener.expandSlidingPanel();
    }

    @Override
    public void openInterruptConfirmationWebView(String tosUrl) {
        if (!isOpenInterruptWebviewDialog) {
            RideGATracking.eventOpenInterruptScreen(tosUrl);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag(INTERRUPT_DIALOG_TAG);
            if (previousDialog != null) {
                fragmentTransaction.remove(previousDialog);
            }
            fragmentTransaction.addToBackStack(null);
            DialogFragment dialogFragment = InterruptConfirmationDialogFragment.newInstance(tosUrl);
            dialogFragment.setTargetFragment(this, REQUEST_CODE_INTERRUPT_DIALOG);
            dialogFragment.show(getFragmentManager().beginTransaction(), INTERRUPT_DIALOG_TAG);
            isOpenInterruptWebviewDialog = true;
        }
    }

    @Override
    public void showErrorTosConfirmation(final String tosUrl) {
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), getString(R.string.uber_product_confirm_tos), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                openInterruptConfirmationWebView(tosUrl);
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void disableConfirmBtn() {
        bookingConfirmationButton.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bookingConfirmationButton.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        } else {
            bookingConfirmationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        }
    }

    @Override
    public void enableConfirmButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bookingConfirmationButton.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn_orange));
        } else {
            bookingConfirmationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn_orange));
        }
        bookingConfirmationButton.setEnabled(true);
    }

    @Override
    public void showErrorTosConfirmationDialog(String message, final String tosUrl, final String key, final String value) {
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                openInterruptConfirmationDialog(tosUrl, key, value);
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void openInterruptConfirmationDialog(String tosUrl, String key, String value) {
        if (!isOpenInterruptWebviewDialog) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            android.app.Fragment previousDialog = getFragmentManager().findFragmentByTag(INTERRUPT_TOKOPEDIA_DIALOG_TAG);
            if (previousDialog != null) {
                fragmentTransaction.remove(previousDialog);
            }
            fragmentTransaction.addToBackStack(null);
            DialogFragment dialogFragment = InterruptDialogFragment.newInstance(key, value, tosUrl);
            dialogFragment.setTargetFragment(this, REQUEST_CODE_INTERRUPT_TOKOPEDIA_DIALOG);
            dialogFragment.show(getFragmentManager().beginTransaction(), INTERRUPT_TOKOPEDIA_DIALOG_TAG);
            isOpenInterruptWebviewDialog = true;
        }
    }

    @Override
    public void showPaymentMethod(String label, String url) {
        paymentMethodTextView.setText(label);
        paymentMethodLayout.setVisibility(View.VISIBLE);

        //set image
        Glide.with(getActivity()).load(url)
                .asBitmap()
                .fitCenter()
                .dontAnimate()
                .error(R.drawable.ic_tokocash_icon)
                .into(paymentMethodImage);

    }

    @Override
    public void hidePaymentMethod() {
        paymentMethodLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTokoCashBalance(String tokoCashBalance) {
        tokocashBalanceTextView.setVisibility(View.VISIBLE);
        tokocashBalanceTextView.setText(tokoCashBalance);
    }

    @Override
    public void hideTokoCashBalance() {
        tokocashBalanceTextView.setVisibility(View.GONE);
    }
}
