package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.bookingride.view.fragment.PlaceAutocompleteFragment;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.fragment.SelectLocationOnMapFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;


public class GooglePlacePickerActivity extends BaseActivity implements PlaceAutocompleteFragment.OnFragmentInteractionListener,
        SelectLocationOnMapFragment.OnFragmentInteractionListener, HasComponent<RideComponent> {
    public static String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE";
    public static String EXTRA_SELECTED_PLACE = "EXTRA_SELECTED_PLACE";
    public static String EXTRA_SOURCE = "EXTRA_SOURCE";
    public static String EXTRA_DESTINATION = "EXTRA_DESTINATION";
    public static String EXTRA_MARKER_ID = "EXTRA_MARKER_ID";
    private RideComponent rideComponent;
    private PlacePassViewModel destination;

    public static Intent getCallingIntent(Activity activity, int markerId) {
        Intent intent = new Intent(activity, GooglePlacePickerActivity.class);
        intent.putExtra(EXTRA_MARKER_ID, markerId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_place_picker);

        boolean showAutoDetectLocation = true;
        boolean selectLocationOnMap = true;
        boolean showNearbyPlaces = true;
        if (getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1) == RideHomeMapFragment.PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE) {
            showAutoDetectLocation = false;
            selectLocationOnMap = true;
            showNearbyPlaces = false;
            destination = getIntent().getParcelableExtra(EXTRA_DESTINATION);
        }

        addFragment(R.id.container, PlaceAutocompleteFragment.newInstance(showAutoDetectLocation, selectLocationOnMap, showNearbyPlaces));
    }

    @Override
    public String getScreenName() {
        if (getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1) == RideHomeMapFragment.PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE) {
            return AppScreen.SCREEN_RIDE_DEST_CHANGE;
        } else {
            return AppScreen.SCREEN_RIDE_SOURCE_CHANGE;
        }
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onLocationSelected(PlacePassViewModel placePassViewModel) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_SELECTED_PLACE, placePassViewModel);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSelectLocationOnMapSelected() {
        CommonUtils.hideKeyboard(this, getCurrentFocus());
        replaceFragment(R.id.container, SelectLocationOnMapFragment.newInstance(destination, getIntent().getIntExtra(EXTRA_MARKER_ID, 0)));
    }

    @Override
    public void handleSelectDestinationOnMap(PlacePassViewModel destination) {
        if (getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1) == RideHomeMapFragment.PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE) {
            RideGATracking.eventClickDoneDestinationMap(getScreenName(), destination.getAddress()); // 15
        } else {
            RideGATracking.eventClickDoneSourceMap(getScreenName(), destination.getAddress()); // 12
        }
        if (destination != null) {
            Intent intent = getIntent();
            intent.putExtra(EXTRA_SELECTED_PLACE, destination);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void backArrowClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        RideGATracking.eventBackPress(getScreenName());
        if (getFragmentManager().findFragmentById(R.id.container) instanceof SelectLocationOnMapFragment) {
            getFragmentManager().popBackStack();

            boolean showAutoDetectLocation = true;
            boolean selectLocationOnMap = true;
            boolean showNearbyPlaces = true;
            if (getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1) == RideHomeMapFragment.PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE) {
                showAutoDetectLocation = false;
                selectLocationOnMap = true;
                showNearbyPlaces = false;
            }

            replaceFragment(R.id.container, PlaceAutocompleteFragment.newInstance(showAutoDetectLocation, selectLocationOnMap, showNearbyPlaces));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PlaceAutocompleteFragment.REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE) {
            PlaceAutocompleteFragment fragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.container);
            if (fragment != null) {
                fragment.handleLocationAlertResult(resultCode);
            }
        }
    }

    @Override
    public RideComponent getComponent() {
        if (rideComponent == null)
            initInjector();
        return rideComponent;
    }

    private void initInjector() {
        rideComponent = DaggerRideComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }
}
