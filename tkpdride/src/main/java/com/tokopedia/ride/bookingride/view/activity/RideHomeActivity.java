package com.tokopedia.ride.bookingride.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.fragment.ConfirmBookingRideFragment;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeFragment;
import com.tokopedia.ride.bookingride.view.fragment.UberProductFragment;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.ride.bookingride.view.fragment.ConfirmBookingRideFragment.EXTRA_PRODUCT;

@RuntimePermissions
public class RideHomeActivity extends BaseActivity implements RideHomeFragment.OnFragmentInteractionListener,
        UberProductFragment.OnFragmentInteractionListener {

    private Unbinder unbinder;

    @BindView(R2.id.cabs_sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R2.id.bottom_container)
    FrameLayout mBottomContainer;

    private int mSlidingPanelMinHeightInPx, mToolBarHeightinPx;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, RideHomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ride);
        unbinder = ButterKnife.bind(this);

        RideHomeActivityPermissionsDispatcher.initFragmentWithCheck(this);

        //mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.cabs_sliding_layout);
        mSlidingPanelMinHeightInPx = (int) getResources().getDimension(R.dimen.sliding_panel_min_height);
        mToolBarHeightinPx = (int) getResources().getDimension(R.dimen.tooler_height);
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
        UberProductFragment productFragment = (UberProductFragment) getFragmenKtManager().findFragmentById(R.id.bottom_container);
        if (productFragment != null) {
            productFragment.updateProductList(source, destination);
        } else {
            productFragment = UberProductFragment.newInstance();
            addFragment(R.id.bottom_container, productFragment);
            productFragment.updateProductList(source, destination);
        }
    }

    @Override
    public void animateBottomPanelOnMapDragging() {
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        mBottomContainer.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(mSlidingPanelMinHeightInPx).setDuration(300);
    }

    @Override
    public void animateBottomPanelOnMapStopped() {
        mBottomContainer.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(300);
    }

    @Override
    public void onProductClicked(RideProductViewModel rideProductViewModel) {
        onBottomContainerChangeToBookingScreen();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PRODUCT, rideProductViewModel);
        ConfirmBookingRideFragment fragment = ConfirmBookingRideFragment.newInstance(bundle);
        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        fragment.setEnterTransition(slideTransition);
        fragment.setAllowEnterTransitionOverlap(true);
        fragment.setAllowReturnTransitionOverlap(true);
        fragment.setSharedElementEnterTransition(changeBoundsTransition);
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
        super.onBackPressed();
    }
}
