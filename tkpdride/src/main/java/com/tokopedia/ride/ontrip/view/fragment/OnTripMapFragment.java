package com.tokopedia.ride.ontrip.view.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.ontrip.di.OnTripDependencyInjection;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.view.OnTripActivity;
import com.tokopedia.ride.ontrip.view.OnTripMapContract;
import com.tokopedia.ride.ontrip.view.OnTripMapPresenter;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnTripMapFragment extends BaseFragment implements OnTripMapContract.View, OnMapReadyCallback {
    OnTripMapContract.Presenter presenter;
    ConfirmBookingViewModel confirmBookingViewModel;
    GoogleMap mGoogleMap;

    @BindView(R2.id.mapview)
    MapView mapView;
    @BindView(R2.id.crux_cabs_source)
    TextView tvSource;
    @BindView(R2.id.crux_cabs_destination)
    TextView tvDestination;
    @BindView(R2.id.layout_src_destination)
    View mSrcDestLayout;
    @BindView(R2.id.iv_my_location_button)
    ImageView myLocationButton;
    @BindView(R2.id.layout_loader)
    LinearLayout loaderLayout;
    @BindView(R2.id.cab_processing_layout)
    RelativeLayout processingLayout;
    @BindView(R2.id.crux_cabs_uber_processing_note)
    TextView processingDescription;
    @BindView(R2.id.cabs_processing_cancel_button)
    LinearLayout cancelButton;

    OnFragmentInteractionListener onFragmentInteractionListener;

    public static OnTripMapFragment newInstance(Bundle bundle) {
        OnTripMapFragment fragment = new OnTripMapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        setMapViewListener();
    }

    public interface OnFragmentInteractionListener {
        void actionCancelBooking();
    }

    public OnTripMapFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_on_trip_map;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        presenter = OnTripDependencyInjection.createOnTripMapPresenter(getActivity());
        presenter.attachView(this);
        confirmBookingViewModel = getArguments().getParcelable(OnTripActivity.EXTRA_CONFIRM_BOOKING);
        presenter.initialize();
        setViewListener();
    }

    private void setViewListener() {
        tvSource.setText(confirmBookingViewModel.getStartAddress());
        tvDestination.setText(confirmBookingViewModel.getEndAddress());
    }

    @Override
    public RequestParams getParam() {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CreateRideRequestUseCase.PARAM_FARE_ID, confirmBookingViewModel.getFareId());
        requestParams.putString(CreateRideRequestUseCase.PARAM_START_LATITUDE, String.valueOf(confirmBookingViewModel.getStartLatitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_START_LONGITUDE, String.valueOf(confirmBookingViewModel.getStartLongitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_END_LATITUDE, String.valueOf(confirmBookingViewModel.getEndLatitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_END_LONGITUDE, String.valueOf(confirmBookingViewModel.getEndLongitude()));
        requestParams.putString(CreateRideRequestUseCase.PARAM_USER_ID, userId);
        requestParams.putString(CreateRideRequestUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(CreateRideRequestUseCase.PARAM_HASH, hash);
        requestParams.putString(CreateRideRequestUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(CreateRideRequestUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return requestParams;
    }

    @Override
    public boolean isWaitingResponse() {
        return false;
    }

    @Override
    public void showLoadingWaitingResponse() {
        processingLayout.setVisibility(View.VISIBLE);
        processingDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingWaitingResponse() {
        processingLayout.setVisibility(View.GONE);
        processingDescription.setVisibility(View.GONE);
    }

    @Override
    public void showFailedRideRequestMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void moveToCurrentLocation(double latitude, double longitude) {
        LatLng currentLocation = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 18);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("must implement fragment listener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("must implement fragment listener");
        }
    }

    private void setMapViewListener() {
        MapsInitializer.initialize(this.getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @OnClick(R2.id.iv_my_location_button)
    public void actionMyLocationButtonClicked() {
        presenter.goToMyLocation();
    }
}
