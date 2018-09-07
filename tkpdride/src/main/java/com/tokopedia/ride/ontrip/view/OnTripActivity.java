package com.tokopedia.ride.ontrip.view;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow;
import com.tokopedia.ride.ontrip.view.fragment.OnTripMapFragment;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;

@RuntimePermissions
public class OnTripActivity extends BaseActivity implements OnTripMapFragment.OnFragmentInteractionListener, HasComponent<RideComponent> {
    public static final String EXTRA_PLACE_SOURCE = "EXTRA_PLACE_SOURCE";
    public static final String EXTRA_PLACE_DESTINATION = "EXTRA_PLACE_DESTINATION";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    public static String EXTRA_CONFIRM_BOOKING = "EXTRA_CONFIRM_BOOKING";
    public static String EXTRA_RIDE_REQUEST = "EXTRA_RIDE_REQUEST";
    public static String EXTRA_FAILED_MESSAGE = "EXTRA_FAILED_MESSAGE";
    public static final int RIDE_HOME_RESULT_CODE = 11;
    public static final int RIDE_BOOKING_RESULT_CODE = 12;
    public static final int APP_HOME_RESULT_CODE = 13;

    public static final String TASK_TAG_PERIODIC = "periodic_task";

    ConfirmBookingViewModel confirmBookingViewModel;
    Toolbar mToolbar;
    private BackButtonListener backButtonListener;
    private OnUpdatedByPushNotification onUpdatedByPushNotification;
    private BroadcastReceiver mReceiver;
    private RideComponent rideComponent;


    public interface BackButtonListener {
        void onBackPressed();

        boolean canGoBack();

        boolean isAnyPendingRequest();
    }

    public static Intent getCallingIntent(Activity activity, ConfirmBookingViewModel confirmBookingViewModel) {
        Intent intent = new Intent(activity, OnTripActivity.class);
        intent.putExtra(EXTRA_CONFIRM_BOOKING, confirmBookingViewModel);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity, RideRequest rideRequest) {
        Intent intent = new Intent(activity, OnTripActivity.class);
        intent.putExtra(EXTRA_RIDE_REQUEST, rideRequest);
        return intent;
    }

    public static TaskStackBuilder getCallingIntentWithShareAction(Context context, String url) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_PUSH, true);
        bundle.putString(RideStatus.KEY, RideStatus.ACCEPTED);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);

        Intent parentHome = new Intent(context, RideHomeActivity.class)
                .putExtras(bundle);
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.RIDE_TYPE)
                .setName(context.getString(R.string.share_ride_title))
                .setTextContent(context.getString(R.string.share_ride_description))
                .setUri(url)
                .build();

        Intent shareIntent = ShareActivity.getCallingRideIntent(context, shareData);

        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentHome);
        taskStackBuilder.addNextIntent(shareIntent);
        return taskStackBuilder;
    }


    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, OnTripActivity.class);
    }

    /**
     * this should be called when user receive accepted push notif
     *
     * @see com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow#showRideAccepted(Context, RideRequest)
     */
    public static TaskStackBuilder getCallingApplinkTaskStack(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);

        Intent parentHome = new Intent(context, RideHomeActivity.class)
                .putExtras(extras);

        Intent destination = new Intent(context, OnTripActivity.class)
                .putExtras(extras);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);

        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentHome);
        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_trip);
        confirmBookingViewModel = getIntent().getParcelableExtra(EXTRA_CONFIRM_BOOKING);

        //this checks if activity recreates with confirmviewbooking model, we will finish this activity
        if (savedInstanceState == null) {
            OnTripActivityPermissionsDispatcher.initFragmentWithCheck(this);
        }

        setupToolbar();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(RidePushNotificationBuildAndShow.ACTION_DONE)) {
                    if (onUpdatedByPushNotification != null) {
                        RideRequest rideRequest = intent.getParcelableExtra(RidePushNotificationBuildAndShow.EXTRA_RIDE_RIDE_REQUEST);
                        if (rideRequest != null) {
                            onUpdatedByPushNotification.onUpdatedByPushNotification(Observable.just(rideRequest));
                        }
                    }
                }
            }
        };
    }


    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void initFragment() {
        if (confirmBookingViewModel != null) {
            OnTripMapFragment fragment = OnTripMapFragment.newInstance(confirmBookingViewModel);
            backButtonListener = fragment.getBackButtonListener();
            onUpdatedByPushNotification = fragment.getUpdatedByPushListener();
            addFragment(R.id.container, fragment, OnTripMapFragment.TAG);
        } else {
            RideRequest rideRequest = getIntent().getExtras().getParcelable(EXTRA_RIDE_REQUEST);
            if (rideRequest != null) {
                OnTripMapFragment fragment = OnTripMapFragment.newInstance(rideRequest);
                backButtonListener = fragment.getBackButtonListener();
                onUpdatedByPushNotification = fragment.getUpdatedByPushListener();
                addFragment(R.id.container, fragment, OnTripMapFragment.TAG);
            } else {
                Toast.makeText(this, ErrorNetMessage.MESSAGE_ERROR_DEFAULT, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        OnTripActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void addFragment(int containerViewId, Fragment fragment, String tag) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment, tag);
            fragmentTransaction.commit();
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

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.title_requesting_ride));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (backButtonListener != null && backButtonListener.canGoBack()) {
            backButtonListener.onBackPressed();
        } else {
            Intent intent = getIntent();
            setResult(APP_HOME_RESULT_CODE, intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RideGATracking.eventBackPress(getScreenName());
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_ONTRIP;
    }


    public interface OnUpdatedByPushNotification {
        void onUpdatedByPushNotification(Observable<RideRequest> rideRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RidePushNotificationBuildAndShow.ACTION_DONE);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(mReceiver);
    }


    @Override
    public RideComponent getComponent() {
        if (rideComponent == null) initInjector();
        return rideComponent;
    }

    private void initInjector() {
        rideComponent = DaggerRideComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }

}
