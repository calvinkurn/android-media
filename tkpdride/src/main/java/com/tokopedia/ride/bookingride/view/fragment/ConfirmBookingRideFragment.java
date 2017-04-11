package com.tokopedia.ride.bookingride.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.ConfirmBookingDependencyInjection;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.view.ConfirmBookingContract;
import com.tokopedia.ride.bookingride.view.activity.TokoCashWebViewActivity;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.SeatViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.common.configuration.RideConfiguration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmBookingRideFragment extends BaseFragment implements ConfirmBookingContract.View {
    public static final int WALLET_WEB_VIEW_REQUEST_CODE = 1012;
    public static String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    @BindView(R2.id.cabAppIcon)
    ImageView productIconImageView;
    @BindView(R2.id.topHeaderConfirmBooking)
    TextView headerTextView;
    @BindView(R2.id.tv_pool_price)
    TextView priceTextView;
    @BindView(R2.id.tv_seats)
    TextView seatsTextView;
    @BindView(R2.id.tvSurgeRate)
    TextView surgeRateTextView;
    @BindView(R2.id.cab_confirmation)
    TextView bookingConfirmationTextView;
    @BindView(R2.id.cab_select_seat)
    LinearLayout selectSeatContainer;
    @BindView(R2.id.toko_cash_topup_layout)
    LinearLayout tokoCashTopupLayout;
    @BindView(R2.id.tv_tokocash_label)
    TextView tokoCashLabelTextView;
    @BindView(R2.id.confirm_progress_bar)
    ProgressBar progressBar;

    @BindView(R2.id.toko_cash_activation_layout)
    LinearLayout tokoCashActivationLayout;
    @BindView(R2.id.tv_activation_label)
    TextView tokoCashActivationLabelTextView;
    @BindView(R2.id.tv_activation_button)
    TextView tokoCashActivationButton;

    ConfirmBookingContract.Presenter presenter;
    OnFragmentInteractionListener mListener;
    ConfirmBookingViewModel confirmBookingViewModel;

    public interface OnFragmentInteractionListener {
        void actionChangeSeatCount(List<SeatViewModel> seatViewModels);

        void actionRequestRide(ConfirmBookingViewModel confirmBookingViewModel);
    }

    public ConfirmBookingRideFragment() {

    }

    public static ConfirmBookingRideFragment newInstance(ConfirmBookingViewModel confirmBookingViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRODUCT, confirmBookingViewModel);
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
        confirmBookingViewModel = getArguments().getParcelable(EXTRA_PRODUCT);
        presenter = ConfirmBookingDependencyInjection.createPresenter(getActivity());
        presenter.attachView(this);
        presenter.initialize();
        setViewListener();
    }

    private void setViewListener() {
        Glide.with(getActivity()).load(confirmBookingViewModel.getProductImage())
                .asBitmap()
                .fitCenter()
                .dontAnimate()
                .error(R.mipmap.ic_launcher)
                .into(productIconImageView);
        headerTextView.setText(confirmBookingViewModel.getHeaderTitle());
        priceTextView.setText(confirmBookingViewModel.getPriceFmt());
//        seatsTextView.setText(confirmBookingViewModel.getSeatCount());
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
        if (confirmBookingViewModel.getMaxCapacity() > 1) {
            List<SeatViewModel> seatViewModels = new ArrayList<>();
            for (int i = 1; i <= confirmBookingViewModel.getMaxCapacity(); i++) {
                String seatTitle = null;
                if (i == 1) {
                    seatTitle = String.format("%d Seat", i);
                } else {
                    seatTitle = String.format("%d Seats", i);
                }
                seatViewModels.add(new SeatViewModel(i, seatTitle));
            }
            mListener.actionChangeSeatCount(seatViewModels);
        } else {
            Toast.makeText(getActivity(), "Capacity 1, cant change seat", Toast.LENGTH_LONG).show();
        }
    }

    public void updateSeatCount(int seat) {
        confirmBookingViewModel.setSeatCount(seat);
        presenter.actionGetFareAndEstimate();
    }

    @Override
    public RequestParams getParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LATITUDE, String.valueOf(confirmBookingViewModel.getSource().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_START_LONGITUDE, String.valueOf(confirmBookingViewModel.getSource().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LATITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLatitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_END_LONGITUDE, String.valueOf(confirmBookingViewModel.getDestination().getLongitude()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_PRODUCT_ID, String.valueOf(confirmBookingViewModel.getProductId()));
        requestParams.putString(GetFareEstimateUseCase.PARAM_SEAT_COUNT, String.valueOf(confirmBookingViewModel.getSeatCount()));
        return requestParams;
    }

    @OnClick(R2.id.cab_confirmation)
    public void actionConfirmButtonClicked() {
        clearConfiguration();
        mListener.actionRequestRide(confirmBookingViewModel);
    }

    private void clearConfiguration() {
        RideConfiguration rideConfiguration = new RideConfiguration();
        rideConfiguration.clearActiveRequest();
    }

    @Override
    public void showErrorChangeSeat(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void renderFareEstimate(String fareId, String display, float price) {
        if (getActivity().isFinishing()) {
            return;
        }

        confirmBookingViewModel.setFareId(fareId);
        confirmBookingViewModel.setPriceFmt(display);
        confirmBookingViewModel.setPrice(price);
        priceTextView.setText(display);
        seatsTextView.setText(String.valueOf(confirmBookingViewModel.getSeatCount()));
    }

    public ConfirmBookingViewModel getActiveConfirmBooking() {
        return confirmBookingViewModel;
    }

    @Override
    public void hideConfirmButton() {
        bookingConfirmationTextView.setVisibility(View.GONE);
    }

    @Override
    public float getFarePrice() {
        return confirmBookingViewModel.getPrice();
    }

    @Override
    public void showTopupTokoCashButton() {
        tokoCashTopupLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTopupTokoCashButton() {
        tokoCashTopupLayout.setVisibility(View.GONE);
    }

    @Override
    public void showConfirmButton() {
        bookingConfirmationTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R2.id.tv_topup_tokocash)
    public void actionTopupButtonClicked() {
        String seamlessURL = URLGenerator.generateURLSessionLogin(
                (Uri.encode("https://wallet-staging.tokopedia.id/")),
                getActivity()
        );
        Intent intent = TokoCashWebViewActivity.getCallingIntent(getActivity(), seamlessURL);
        startActivityForResult(intent, WALLET_WEB_VIEW_REQUEST_CODE);

    }

    @Override
    public void setBalanceText(String balance) {
        tokoCashLabelTextView.setText("Insufficient Balance. Your Balance is " + balance);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideNotActivatedTokoCashLayout() {
        tokoCashActivationLayout.setVisibility(View.GONE);
    }

    @Override
    public void showNotActivatedTokoCashLayout(final String redirectUrl) {
//        tokoCashActivationLabelTextView.setText();
        tokoCashActivationLayout.setVisibility(View.VISIBLE);
        tokoCashActivationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TokoCashWebViewActivity.getCallingIntent(getActivity(), redirectUrl);
                startActivityForResult(intent, WALLET_WEB_VIEW_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WALLET_WEB_VIEW_REQUEST_CODE) {
            presenter.clearTokoCashCache();
            presenter.actionCheckBalance();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.actionCheckBalance();
    }
}
