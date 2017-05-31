package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.fragment.PlaceAutocompleteFragment;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.fragment.SelectLocationOnMapFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;


public class GooglePlacePickerActivity extends BaseActivity
        implements PlaceAutocompleteFragment.OnFragmentInteractionListener, SelectLocationOnMapFragment.OnFragmentInteractionListener {
    public static String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE";
    public static String EXTRA_SELECTED_PLACE = "EXTRA_SELECTED_PLACE";
    public static String EXTRA_SOURCE = "EXTRA_SOURCE";
    public static String EXTRA_MARKER_ID = "EXTRA_MARKER_ID";

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
        if (getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1) == RideHomeMapFragment.PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE) {
            showAutoDetectLocation = false;
            selectLocationOnMap = true;
        }

        addFragment(R.id.container, PlaceAutocompleteFragment.newInstance(showAutoDetectLocation, selectLocationOnMap));
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
        replaceFragment(R.id.container, SelectLocationOnMapFragment.newInstance(getIntent().getIntExtra(EXTRA_SOURCE, 0)));
    }

    @Override
    public void handleSelectDestinationOnMap(PlacePassViewModel destination) {
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
        if (getFragmentManager().findFragmentById(R.id.container) instanceof SelectLocationOnMapFragment) {
            //hideBlockTranslucentLayout();
            //hideSeatPanelLayout();
            getFragmentManager().popBackStack();

            boolean showAutoDetectLocation = true;
            boolean selectLocationOnMap = false;
            if (getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1) == RideHomeMapFragment.PLACE_AUTOCOMPLETE_DESTINATION_REQUEST_CODE) {
                showAutoDetectLocation = false;
                selectLocationOnMap = true;
            }

            replaceFragment(R.id.container, PlaceAutocompleteFragment.newInstance(showAutoDetectLocation, selectLocationOnMap));
//            mSlidingUpPanelLayout.setPanelHeight(Float.floatToIntBits(getResources().getDimension(R.dimen.sliding_panel_min_height)));
//            mSlidingUpPanelLayout.setParallaxOffset(Float.floatToIntBits(getResources().getDimension(R.dimen.tooler_height)));
        } else {
            super.onBackPressed();
        }
    }
}
