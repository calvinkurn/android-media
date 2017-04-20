package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.ride.R;

import net.hockeyapp.android.metrics.model.Base;

public class RideHistoryDetailActivity extends BaseActivity {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    public static Intent getCallingIntent(Activity activity, String requestId) {
        Intent intent = new Intent(activity, RideHistoryDetailActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_detail);
        String requestId = getIntent().getStringExtra(EXTRA_REQUEST_ID);
        replaceFragment(R.id.fl_container, RideHistoryDetailFragment.newInstance(requestId));
    }


    private void replaceFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }
}
