package com.tokopedia.ride.history.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.history.di.RideHistoryDetailDependencyInjection;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;

import butterknife.BindView;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

public class RideHistoryDetailFragment extends BaseFragment implements RideHistoryDetailContract.View {
    private OnFragmentInteractionListener mListener;
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private String requestId;
    @BindView(R2.id.iv_google_map)
    AppCompatImageView mapImageView;
    @BindView(R2.id.tv_ride_start_time)
    AppCompatTextView requestTimeTextView;
    @BindView(R2.id.tv_driver_car_display_name)
    AppCompatTextView driverCarTextView;
    @BindView(R2.id.tv_ride_fare)
    AppCompatTextView rideFareTextView;
    @BindView(R2.id.tv_ride_status)
    AppCompatTextView rideStatusTextView;
    @BindView(R2.id.tv_source)
    AppCompatTextView sourceTextView;
    @BindView(R2.id.tv_destination)
    AppCompatTextView destinationTextView;
    @BindView(R2.id.iv_driver_pict)
    AppCompatImageView driverPictTextView;
    @BindView(R2.id.tv_driver_name)
    AppCompatTextView driverNameTextView;
    @BindView(R2.id.top_container)
    RelativeLayout topLayout;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;

    RideHistoryDetailContract.Presenter mPresenter;

    public RideHistoryDetailFragment() {
        // Required empty public constructor
    }


    public static RideHistoryDetailFragment newInstance(String requestId) {
        RideHistoryDetailFragment fragment = new RideHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_REQUEST_ID, requestId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestId = getArguments().getString(EXTRA_REQUEST_ID);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ride_history_detail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = RideHistoryDetailDependencyInjection.createPresenter(getActivity());
        mPresenter.attachView(this);
        mPresenter.initialize();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public RequestParams getSingleHistoryParam() {
        RequestParams requestParams = RequestParams.create();
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_HASH, hash);
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetSingleRideHistoryUseCase.PARAM_REQUEST_ID, requestId);
        return requestParams;
    }

    @Override
    public void showErrorLayout() {
        NetworkErrorHelper.showEmptyState(getActivity(), topLayout, getRetryListener());
    }

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.initialize();
            }
        };
    }

    @Override
    public void renderHistory(RideHistory rideHistory) {
        requestTimeTextView.setText(rideHistory.getRequestTime());
        rideStatusTextView.setText(rideHistory.getStatus());
//        if (!TextUtils.isEmpty(rideHistory.getStatus()) &&
//                (rideHistory.getStatus().equalsIgnoreCase(RideStatus.ACCEPTED) ||
//                        rideHistory.getStatus().equalsIgnoreCase(RideStatus.ARRIVING) ||
//                        rideHistory.getStatus().equalsIgnoreCase(RideStatus.IN_PROGRESS) ||
//                        rideHistory.getStatus().equalsIgnoreCase(RideStatus.DRIVER_CANCELED) ||
//                        rideHistory.getStatus().equalsIgnoreCase(RideStatus.COMPLETED))) {
        if (rideHistory.getVehicle() != null && rideHistory.getDriver() != null) {

            driverCarTextView.setText(String.format("%s %s %s",
                    rideHistory.getVehicle().getMake(),
                    rideHistory.getVehicle().getVehicleModel(),
                    rideHistory.getVehicle().getLicensePlate())
            );

            driverNameTextView.setText(rideHistory.getDriver().getName());

            Glide.with(getActivity()).load(rideHistory.getDriver().getPictureUrl())
                    .asBitmap()
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .into(new BitmapImageViewTarget(driverPictTextView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            driverPictTextView.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
        } else {
            driverCarTextView.setVisibility(View.GONE);
            driverNameTextView.setVisibility(View.GONE);
            driverPictTextView.setVisibility(View.GONE);
        }

        rideFareTextView.setText(
                String.format("%s %s",
                        rideHistory.getPayment().getCurrency(),
                        rideHistory.getPayment().getValue())
        );
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMainLayout() {
        topLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMainLayout() {
        topLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPickupLocationText(String sourceAddress) {
        sourceTextView.setText(sourceAddress);
    }

    @Override
    public void setDestinationLocation(String sourceAddress) {
        destinationTextView.setText(sourceAddress);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
