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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.history.di.RideHistoryDetailDependencyInjection;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R2.id.tv_label_total_fare)
    TextView totalFareLabelTextView;
    @BindView(R2.id.tv_total_fare_value)
    TextView totalFareValueTextView;
    @BindView(R2.id.tv_label_discount)
    TextView discountLabelTextView;
    @BindView(R2.id.tv_discount)
    TextView discountValueTextView;
    @BindView(R2.id.tv_label_cashback)
    TextView cashbackLableTextView;
    @BindView(R2.id.tv_cashback)
    TextView cashbackValueTextView;
    @BindView(R2.id.tv_total_charged)
    TextView totalChargedTexView;
    @BindView(R2.id.rl_payment_details)
    RelativeLayout paymentDetailsLayout;

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
        rideStatusTextView.setText(rideHistory.getDisplayStatus());
        driverCarTextView.setText(rideHistory.getDriverCarDisplay());
        driverNameTextView.setText(getString(R.string.your_trip_with) + " " + rideHistory.getDriverName());
        setPickupLocationText(rideHistory.getStartAddress());
        setDestinationLocation(rideHistory.getEndAddress());

        totalChargedTexView.setText(rideHistory.getFare());
        rideFareTextView.setText(rideHistory.getFare());
        totalFareValueTextView.setText(rideHistory.getTotalFare());

        if (rideHistory.getStatus().equalsIgnoreCase(RideStatus.COMPLETED)) {
            paymentDetailsLayout.setVisibility(View.VISIBLE);
            if (rideHistory.getCashback() > 0) {
                cashbackValueTextView.setText(rideHistory.getCashback() + "");
            } else {
                cashbackValueTextView.setVisibility(View.GONE);
                cashbackLableTextView.setVisibility(View.GONE);
            }
        } else {
            paymentDetailsLayout.setVisibility(View.GONE);
        }

        if (rideHistory.getDiscount() > 0) {
            discountValueTextView.setText("- " + rideHistory.getDiscount());
        } else {
            discountValueTextView.setVisibility(View.GONE);
            discountLabelTextView.setVisibility(View.GONE);
        }


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

    @OnClick(R2.id.layout_need_help)
    public void actionNeedHelpClicked() {
        startActivity(RideHistoryNeedHelpActivity.getCallingIntent(getActivity(), rideHistory));
    }


}
