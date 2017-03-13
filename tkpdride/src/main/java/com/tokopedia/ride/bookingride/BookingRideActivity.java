package com.tokopedia.ride.bookingride;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.ride.R;

public class BookingRideActivity extends BaseActivity implements BookingRideContract.View {
    public static final int REQUEST_CODE_LOGIN = 1005;

    private BookingRideContract.Presenter presenter;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, BookingRideActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ride);
        presenter = new BookingRidePresenter();
        presenter.attachView(this);
        presenter.initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    Toast.makeText(this, "User Logged In", Toast.LENGTH_SHORT).show();
                    recreate();
                    break;
            }
        }
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public boolean isUserLoggedIn() {
        return SessionHandler.isV4Login(this);
    }

    @Override
    public void navigateToLoginPage() {
        Intent intent = SessionRouter.getLoginActivityIntent(this);
        intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                TkpdState.DrawerPosition.LOGIN);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void showVerificationPhoneNumberDialog() {

    }

    @Override
    public boolean isUserPhoneNumberVerified() {
        return SessionHandler.isMsisdnVerified();
    }

    @Override
    public void prepareMainView() {

    }
}
