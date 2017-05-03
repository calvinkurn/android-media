package com.tokopedia.ride.bookingride.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.ApplyPromoDependencyInjection;
import com.tokopedia.ride.bookingride.domain.ApplyPromoUseCase;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.view.ApplyPromoContract;
import com.tokopedia.ride.bookingride.view.activity.ApplyPromoActivity;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.common.ride.domain.model.ApplyPromo;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * Created by alvarisi on 4/25/17.
 */


public class ApplyPromoFragment extends BaseFragment implements ApplyPromoContract.View {
    private static final String EXTRA_CONFIRM_BOOKING = "EXTRA_CONFIRM_BOOKING";
    @BindView(R2.id.et_promo)
    EditText promoEditText;
    @BindView(R2.id.tv_submit_promo)
    TextView submitPromoTextView;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R2.id.ll_promo_layout)
    LinearLayout promoLayout;
    @BindView(R2.id.tv_description)
    TextView descriptionTextView;

    ApplyPromoContract.Presenter presenter;
    ConfirmBookingViewModel confirmBookingViewModel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = ApplyPromoDependencyInjection.createPresenter(getActivity());
        presenter.attachView(this);
        confirmBookingViewModel = getArguments().getParcelable(EXTRA_CONFIRM_BOOKING);
        if (!TextUtils.isEmpty(confirmBookingViewModel.getPromoCode())) {
            promoEditText.setText(String.valueOf(confirmBookingViewModel.getPromoCode()));
            descriptionTextView.setText(String.valueOf(confirmBookingViewModel.getPromoDescription()));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_apply_promo;
    }

    public static ApplyPromoFragment newInstance(ConfirmBookingViewModel confirmBookingViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        ApplyPromoFragment fragment = new ApplyPromoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R2.id.tv_submit_promo)
    public void actionSubmitPromo() {
        presenter.actionApplyPromo();
    }

    @Override
    public String getPromo() {
        return promoEditText.getText().toString();
    }

    @Override
    public RequestParams getApplyPromoParams() {
        //TODO : change param when api created
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ApplyPromoUseCase.PARAM_USER_ID, userId);
        requestParams.putString(ApplyPromoUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(ApplyPromoUseCase.PARAM_HASH, hash);
        requestParams.putString(ApplyPromoUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(ApplyPromoUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        requestParams.putString(ApplyPromoUseCase.PARAM_PROMO, getPromo());
        return requestParams;
    }

    @Override
    public void hideApplyPromoLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessApplyPromo(FareEstimate fareEstimate) {
        descriptionTextView.setTextColor(getResources().getColor(R.color.body_text_4_inverse));
        descriptionTextView.setText(fareEstimate.getMessageSuccess());
        confirmBookingViewModel.setPromoCode(getPromo());
        confirmBookingViewModel.setPromoDescription(fareEstimate.getMessageSuccess());
        confirmBookingViewModel.setDeviceType(getDeviceName());
    }

    @Override
    public void onFailedApplyPromo(ApplyPromo applyPromo) {
        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public void showApplyPromoLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedApplyPromo(String message) {
        descriptionTextView.setTextColor(getResources().getColor(R.color.red_500));
        descriptionTextView.setText(message);
    }

    @Override
    public void showApplyPromoLayout() {
        promoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideApplyPromoLayout() {
        promoLayout.setVisibility(View.GONE);
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(confirmBookingViewModel.getSource().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(confirmBookingViewModel.getSource().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_ID, String.valueOf(confirmBookingViewModel.getProductId()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_PROMO_CODE, getPromo());
        requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_NAME, String.valueOf(confirmBookingViewModel.getProductDisplayName()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_DEVICE_TYPE, getDeviceName());

        //add seat count for Uber Pool only
        if (confirmBookingViewModel.getProductDisplayName().equalsIgnoreCase(getString(R.string.confirm_booking_uber_pool_key))) {
            requestParams.putString(GetFareEstimateUseCase.PARAM_SEAT_COUNT, String.valueOf(confirmBookingViewModel.getSeatCount()));
        }

        return requestParams;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    public void setEmptyPromoError() {
        promoEditText.setError(getString(R.string.apply_promo_empty_promo_error));
    }

    @Override
    public void clearEmptyPromoError() {
        promoEditText.setError(null);
    }

    public ApplyPromoActivity.BackButtonListener getBackButtonListener() {
        return new ApplyPromoActivity.BackButtonListener() {
            @Override
            public ConfirmBookingViewModel getConfirmParam() {
                return confirmBookingViewModel;
            }
        };
    }
}
