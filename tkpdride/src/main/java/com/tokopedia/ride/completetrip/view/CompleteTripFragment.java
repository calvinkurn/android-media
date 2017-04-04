package com.tokopedia.ride.completetrip.view;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.completetrip.di.CompleteTripDependencyInjection;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.model.Receipt;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleViewModel;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteTripFragment extends BaseFragment implements CompleteTripContract.View {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final String EXTRA_DRIVER_VEHICLE_VIEW_MODEL = "EXTRA_DRIVER_VEHICLE_VIEW_MODEL";

    @BindView(R2.id.on_trip_complete_layout)
    RelativeLayout onTripCompleteLayout;
    @BindView(R2.id.layout_loader)
    LinearLayout loaderLayout;
    @BindView(R2.id.tv_total_fare)
    TextView totalFareTextView;
    @BindView(R2.id.iv_driver_pict)
    ImageView driverPictImageView;
    @BindView(R2.id.tv_driver_rating)
    TextView driverRatingTextView;
    @BindView(R2.id.tv_driver_name)
    TextView driverNameTextView;
    @BindView(R2.id.iv_vehicle_pict)
    ImageView vehiclePictImageView;
    @BindView(R2.id.tv_vehicle_license_number)
    TextView vehicleLicenseNumberTextView;
    @BindView(R2.id.tv_vehicle_desc)
    TextView vehicleDescTextView;
    @BindView(R2.id.tv_base_fare)
    TextView baseFareTextView;
    @BindView(R2.id.tv_label_distance)
    TextView labelDistanceTextView;
    @BindView(R2.id.tv_distance)
    TextView distanceTextView;
    @BindView(R2.id.label_time)
    TextView labelTimeTextView;
    @BindView(R2.id.tv_time)
    TextView timeTextView;
    @BindView(R2.id.tv_sub_total)
    TextView subTotalTextView;
    @BindView(R2.id.tv_total_charged)
    TextView totalChargedTextView;
    @BindView(R2.id.tv_sign_up_uber)
    TextView signUpUberTextView;

    CompleteTripContract.Presenter presenter;
    private String requestId;
    private DriverVehicleViewModel driverVehicleViewModel;

    public CompleteTripFragment() {
        // Required empty public constructor
    }

    public static CompleteTripFragment newInstance(String requestId, DriverVehicleViewModel viewModel) {
        CompleteTripFragment fragment = new CompleteTripFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_REQUEST_ID, requestId);
        bundle.putParcelable(EXTRA_DRIVER_VEHICLE_VIEW_MODEL, viewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complete_trip;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitialVariable();
    }

    private void setInitialVariable() {
        presenter = CompleteTripDependencyInjection.createPresenter(getActivity());
        requestId = getArguments().getString(EXTRA_REQUEST_ID);
        driverVehicleViewModel = getArguments().getParcelable(EXTRA_DRIVER_VEHICLE_VIEW_MODEL);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitialView();
        presenter.attachView(this);
        presenter.actionGetReceipt();
    }

    private void setInitialView() {
        driverNameTextView.setText(String.valueOf(driverVehicleViewModel.getDriver().getName()));
        driverRatingTextView.setText(String.valueOf(driverVehicleViewModel.getDriver().getRating()));
        vehicleLicenseNumberTextView.setText(String.valueOf(driverVehicleViewModel.getVehicle().getLicensePlate()));
        vehicleDescTextView.setText(String.format(
                "%s %s %s",
                driverVehicleViewModel.getVehicle().getMake(),
                driverVehicleViewModel.getVehicle().getVehicleModel(),
                driverVehicleViewModel.getVehicle().getLicensePlate())
        );


        Glide.with(getActivity()).load(driverVehicleViewModel.getDriver().getPictureUrl())
                .asBitmap()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .into(new BitmapImageViewTarget(driverPictImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        driverPictImageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });

        Glide.with(getActivity()).load(driverVehicleViewModel.getVehicle().getPictureUrl())
                .asBitmap()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .into(new BitmapImageViewTarget(vehiclePictImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        vehiclePictImageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });
    }

    @Override
    public void showGetReceiptLoading() {

    }

    @Override
    public void hideGetReceiptLoading() {

    }

    @Override
    public RequestParams getReceiptParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetReceiptUseCase.PARAM_REQUEST_ID, requestId);
        return requestParams;
    }

    @Override
    public void renderReceipt(Receipt receipt) {
        baseFareTextView.setText(receipt.getTotalFare());
        labelDistanceTextView.setText(receipt.getDistanceUnit());
        distanceTextView.setText(receipt.getDistance());
        subTotalTextView.setText(receipt.getSubtotal());
        totalChargedTextView.setText(receipt.getTotalCharged());
        timeTextView.setText(String.valueOf(receipt.getDuratuinInMinute()));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
