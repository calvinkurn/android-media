package com.tokopedia.ride.maplocationpicker;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapLocationPickerActivity extends BaseActivity {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, MapLocationPickerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location_picker);
        MapLocationPickerActivityPermissionsDispatcher.initFragmentWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void initFragment() {
        addFragment(R.id.container, MapLocationPickerFragment.newInstance());
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MapLocationPickerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
