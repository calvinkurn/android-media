package com.tokopedia.ride.bookingride.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.chat.utils.ChatMessage;
import com.tokopedia.ride.chat.utils.ChatView;
import com.tokopedia.ride.common.ride.domain.model.Driver;
import com.tokopedia.ride.common.ride.domain.model.Vehicle;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by sachinbansal on 2/13/18.
 */
@RuntimePermissions
public class UberSMSChatActivity extends BaseActivity {

    private static final String TAG = "UberSMSChatActivity";
    private static final String MESSAGE_ID = "Message_id";
    private String phoneNo = "";

    private final String INBOX_URI = "content://sms/inbox";
    private final String SENT_URI = "content://sms/sent";
    private ChatView chatView;
    private BroadcastReceiver receiveSMSBroadcastReceiver;
    private BroadcastReceiver sentSMSStatusBroadcastReceiver;
    private BroadcastReceiver deliveryReportBroadcastReceiver;
    private Driver driverDetails;

    public static final String DRIVER_INFO = "DRIVER_INFO";
    public static final String VEHICLE_INFO = "VEHICLE_INFO";
    private ImageView driverImageView;
    private TextView licensePlateTV;
    private TextView driverNameTV;
    private ArrayList<ChatMessage> sentMessagesArrayList = new ArrayList<>();
    private ArrayList<ChatMessage> receivedMessagesArrayList = new ArrayList<>();
    private ArrayList<ChatMessage> chatArrayList = new ArrayList<>();
    private Vehicle vehicleDetails;

