package com.tokopedia.ride.bookingride.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.RideModuleRouter;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.view.UberSMSChatContract;
import com.tokopedia.ride.bookingride.view.UberSMSChatPresenter;
import com.tokopedia.ride.chat.utils.ChatMessage;
import com.tokopedia.ride.chat.utils.ChatView;
import com.tokopedia.ride.common.ride.di.DaggerRideComponent;
import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

import java.util.ArrayList;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by sachinbansal on 2/13/18.
 */
@RuntimePermissions
public class UberSMSChatActivity extends BaseActivity implements UberSMSChatContract.View,
        HasComponent<RideComponent> {

    private String phoneNo = "";
    private ChatView chatView;
    public static final String DRIVER_INFO = "DRIVER_INFO";
    public static final String VEHICLE_INFO = "VEHICLE_INFO";
    private ImageView driverImageView;
    private TextView licensePlateTV;
    private TextView driverNameTV;
    private Vehicle vehicleDetails;
    private Driver driverDetails;

    @Inject
    UberSMSChatPresenter presenter;
    private RideComponent rideComponent;

    public static Intent newInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, UberSMSChatActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_chat);
        chatView = findViewById(R.id.chat_view);
        chatView.setSendActionButton(false);

        driverDetails = getIntent().getParcelableExtra(DRIVER_INFO);
        vehicleDetails = getIntent().getParcelableExtra(VEHICLE_INFO);

        phoneNo = driverDetails.getPhoneNumber();
        phoneNo = driverDetails.getPhoneNumber().replaceAll("[-,(]", "");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_new_action_back);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
        }

        View view = getLayoutInflater().inflate(R.layout.custom_toolbar, null);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(view, layout);
        driverNameTV = view.findViewById(R.id.driver_name);
        licensePlateTV = view.findViewById(R.id.driver_phone);
        driverImageView = view.findViewById(R.id.driver_image);

        initToolbar();
        executeInjector();
        proceed();
    }

    private void initToolbar() {

        driverNameTV.setText(driverDetails.getName());
        String vehicleInfo = vehicleDetails.getVehicleModel() + " | " + vehicleDetails.getLicensePlate();
        licensePlateTV.setText(vehicleInfo);

        ImageHandler.loadCircleImageWithPlaceHolder(this, driverImageView, R.drawable.default_user_pic_light, driverDetails.getPictureUrl());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            RideGATracking.eventClickHomeUberSMS(getScreenName());
            onBackPressed();
            return true;
        } else if (id == R.id.call_driver) {
            RideGATracking.eventClickCallUberSMS(getScreenName());
            UberSMSChatActivityPermissionsDispatcher.openCallIntentWithCheck(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @NeedsPermission({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    public void openCallIntent() {
        presenter.actionCallUberDriver(phoneNo);
    }

    @OnPermissionDenied({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void requestCallPermissionDenied() {
        Toast.makeText(this, getResources().getString(R.string.permission_error_call), Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void requestCallPermissionNeverAsk() {
        presenter.openDialerIntent(phoneNo);
    }

    private void proceed() {

        presenter.attachView(this);
        presenter.initialize();

        UberSMSChatActivityPermissionsDispatcher.fetchSMSHistoryWithCheck(this);

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {

                return !TextUtils.isEmpty(chatMessage.getMessage()) && presenter.sendSMS(phoneNo, chatMessage);
            }
        });

        chatView.setOnSendSMSRetry(new ChatView.OnSendSMSRetry() {
            @Override
            public void onTapRetry(ChatMessage chatMessage) {
                presenter.sendSMS(phoneNo, chatMessage);
            }
        });

    }

    @Override
    public void setMessageSentStatus(ChatMessage.DeliveryStatus deliveryStatus, int intExtra) {
        chatView.updateMessageSentStatus(deliveryStatus, intExtra);
    }

    @Override
    public void onSMSReceived(String message, long timeStamp) {
        ChatMessage chatMessage = new ChatMessage(message, timeStamp, ChatMessage.Type.RECEIVED);
        chatView.addMessage(chatMessage);
    }


    @NeedsPermission({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE})
    public void fetchSMSHistory() {
        presenter.fetchSMSHistory(phoneNo);
        chatView.setSendActionButton(true);
    }


    @OnPermissionDenied({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE})
    void requestSMSPermissionDenied() {
        chatView.setSendActionButton(false);
        Toast.makeText(this, getResources().getString(R.string.permission_error_send_sms), Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE})
    void requestSMSPermissionNeverAsk() {
        chatView.setSendActionButton(false);
        presenter.openSMSIntent(phoneNo);
        UberSMSChatActivity.this.finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UberSMSChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_UBER_SMS_CHAT;
    }

    @Override
    public void interruptToLoginPage() {
        Intent intent = ((RideModuleRouter) MainApplication.getAppContext()).getLoginIntent(this);
        startActivityForResult(intent, RideHomeActivity.LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RideHomeActivity.LOGIN_REQUEST_CODE) {
            if (!SessionHandler.isV4Login(this)) {
                finish();
            } else {
                chatView.clearMessages();
                proceed();
            }
        }
    }

    @Override
    public void addMessages(ArrayList<ChatMessage> chatMessageArrayList) {
        chatView.addMessages(chatMessageArrayList);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RideComponent getComponent() {
        if (rideComponent == null) initInjector();
        return rideComponent;
    }
}