package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;

public class RideHistoryActivity extends BaseActivity {

    public static Intent getCallingIntent(Activity activity){
        return new Intent(activity, RideHistoryActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        replaceFragment(R.id.fl_container, RideHistoryFragment.newInstance());
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

}