    public static final String SMS_SENT_ACTION = "SMS_SENT_ACTION";
    public static final String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";


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
        proceed();
    }

    private void initToolbar() {

        driverNameTV.setText(driverDetails.getName());
        String vehicleInfo = vehicleDetails.getVehicleModel() + " | " + vehicleDetails.getLicensePlate();
        licensePlateTV.setText(vehicleInfo);

        ImageHandler.loadCircleImageWithPlaceHolder(this, driverImageView, R.drawable.default_user_pic_light, driverDetails.getPictureUrl());
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
            UberSMSChatActivityPermissionsDispatcher.openCallIntentWithCheck(this, phoneNo);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @NeedsPermission({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    public void openCallIntent(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    @OnPermissionDenied({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void requestCallPermissionDenied() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE})
    void requestCallPermissionNeverAsk() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
    }

    private void proceed() {

        showChatHistory();

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {

                return !TextUtils.isEmpty(chatMessage.getMessage()) && sendSMS(phoneNo, chatMessage.getMessage(), chatMessage.getId());
            }
        });


        chatView.setOnSendSMSRetry(new ChatView.OnSendSMSRetry() {
            @Override
            public void onTapRetry(ChatMessage chatMessage) {
                sendSMS(phoneNo, chatMessage.getMessage(), chatMessage.getId());
            }
        });

        receiveSMSBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                    String smsSender = "";
                    StringBuilder smsBody = new StringBuilder();
                    long timeStamp = 0L;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                            smsSender = smsMessage.getDisplayOriginatingAddress();
                            smsBody.append(smsMessage.getMessageBody());
                            timeStamp = smsMessage.getTimestampMillis();
                        }
                    } else {
                        Bundle smsBundle = intent.getExtras();
                        if (smsBundle != null) {
                            Object[] pdus = (Object[]) smsBundle.get("pdus");
                            if (pdus == null) {
                                // Display some error to the user
                                Log.e(TAG, "SmsBundle had no pdus key");
                                return;
                            }
                            SmsMessage[] messages = new SmsMessage[pdus.length];
                            for (int i = 0; i < messages.length; i++) {
                                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                smsBody.append(messages[i].getMessageBody());
                            }
                            smsSender = messages[0].getOriginatingAddress();
                            timeStamp = messages[0].getTimestampMillis();
                        }
                    }

                    if (smsSender.equals(phoneNo)) {
                        onSMSReceived(smsBody.toString(), timeStamp);
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(1000);
        registerReceiver(receiveSMSBroadcastReceiver, intentFilter);


        sentSMSStatusBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_SUCCESS, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                }
            }
        };
        registerReceiver(sentSMSStatusBroadcastReceiver, new IntentFilter(SMS_SENT_ACTION));


        deliveryReportBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        setMessageSentStatus(ChatMessage.DeliveryStatus.DELIVER_SUCCESS, intent.getIntExtra(MESSAGE_ID, -1));

                        break;
                    case Activity.RESULT_CANCELED:
                        setMessageSentStatus(ChatMessage.DeliveryStatus.DELIVER_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));

                        break;
                }
            }
        };
        registerReceiver(deliveryReportBroadcastReceiver, new IntentFilter(SMS_DELIVERED_ACTION));
    }

    private void setMessageSentStatus(ChatMessage.DeliveryStatus deliveryStatus, int id) {
        chatView.updateMessageSentStatus(deliveryStatus, id);
    }


    private void onSMSReceived(String message, long timestamp) {
        ChatMessage chatMessage = new ChatMessage(message, timestamp, ChatMessage.Type.RECEIVED);
        chatView.addMessage(chatMessage);
    }

    public boolean sendSMS(String phoneNo, String msg, int id) {
        try {
            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(phoneNo, null, msg, PendingIntent.getBroadcast(
                    this, 0, new Intent(SMS_SENT_ACTION).putExtra(MESSAGE_ID, id), 0),

                    PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED_ACTION).putExtra(MESSAGE_ID, id), 0));

            return true;
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
            return false;
        }
    }


    private void showChatHistory() {
        UberSMSChatActivityPermissionsDispatcher.fetchSMSHistoryWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE})
    public void fetchSMSHistory() {
        readSentSMS();
        readInboxSMS();
        mergeSMS();
    }


    @OnPermissionDenied({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE})
    void requestSMSPermissionDenied() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE})
    void requestSMSPermissionNeverAsk() {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UberSMSChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiveSMSBroadcastReceiver != null)
            unregisterReceiver(receiveSMSBroadcastReceiver);
        if (sentSMSStatusBroadcastReceiver != null)
            unregisterReceiver(sentSMSStatusBroadcastReceiver);
        if (deliveryReportBroadcastReceiver != null)
            unregisterReceiver(deliveryReportBroadcastReceiver);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_UBER_SMS_CHAT;
    }

    public void readInboxSMS() {

        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};

        try {
            Cursor cur = getContentResolver().query(Uri.parse(INBOX_URI), projection, "address='" + phoneNo + "'", null, "date asc");
            if (cur != null && cur.moveToFirst()) {
                receivedMessagesArrayList.clear();
                int index_Address = cur.getColumnIndex("address");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");

                do {
                    ChatMessage chatMessage = new ChatMessage();

                    String strAddress = cur.getString(index_Address);
                    String strBody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);

                    chatMessage.setMessage(strBody);
                    chatMessage.setTimestamp(longDate);
                    chatMessage.setType(ChatMessage.Type.RECEIVED);
                    receivedMessagesArrayList.add(chatMessage);

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    private void readSentSMS() {

        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};

        try {
            Cursor cur = getContentResolver().query(Uri.parse(SENT_URI), projection, "address='" + phoneNo + "'", null, "date asc");
            if (cur != null && cur.moveToFirst()) {
                sentMessagesArrayList.clear();
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                do {
                    ChatMessage chatMessage = new ChatMessage();
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);

                    chatMessage.setMessage(strbody);
                    chatMessage.setTimestamp(longDate);
                    chatMessage.setType(ChatMessage.Type.SENT);
                    // TODO: 2/21/18 need to check
                    chatMessage.setDeliveryStatus(ChatMessage.DeliveryStatus.DELIVER_SUCCESS);

                    sentMessagesArrayList.add(chatMessage);

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    private void mergeSMS() {

        int receiveSMSCount = receivedMessagesArrayList.size();
        int sentSMSCount = sentMessagesArrayList.size();

        int i = 0, j = 0;
        chatArrayList.clear();

        while (i < receiveSMSCount && j < sentSMSCount) {

            if (receivedMessagesArrayList.get(i).getTimestamp() < sentMessagesArrayList.get(j).getTimestamp()) {
                chatArrayList.add(receivedMessagesArrayList.get(i));
                i++;
            } else {
                chatArrayList.add(sentMessagesArrayList.get(j));
                j++;
            }
        }

        for (int k = i; k < receiveSMSCount; k++)
            chatArrayList.add(receivedMessagesArrayList.get(k));

        for (int k = j; k < sentSMSCount; k++)
            chatArrayList.add(sentMessagesArrayList.get(k));

        chatView.addMessages(chatArrayList);

    }

}