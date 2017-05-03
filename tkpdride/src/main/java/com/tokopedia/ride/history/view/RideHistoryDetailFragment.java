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
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.history.di.RideHistoryDetailDependencyInjection;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import butterknife.BindView;

public class RideHistoryDetailFragment extends BaseFragment implements RideHistoryDetailContract.View {
    private OnFragmentInteractionListener mListener;
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private RideHistoryViewModel rideHistory;

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

    RideHistoryDetailContract.Presenter mPresenter;

    public RideHistoryDetailFragment() {
        // Required empty public constructor
    }


    public static RideHistoryDetailFragment newInstance(RideHistoryViewModel rideHistory) {
        RideHistoryDetailFragment fragment = new RideHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_REQUEST_ID, rideHistory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rideHistory = getArguments().getParcelable(EXTRA_REQUEST_ID);
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
    public void renderHistory() {
        System.out.println("Vishal renderHistory " + rideHistory.getStartAddress());

        requestTimeTextView.setText(rideHistory.getRequestTime());
        rideStatusTextView.setText(rideHistory.getStatus());
        driverCarTextView.setText(rideHistory.getDriverCarDisplay());
        driverNameTextView.setText(rideHistory.getDriverName());
        rideFareTextView.setText(rideHistory.getFare());
        setPickupLocationText(rideHistory.getStartAddress());
        setDestinationLocation(rideHistory.getEndAddress());


        if (rideHistory.getDriverPictureUrl().length() > 0) {
            Glide.with(getActivity()).load(rideHistory.getDriverPictureUrl())
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
            driverPictTextView.setVisibility(View.GONE);
        }


        Glide.with(getActivity()).load(rideHistory.getMapImage())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.staticmap_dummy)
                .into(mapImageView);
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

    @Override
    public RideHistoryViewModel getRideHistory() {
        return rideHistory;
    }


}
