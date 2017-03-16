package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.fragment.PlaceAutocompleteFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

public class GooglePlacePickerActivity extends BaseActivity
        implements PlaceAutocompleteFragment.OnFragmentInteractionListener {
    public static String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE";
    public static String EXTRA_SELECTED_PLACE = "EXTRA_SELECTED_PLACE";

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, GooglePlacePickerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_place_picker);
        addFragment(R.id.container, PlaceAutocompleteFragment.newInstance());
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onLocationSelected(PlacePassViewModel placePassViewModel) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_SELECTED_PLACE, placePassViewModel);
        int requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, 0);
        setResult(requestCode, intent);
        finish();
    }
}
