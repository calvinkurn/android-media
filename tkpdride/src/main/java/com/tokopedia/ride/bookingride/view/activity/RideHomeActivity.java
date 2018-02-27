package com.tokopedia.ride.bookingride.view.activity;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.OtpRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.RideModuleRouter;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.RideHomeContract;
import com.tokopedia.ride.bookingride.view.RideHomePresenter;
import com.tokopedia.ride.bookingride.view.adapter.SeatAdapter;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.SeatViewModel;
import com.tokopedia.ride.bookingride.view.fragment.ConfirmBookingRideFragment;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.fragment.UberProductFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingPassData;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.GetPending;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.utils.RideUtils;
import com.tokopedia.ride.completetrip.view.CompleteTripActivity;
import com.tokopedia.ride.history.view.RideHistoryActivity;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;
import com.tokopedia.ride.ontrip.view.OnTripActivity;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleAddressViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

@RuntimePermissions
public class RideHomeActivity extends BaseActivity implements RideHomeMapFragment.OnFragmentInteractionListener,
        UberProductFragment.OnFragmentInteractionListener, ConfirmBookingRideFragment.OnFragmentInteractionListener,
        SeatAdapter.OnItemClickListener, RideHomeContract.View, HasComponent<RideComponent> {
    private static final String MAP_FRAGMENT_TAG = "map_fragment_tag";
    private static final String PRODUCTS_FRAGMENT_TAG = "products_fragment_tag";
    private static final String CONFIRM_FRAGMENT_TAG = "confirm_fragment_tag";
    public static final String EXTRA_LAUNCH_SHORTCUT = "shortcut";

    private static final int RIDE_PHONE_VERIFY_REQUEST_CODE = 1011;
    public static final int LOGIN_REQUEST_CODE = 1005;
    public static final int REQUEST_GO_TO_ONTRIP_REQUEST_CODE = 1009;

    private Unbinder unbinder;

    @BindView(R2.id.cabs_sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R2.id.bottom_container)
    FrameLayout mBottomContainer;
    @BindView(R2.id.rv_list_seats)
    RecyclerView seatListRecyclerView;
    @BindView(R2.id.block_translucent_view)
    FrameLayout blockTranslucentFrameLayout;
    @BindView(R2.id.seat_pannel)
    RelativeLayout seatPanelLayout;
    @BindView(R2.id.progress_bar_main_layout)
    LinearLayout progressBar;
    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;
    Toolbar mToolbar;

    private int mSlidingPanelMinHeightInPx, mToolBarHeightinPx;

    private boolean isSeatPanelShowed;
    RideComponent rideComponent;
    @Inject
    RideHomePresenter mPresenter;
    private boolean inBackground;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, RideHomeActivity.class);
    }

    public static Intent getCallingIntentForClearTop(Activity activity) {
        Intent intent = new Intent(activity, RideHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @DeepLink({Constants.Applinks.RIDE, Constants.Applinks.RIDE_DETAIL})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent destination = new Intent(context, RideHomeActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        return destination;
    }


    public static TaskStackBuilder getCallingTaskStask(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);

        Intent destination = new Intent(context, RideHomeActivity.class)
                .putExtras(extras);
        destination.putExtra(Constants.EXTRA_FROM_PUSH, true);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ride);

        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        mPresenter.initialize();

        mSlidingPanelMinHeightInPx = (int) getResources().getDimension(R.dimen.sliding_panel_min_height);
        mToolBarHeightinPx = (int) getResources().getDimension(R.dimen.tooler_height);

        //send GA event if launch from shortcut
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(EXTRA_LAUNCH_SHORTCUT, null) != null) {
            CommonUtils.dumper("Sending Shortcut Event to GA");
            RideGATracking.eventUberOpenViaShortcut(getScreenName());
        }
    }

    private void executeInjector() {
        if (rideComponent == null) initInjector();
        BookingRideComponent component = DaggerBookingRideComponent.builder()
                .rideComponent(rideComponent)
                .build();
        component.inject(this);
    }

    private void initInjector() {
        rideComponent = DaggerRideComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (inBackground) {
            inBackground = false;
            try {
                RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
                if (fragment != null) {
                    fragment.appResumedFromBackground();
                }
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isApplicationBroughtToBackground(this)) {
            inBackground = true;
        }
    }

    public static boolean isApplicationBroughtToBackground(final Activity activity) {
        try {
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks;

            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                tasks = activityManager.getRunningTasks(1);
            } else {
                return false;
            }

            // Check the top Activity against the list of Activities contained in the Application's package.
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_ACTIVITIES);
                for (ActivityInfo activityInfo : pi.activities) {
                    if (topActivity.getClassName().equals(activityInfo.name)) {
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    @Override
    public void closeScreen() {
        finish();
    }

    @Override
    public boolean isHavePendingRequestAndOpenedFromPushNotif() {
        return getIntent().getExtras() != null &&
                getIntent().getExtras().getBoolean(Constants.EXTRA_FROM_PUSH) &&
                getIntent().hasExtra(RideStatus.KEY) &&
                getIntent().getStringExtra(RideStatus.KEY).equalsIgnoreCase(RideStatus.ACCEPTED);
    }

    @Override
    public void showCheckPendingRequestLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public RequestParams getCurrentRideRequestParam() {
        String deviceId = GCMHandler.getRegistrationId(this);
        String userId = SessionHandler.getLoginID(this);
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_HASH, hash);
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetRideRequestDetailUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return requestParams;
    }

    @Override
    public void hideCheckPendingRequestLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRetryCheckPendingRequestLayout() {
        NetworkErrorHelper.showEmptyState(this, mainLayout, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.actionCheckPendingRequestIfAny();
            }
        });
    }

    @Override
    public void showRetryCheckPendingRequestLayout(String message) {
        NetworkErrorHelper.showEmptyState(this, mainLayout, message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.actionCheckPendingRequestIfAny();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void actionInflateInitialToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.toolbar_title_booking);
            mToolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
    }

    @Override
    public String getLastRequestId() {
        RideConfiguration rideConfiguration = new RideConfiguration(getApplicationContext());
        return rideConfiguration.getActiveRequestId();
    }

    @Override
    public void clearActiveRequestId() {
        RideConfiguration rideConfiguration = new RideConfiguration(getApplicationContext());
        rideConfiguration.clearActiveRequestId();
    }

    @Override
    public void navigateToCompleteTripScreen(String requestId, DriverVehicleAddressViewModel driverAndVehicle) {
        Intent intent = CompleteTripActivity.getCallingIntent(this, requestId, driverAndVehicle);
        startActivity(intent);
        finish();
    }

    @Override
    public void showDialogDriverCancelled() {
        Toast.makeText(this, R.string.ride_home_driver_cancel_last_rid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideMainLayout() {
        mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMainLayout() {
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPendingFareInterrupt(GetPending getPending) {
        startActivity(PayPendingFareActivity.getCallingIntent(this, getPending));
    }

    @Override
    public void actionNavigateToOnTripScreen(RideRequest rideRequest) {
        Intent intent = OnTripActivity.getCallingIntent(this, rideRequest);
        startActivityForResult(intent, REQUEST_GO_TO_ONTRIP_REQUEST_CODE);
    }

    @Override
    public void inflateMapAndProductFragment() {
        RideHomeActivityPermissionsDispatcher.initFragmentWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void initFragment() {
        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
        relativeParams.setMargins(0, 0, 0, 0);  // left, top, right, bottom
        mainLayout.setLayoutParams(relativeParams);
        mToolbar.setVisibility(View.GONE);
        replaceFragment(R.id.top_container, RideHomeMapFragment.newInstance(), MAP_FRAGMENT_TAG);
        replaceFragment(R.id.bottom_container, UberProductFragment.newInstance(), PRODUCTS_FRAGMENT_TAG);
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showDeniedForLocation() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        RequestPermissionUtil.onPermissionDenied(this, listPermission);
        initFragment();
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForLocation(final PermissionRequest request) {
        List<String> listPermisConsumersion = new ArrayList<>();
        listPermisConsumersion.add(Manifest.permission.ACCESS_FINE_LOCATION);
        listPermisConsumersion.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        RequestPermissionUtil.onShowRationale(this, request, listPermisConsumersion);
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showNeverAskForLocation() {

        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        RequestPermissionUtil.onNeverAskAgain(this, listPermission);
    }

    private void initFragmentWithPlace(PlacePassViewModel source, PlacePassViewModel destination) {
        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
        relativeParams.setMargins(0, 0, 0, 0);  // left, top, right, bottom
        mainLayout.setLayoutParams(relativeParams);
        mToolbar.setVisibility(View.GONE);

        if (source != null && destination != null) {
            replaceFragment(R.id.bottom_container, UberProductFragment.newInstance(source, destination), PRODUCTS_FRAGMENT_TAG);
            replaceFragment(R.id.top_container, RideHomeMapFragment.newInstance(source, destination), MAP_FRAGMENT_TAG);
        } else {
            replaceFragment(R.id.top_container, RideHomeMapFragment.newInstance(), MAP_FRAGMENT_TAG);
            replaceFragment(R.id.bottom_container, UberProductFragment.newInstance(), PRODUCTS_FRAGMENT_TAG);
        }
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    private void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (!isFinishing() && !fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            Fragment prevFragment = getFragmentManager().findFragmentByTag(tag);
            if (prevFragment != null) {
                fragmentTransaction.remove(prevFragment);
            }
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RideHomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onSourceAndDestinationChanged(PlacePassViewModel source, PlacePassViewModel destination) {
        if (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof UberProductFragment) {
            UberProductFragment productFragment = (UberProductFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
            if (productFragment != null) {
                productFragment.updateProductList(source, destination);
            }
        } else if (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof ConfirmBookingRideFragment && destination != null) {
            ConfirmBookingRideFragment confirmBookingRideFragment = (ConfirmBookingRideFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
            if (confirmBookingRideFragment != null) {
                confirmBookingRideFragment.updateLocations(source, destination);
            }
        } else {
            UberProductFragment productFragment = UberProductFragment.newInstance(source, destination);
            replaceFragment(R.id.bottom_container, productFragment, PRODUCTS_FRAGMENT_TAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RideHomeMapFragment.REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE) {
            RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
            if (fragment != null) {
                fragment.handleLocationAlertResult(resultCode);
            }
        } else if (requestCode == REQUEST_GO_TO_ONTRIP_REQUEST_CODE) {
            switch (resultCode) {
                case OnTripActivity.APP_HOME_RESULT_CODE:
                    finish();
                    break;
                case OnTripActivity.RIDE_HOME_RESULT_CODE:
                    PlacePassViewModel source = null, destionation = null;
                    if (data != null) {
                        source = data.getParcelableExtra(OnTripActivity.EXTRA_PLACE_SOURCE);
                        destionation = data.getParcelableExtra(OnTripActivity.EXTRA_PLACE_DESTINATION);
                    }
                    initFragmentWithPlace(source, destionation);

                    //collapse bottom sliding panel
                    if (mSlidingUpPanelLayout != null) {
                        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }

                    break;
                case OnTripActivity.RIDE_BOOKING_RESULT_CODE:
                    //message on confirm booking fragment
                    if (data != null) {
                        String message = data.getStringExtra(OnTripActivity.EXTRA_FAILED_MESSAGE);

                        Fragment bottomFragment = getFragmentManager().findFragmentById(R.id.bottom_container);
                        if (bottomFragment instanceof ConfirmBookingRideFragment) {
                            ((ConfirmBookingRideFragment) bottomFragment).showErrorMessage(message);
                        }
                    }
                    break;
            }
        } else if (requestCode == RideHomeActivity.LOGIN_REQUEST_CODE) {
            if (!SessionHandler.isV4Login(this)) {
                finish();
            } else {
                mPresenter.attachView(this);
                mPresenter.initialize();
            }
        } else if (requestCode == RIDE_PHONE_VERIFY_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Cant cont., must verify your phone number.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    mPresenter.initialize();
                    break;
            }
        }
    }

    @Override
    public void animateBottomPanelOnMapDragging() {
        if (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mBottomContainer.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(mSlidingPanelMinHeightInPx).setDuration(300);
        }
    }

    @Override
    public void animateBottomPanelOnMapStopped() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomContainer.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(300);
            }
        }, 100);
    }

    @Override
    public void showMessageInBottomContainer(String message, String btnText) {
        UberProductFragment fragment = (UberProductFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
        if (fragment != null) {
            fragment.showErrorMessage(message, btnText);
        }
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        if (mSlidingUpPanelLayout == null) {
            return null;
        }

        return mSlidingUpPanelLayout.getPanelState();
    }

    @Override
    public int getBottomViewLocation() {
        return mBottomContainer.getTop();
    }

    @Override
    public void onMinimumTimeEstCalculated(String timeEst) {
        RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.setMarkerText(timeEst);
        } else {
            fragment = RideHomeMapFragment.newInstance();
            replaceFragment(R.id.top_container, fragment, MAP_FRAGMENT_TAG);
            fragment.setMarkerText(timeEst);
        }
    }

    @Override
    public void showEnterDestError() {
        RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.showEnterDestError();
        }
    }

    @Override
    public void showEnterSourceLocationActiity() {
        RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.actionSourceButtonClicked();
        }
    }

    @Override
    public void actionProductListHeaderClick() {
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void actionAdsShowed() {
    }

    @Override
    public void actionAdsHidden() {
    }

    @Override
    public void renderNearbyRides(NearbyRides nearbyRides) {
        RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.renderNearbyRides(nearbyRides);
        }
    }

    @Override
    public void renderNearbyCabs(List<ProductEstimate> productEstimates) {

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(RideHomeActivity.this);
        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SHOW_NEARBY_CABS, false)) {
            RideHomeMapFragment rideHomeMapFragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
            if (rideHomeMapFragment != null) {
                rideHomeMapFragment.displayNearByCabs(productEstimates);
            }
        }

    }

    @Override
    public void onProductClicked(ConfirmBookingPassData confirmBookingPassData) {
        onBottomContainerChangeToBookingScreen();

        ConfirmBookingRideFragment fragment = ConfirmBookingRideFragment.newInstance(confirmBookingPassData);
        Slide slideTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slideTransition = new Slide(Gravity.RIGHT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        }

        ChangeBounds changeBoundsTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(slideTransition);
            fragment.setAllowEnterTransitionOverlap(true);
            fragment.setAllowReturnTransitionOverlap(true);
            fragment.setSharedElementEnterTransition(changeBoundsTransition);
        }
        replaceFragment(R.id.bottom_container, fragment, CONFIRM_FRAGMENT_TAG);

        sendScreenEventWithDelay();
    }

    private void sendScreenEventWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ScreenTracking.sendScreen(RideHomeActivity.this, RideHomeActivity.this);
            }
        }, 50);
    }

    private void onBottomContainerChangeToBookingScreen() {
        RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
//            fragment.disablePickLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mPresenter.onDestroy();
    }

    private void onBottomContainerChangeToProductListScreen() {
        if (getFragmentManager().findFragmentById(R.id.top_container) instanceof RideHomeMapFragment) {
            RideHomeMapFragment fragment = (RideHomeMapFragment) getFragmentManager().findFragmentById(R.id.top_container);
            if (fragment != null) {
                fragment.enablePickLocation();
            }
        }
    }

    @Override
    public void onBackPressed() {
        RideGATracking.eventBackPress(getScreenName());
        if (isSeatPanelShowed) {
            hideBlockTranslucentLayout();
            hideSeatPanelLayout();
        } else if (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof ConfirmBookingRideFragment) {
            getFragmentManager().popBackStack();

            onBottomContainerChangeToProductListScreen();

            ConfirmBookingRideFragment fragment = (ConfirmBookingRideFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
            ConfirmBookingPassData viewModel = fragment.getActiveConfirmBooking();
            UberProductFragment productFragment = UberProductFragment.newInstance(viewModel.getSource(),
                    viewModel.getDestination());
            replaceFragment(R.id.bottom_container, productFragment, PRODUCTS_FRAGMENT_TAG);
            sendScreenEventWithDelay();
        } else if (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof UberProductFragment &&
                mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void actionChangeSeatCount(List<SeatViewModel> seatViewModels) {
        showBlockTranslucentLayout();
        showSeatPanelLayout();

        SeatAdapter adapter = new SeatAdapter(this);
        adapter.setOnItemClickListener(this);
        seatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        seatListRecyclerView.setAdapter(adapter);
        adapter.setSeatViewModels(seatViewModels);
    }

    private void showSeatPanelLayout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isSeatPanelShowed = true;
                Animation bottomUp = AnimationUtils.loadAnimation(RideHomeActivity.this,
                        R.anim.bottom_up);

                seatPanelLayout.startAnimation(bottomUp);
                seatPanelLayout.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void showBlockTranslucentLayout() {
        blockTranslucentFrameLayout.setVisibility(View.VISIBLE);
        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(blockTranslucentFrameLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0x00000000,
                0xBB000000);
        backgroundColorAnimator.setDuration(500);
        backgroundColorAnimator.start();
    }

    private void hideBlockTranslucentLayout() {
        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(blockTranslucentFrameLayout,
                "backgroundColor",
                new ArgbEvaluator(),
                0xBB000000,
                0x00000000);
        backgroundColorAnimator.setDuration(500);
        backgroundColorAnimator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blockTranslucentFrameLayout.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void hideSeatPanelLayout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isSeatPanelShowed = false;
                Animation bottomDown = AnimationUtils.loadAnimation(RideHomeActivity.this,
                        R.anim.bottom_down);

                seatPanelLayout.startAnimation(bottomDown);
                seatPanelLayout.setVisibility(View.INVISIBLE);
            }
        }, 200);
    }

    @Override
    public void onItemClicked(SeatViewModel seatViewModel) {
        hideBlockTranslucentLayout();
        hideSeatPanelLayout();
        ConfirmBookingRideFragment productFragment = (ConfirmBookingRideFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
        if (productFragment != null) {
            productFragment.updateSeatCount(seatViewModel.getSeat());
        } else {
            throw new RuntimeException("ConfirmBookingRideFragment view is gone");
        }
    }

    @Override
    public void actionRequestRide(ConfirmBookingViewModel confirmBookingViewModel) {
        RideGATracking.eventClickRequestRideOption(getScreenName(), confirmBookingViewModel.getProductDisplayName());
        Intent intent = OnTripActivity.getCallingIntent(this, confirmBookingViewModel);
        startActivityForResult(intent, REQUEST_GO_TO_ONTRIP_REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.action_history) {
            RideGATracking.eventClickOnTrip(getScreenName());
            actionNavigateToHistory();
            return true;
        } else if (i == R.id.action_payment) {
            actionNavigateToPayment();
            return true;
        } else if (i == R.id.action_add_to_home_screen) {
            RideUtils.addUberShortcutOnLauncher(this, getString(R.string.label_book_uber_shortcut), getString(R.string.label_book_uber_shortcut));
            RideGATracking.eventUberCreateShortcut(getScreenName());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void actionNavigateToHistory() {
        Intent intent = RideHistoryActivity.getCallingIntent(this);
        startActivity(intent);
    }

    private void actionNavigateToPayment() {
        startActivity(ManagePaymentOptionsActivity.getCallingActivity(this, ManagePaymentOptionsActivity.TYPE_MANAGE_PAYMENT_OPTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_booking_ride, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public String getScreenName() {
        if (getFragmentManager() != null && (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof ConfirmBookingRideFragment)) {
            return AppScreen.SCREEN_RIDE_BOOKING;
        } else {
            return AppScreen.SCREEN_RIDE_HOME;
        }
    }

    @Override
    public boolean isUserLoggedIn() {
        return SessionHandler.isV4Login(this);
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = ((RideModuleRouter) MainApplication.getAppContext()).getLoginIntent(this);
        startActivityForResult(intent, RideHomeActivity.LOGIN_REQUEST_CODE);
    }

    @Override
    public void showVerificationPhoneNumberPage() {
        if (getApplicationContext() instanceof OtpRouter) {
            OtpRouter otpRouter = (OtpRouter) getApplicationContext();
            startActivityForResult(otpRouter.getRidePhoneNumberActivityIntent(this),
                    RIDE_PHONE_VERIFY_REQUEST_CODE);
        }
    }

    @Override
    public boolean isUserPhoneNumberVerified() {
        return SessionHandler.isMsisdnVerified();
    }

    @Override
    public void expandSlidingPanel() {
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void collapseBottomPanel() {
        if (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof UberProductFragment) {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    public void actionBackToProductList() {
        if (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof ConfirmBookingRideFragment && !isFinishing()) {
            try {
                getFragmentManager().popBackStack();
                onBottomContainerChangeToProductListScreen();
                ConfirmBookingRideFragment fragment = (ConfirmBookingRideFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
                ConfirmBookingPassData viewModel = fragment.getActiveConfirmBooking();
                UberProductFragment productFragment = UberProductFragment.newInstance(viewModel.getSource(),
                        viewModel.getDestination());
                replaceFragment(R.id.bottom_container, productFragment, PRODUCTS_FRAGMENT_TAG);
                sendScreenEventWithDelay();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public RideComponent getComponent() {
        if (rideComponent == null) initInjector();
        return rideComponent;
    }
}
