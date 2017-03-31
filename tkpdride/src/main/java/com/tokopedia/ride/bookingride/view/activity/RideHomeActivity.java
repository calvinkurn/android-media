package com.tokopedia.ride.bookingride.view.activity;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.view.adapter.SeatAdapter;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.SeatViewModel;
import com.tokopedia.ride.bookingride.view.fragment.ConfirmBookingRideFragment;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeFragment;
import com.tokopedia.ride.bookingride.view.fragment.UberProductFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.ontrip.view.OnTripActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.ride.bookingride.view.fragment.ConfirmBookingRideFragment.EXTRA_PRODUCT;

@RuntimePermissions
public class RideHomeActivity extends BaseActivity implements RideHomeFragment.OnFragmentInteractionListener,
        UberProductFragment.OnFragmentInteractionListener, ConfirmBookingRideFragment.OnFragmentInteractionListener, SeatAdapter.OnItemClickListener {

    public static int REQUEST_GO_TO_ONTRIP_CODE = 1009;
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

    private int mSlidingPanelMinHeightInPx, mToolBarHeightinPx;

    private RideConfiguration configuration;
    private boolean isSeatPanelShowed;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, RideHomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ride);
        unbinder = ButterKnife.bind(this);

        RideHomeActivityPermissionsDispatcher.initFragmentWithCheck(this);

        mSlidingPanelMinHeightInPx = (int) getResources().getDimension(R.dimen.sliding_panel_min_height);
        mToolBarHeightinPx = (int) getResources().getDimension(R.dimen.tooler_height);

        configuration = new RideConfiguration();
        if (configuration.isWaitingDriverState()) {
            Intent intent = OnTripActivity.getCallingIntent(this);
            startActivityForResult(intent, REQUEST_GO_TO_ONTRIP_CODE);
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void initFragment() {
        addFragment(R.id.top_container, RideHomeFragment.newInstance());
        addFragment(R.id.bottom_container, UberProductFragment.newInstance());
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RideHomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onSourceAndDestinationChanged(PlacePassViewModel source, PlacePassViewModel destination) {
        UberProductFragment productFragment = (UberProductFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
        if (productFragment != null) {
            productFragment.updateProductList(source, destination);
        } else {
            productFragment = UberProductFragment.newInstance(source, destination);
            addFragment(R.id.bottom_container, productFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RideHomeFragment.REQUEST_CHECK_LOCATION_SETTINGS) {
            RideHomeFragment fragment = (RideHomeFragment) getFragmentManager().findFragmentById(R.id.top_container);
            if (fragment != null) {
                fragment.handleLocationAlertResult(resultCode);
            }
        } else if (requestCode == REQUEST_GO_TO_ONTRIP_CODE) {
            switch (resultCode) {
                case OnTripActivity.APP_HOME_RESULT_CODE:
                    finish();
                    break;
                case OnTripActivity.RIDE_HOME_RESULT_CODE:
                    RideHomeActivityPermissionsDispatcher.initFragmentWithCheck(this);
                    break;
                case OnTripActivity.RIDE_BOOKING_RESULT_CODE:
                    break;
            }
        }
    }

    @Override
    public void animateBottomPanelOnMapDragging() {
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        //running after 100 ms to allow to panel get collapsed first
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomContainer.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(mSlidingPanelMinHeightInPx).setDuration(300);
            }
        }, 100);
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
    public void onMinimumTimeEstCalculated(String timeEst) {
        RideHomeFragment fragment = (RideHomeFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.setMarkerText(timeEst);
        } else {
            fragment = RideHomeFragment.newInstance();
            addFragment(R.id.top_container, fragment);
            fragment.setMarkerText(timeEst);
        }
    }

    @Override
    public void showEnterDestError() {
        RideHomeFragment fragment = (RideHomeFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.showEnterDestError();
        }
    }

    @Override
    public void showEnterSourceLocationActiity() {
        RideHomeFragment fragment = (RideHomeFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.actionSourceButtonClicked();
        }
    }

    @Override
    public void onProductClicked(ConfirmBookingViewModel rideProductViewModel) {
        onBottomContainerChangeToBookingScreen();
//        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        mSlidingUpPanelLayout.setPanelHeight(50);
        mSlidingUpPanelLayout.setParallaxOffset(5);

        ConfirmBookingRideFragment fragment = ConfirmBookingRideFragment.newInstance(rideProductViewModel);
        Slide slideTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slideTransition = new Slide(Gravity.END);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        }

        ChangeBounds changeBoundsTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(slideTransition);
            fragment.setAllowEnterTransitionOverlap(true);
            fragment.setAllowReturnTransitionOverlap(true);
            fragment.setSharedElementEnterTransition(changeBoundsTransition);
        }
        replaceFragment(R.id.bottom_container, fragment);
    }

    private void onBottomContainerChangeToBookingScreen() {
        RideHomeFragment fragment = (RideHomeFragment) getFragmentManager().findFragmentById(R.id.top_container);
        if (fragment != null) {
            fragment.disablePickLocation();
        } else {
            fragment = RideHomeFragment.newInstance();
            addFragment(R.id.top_container, fragment);
            fragment.disablePickLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (isSeatPanelShowed) {
            hideBlockTranslucentLayout();
            hideSeatPanelLayout();
        } else if (getFragmentManager().findFragmentById(R.id.bottom_container) instanceof ConfirmBookingRideFragment) {
            hideBlockTranslucentLayout();
            hideSeatPanelLayout();
            getFragmentManager().popBackStack();

            ConfirmBookingRideFragment fragment = (ConfirmBookingRideFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
            ConfirmBookingViewModel viewModel = fragment.getActiveConfirmBooking();
            UberProductFragment productFragment = UberProductFragment.newInstance(viewModel.getSource(),
                    viewModel.getDestination());
            replaceFragment(R.id.bottom_container, productFragment);
//            mSlidingUpPanelLayout.setPanelHeight(Float.floatToIntBits(getResources().getDimension(R.dimen.sliding_panel_min_height)));
//            mSlidingUpPanelLayout.setParallaxOffset(Float.floatToIntBits(getResources().getDimension(R.dimen.tooler_height)));
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
        blockTranslucentFrameLayout.setVisibility(View.GONE);
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
        seatPanelLayout.setVisibility(View.GONE);
        ConfirmBookingRideFragment productFragment = (ConfirmBookingRideFragment) getFragmentManager().findFragmentById(R.id.bottom_container);
        if (productFragment != null) {
            productFragment.updateSeatCount(seatViewModel.getSeat());
        } else {
            throw new RuntimeException("ConfirmBookingRideFragment view is gone");
        }
    }

    @Override
    public void actionRequestRide(ConfirmBookingViewModel confirmBookingViewModel) {
        Intent intent = OnTripActivity.getCallingIntent(this, confirmBookingViewModel);
        startActivityForResult(intent, REQUEST_GO_TO_ONTRIP_CODE);
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
}
