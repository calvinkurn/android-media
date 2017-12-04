package com.tokopedia.ride.bookingride.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetPromoUseCase;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.bookingride.view.ApplyPromoContract;
import com.tokopedia.ride.bookingride.view.ApplyPromoPresenter;
import com.tokopedia.ride.bookingride.view.activity.ApplyPromoActivity;
import com.tokopedia.ride.bookingride.view.adapter.OnGoingPromoAdapter;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * Created by alvarisi on 4/25/17.
 */


public class ApplyPromoFragment extends BaseFragment implements ApplyPromoContract.View, OnGoingPromoAdapter.OnAdapterInteractionListener {
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
    @BindView(R2.id.on_going_promo_container)
    LinearLayout onGoingPromoLayout;
    @BindView(R2.id.rv_promo)
    RecyclerView promoRecyclerView;
    @BindView((R2.id.progress_bar_promo_list))
    ProgressBar mPromoListProgressBar;

    @Inject
    ApplyPromoPresenter presenter;

    ConfirmBookingViewModel confirmBookingViewModel;
    OnGoingPromoAdapter onGoingPromoAdapter;
    OnFragmentInteractionListener interactionListener;

    public static ApplyPromoFragment newInstance(ConfirmBookingViewModel confirmBookingViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        ApplyPromoFragment fragment = new ApplyPromoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void openWebView(String url);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.getOnGoingPromo();
        disableApplyButton();
        confirmBookingViewModel = getArguments().getParcelable(EXTRA_CONFIRM_BOOKING);
        if (confirmBookingViewModel != null && !TextUtils.isEmpty(confirmBookingViewModel.getPromoCode())) {
            promoEditText.setText(String.valueOf(confirmBookingViewModel.getPromoCode()));
            descriptionTextView.setText(String.valueOf(confirmBookingViewModel.getPromoDescription()));
            descriptionTextView.setVisibility(View.VISIBLE);
        } else {
            descriptionTextView.setVisibility(View.GONE);
        }
        onGoingPromoAdapter = new OnGoingPromoAdapter(getActivity());
        onGoingPromoAdapter.setInteractionListener(this);
        promoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        promoRecyclerView.setAdapter(onGoingPromoAdapter);
        setViewListener();
    }

    private void setViewListener() {
        promoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        promoEditText.setBackground(getResources().getDrawable(R.drawable.et_normal_bg));
                    } else {
                        promoEditText.setBackgroundDrawable(getResources().getDrawable(R.drawable.et_normal_bg));
                    }
                    descriptionTextView.setText("");
                    disableApplyButton();
                } else {
                    descriptionTextView.setText("");
                    enableApplyButton();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        promoEditText.setBackground(getResources().getDrawable(R.drawable.et_selected_bg));
                    } else {
                        promoEditText.setBackgroundDrawable(getResources().getDrawable(R.drawable.et_selected_bg));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
    protected int getLayoutId() {
        return R.layout.fragment_apply_promo;
    }

    @OnClick(R2.id.tv_submit_promo)
    public void actionSubmitPromo() {
        RideGATracking.eventClickApplyPromoSearch(getScreenName(),getPromo()); //19
        presenter.actionApplyPromo();
    }

    @Override
    public String getPromo() {
        return promoEditText.getText().toString();
    }

    @Override
    public void hideApplyPromoLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessApplyPromo(FareEstimate fareEstimate) {
        //descriptionTextView.setVisibility(View.VISIBLE);
        //descriptionTextView.setTextColor(getResources().getColor(R.color.body_text_4_inverse));
        //descriptionTextView.setText(fareEstimate.getMessageSuccess());
        confirmBookingViewModel.setPromoCode(getPromo());
        confirmBookingViewModel.setPromoDescription(fareEstimate.getMessageSuccess());

        //set result back
        Intent intent = getActivity().getIntent();
        intent.putExtra(ConfirmBookingViewModel.EXTRA_CONFIRM_PARAM, confirmBookingViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showApplyPromoLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedApplyPromo(String message) {
        descriptionTextView.setVisibility(View.VISIBLE);
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

    @Override
    public void showPromoLoading() {
        mPromoListProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public RequestParams getPromoParams() {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPromoUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetPromoUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetPromoUseCase.PARAM_HASH, hash);
        requestParams.putString(GetPromoUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetPromoUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return requestParams;
    }

    @Override
    public void hidePromoLoading() {
        if (mPromoListProgressBar != null) {
            mPromoListProgressBar.setVisibility(View.GONE);
        }
    }

    public ApplyPromoActivity.BackButtonListener getBackButtonListener() {
        return new ApplyPromoActivity.BackButtonListener() {
            @Override
            public ConfirmBookingViewModel getConfirmParam() {
                return confirmBookingViewModel;
            }
        };
    }

    @Override
    public void renderPromoList(List<Promo> promos) {
        onGoingPromoLayout.setVisibility(View.VISIBLE);
        onGoingPromoAdapter.setPromos(promos);
    }

    @Override
    public void renderEmptyOnGoingPromo() {
        onGoingPromoLayout.setVisibility(View.GONE);
    }

    @Override
    public void onItemClicked(String promoCode) {
        RideGATracking.eventClickApplyOffers(getScreenName(),promoCode); //20
        promoEditText.setText(promoCode.toUpperCase());
        presenter.actionApplyPromo();
    }

    @Override
    public void onLinkBtnClicked(String url) {
        if (interactionListener != null) {
            interactionListener.openWebView(url);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(String.format("%s must implement OnFragmentInteractionListener",
                    getActivity().getClass().getSimpleName())
            );
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(String.format("%s must implement OnFragmentInteractionListener",
                    getActivity().getClass().getSimpleName())
            );
        }
    }

    @Override
    public void enableApplyButton() {
        if (submitPromoTextView == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            submitPromoTextView.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
        } else {
            submitPromoTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_bttn));
        }
    }

    @Override
    public void disableApplyButton() {
        if (submitPromoTextView == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            submitPromoTextView.setBackground(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        } else {
            submitPromoTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_filled_theme_disable_bttn));
        }
    }

    @Override
    public void hideErrorPromoMessage() {
        descriptionTextView.setVisibility(View.GONE);
        descriptionTextView.setText("");
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
