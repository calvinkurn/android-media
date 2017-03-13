package com.tokopedia.ride;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

public class BookingRideActivity extends BaseActivity {
    public static final int REQUEST_CODE_LOGIN = 1005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ride);
        if (SessionHandler.isV4Login(this)){
            Intent intent = SessionRouter.getLoginActivityIntent(this);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_LOGIN:
                    Toast.makeText(this, "User Logged In", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
