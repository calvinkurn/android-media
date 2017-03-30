package com.tokopedia.ride.completetrip;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;

public class CompleteTripActivity extends BaseActivity {
    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, CompleteTripActivity.class);
    }

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_completed);
        setupToolbar();
        addFragment(R.id.container, CompleteTripFragment.newInstance());
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
        Intent homeIntent = HomeRouter.getHomeActivity(getApplicationContext());
        Intent destination = new Intent(this, RideHomeActivity.class);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(destination);
        taskStackBuilder.startActivities();
        finish();
    }
}