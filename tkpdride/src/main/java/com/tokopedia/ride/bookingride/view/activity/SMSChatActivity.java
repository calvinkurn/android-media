package com.tokopedia.ride.bookingride.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.ride.R;
import com.tokopedia.ride.chat.utils.ChatMessage;
import com.tokopedia.ride.chat.utils.ChatView;
import com.tokopedia.ride.common.ride.domain.model.Driver;

import java.util.ArrayList;

/**
 * Created by sachinbansal on 2/13/18.
 */

public class SMSChatActivity extends BaseActivity {

    private static final String TAG = "SMSChatActivity";
    private static final int SMS_PERMISSION_CODE = 0;
    private static final int CALL_PERMISSION_CODE = 1;
    private String PHONE_NO = "";

    private String INBOX_URI = "content://sms/inbox";
    private String SENT_URI = "content://sms/sent";
    private String ALL_SMS_URI = "content://sms/";
    private String SMS_URI_ALL = "content://mms-sms/conversations?simple=true";
    private ChatView chatView;
    private BroadcastReceiver broadcastReceiver;
    private Driver driverDetails;

    public static final String DRIVER_INFO = "DRIVER_INFO";
    private ImageView driverImageView;
    private TextView driverPhoneTV;
    private TextView driverNameTV;
    private ArrayList<ChatMessage> sentMessagesArrayList = new ArrayList<>();
    private ArrayList<ChatMessage> receivedMessagesArrayList = new ArrayList<>();
    private ArrayList<ChatMessage> chatArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_chat);

        chatView = findViewById(R.id.chat_view);

        driverDetails = getIntent().getParcelableExtra(DRIVER_INFO);
        PHONE_NO = driverDetails.getPhoneNumber();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
        }

        View view = getLayoutInflater().inflate(R.layout.custom_toolbar, null);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(view, layout);
        driverNameTV = view.findViewById(R.id.driver_name);
        driverPhoneTV = view.findViewById(R.id.driver_phone);
        driverImageView = view.findViewById(R.id.driver_image);
        initToolbar();

        proceed();
    }

    private void initToolbar() {

        driverNameTV.setText(driverDetails.getName());
        driverPhoneTV.setText(driverDetails.getPhoneNumber());

        Glide.with(this).load(driverDetails.getPictureUrl())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_user_pic_light)
                .into(new BitmapImageViewTarget(driverImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        if (!isFinishing()) {
                            RoundedBitmapDrawable roundedBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            roundedBitmapDrawable.setCircular(true);
                            driverImageView.setImageDrawable(roundedBitmapDrawable);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);

        MenuItem menuItem = menu.findItem(R.id.call_driver);

        if (menuItem != null) {

            Drawable normalDrawable = menuItem.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, SMSChatActivity.this.getResources().getColor(R.color.white));

            menuItem.setIcon(wrapDrawable);
        }


        return super.onCreateOptionsMenu(menu);
    }

    public void readInboxSMS() {

        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Cursor cur = getContentResolver().query(Uri.parse(INBOX_URI), projection, "address='" + PHONE_NO + "'", null, "date asc");
            if (cur.moveToFirst()) {
                receivedMessagesArrayList.clear();
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {

                    ChatMessage chatMessage = new ChatMessage();

                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    chatMessage.setMessage(strbody);
                    chatMessage.setTimestamp(longDate);
                    chatMessage.setType(ChatMessage.Type.RECEIVED);
                    chatMessage.setSender(strAddress);

                    receivedMessagesArrayList.add(chatMessage);
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }


        /*int senderIndex = smsInboxCursor.getColumnIndex("address");
        int messageIndex = smsInboxCursor.getColumnIndex("body");
        if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;
        do {
            String sender = smsInboxCursor.getString(senderIndex);
            String message = smsInboxCursor.getString(messageIndex);
        } while (smsInboxCursor.moveToNext());


        contentResolver = getContentResolver();
        final String[] projection = new String[]{"*"};
        Uri uri = Uri.parse("content://mms-sms/conversations/");
        Cursor query = contentResolver.query(uri, projection, null, null, null);

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();*/
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

    private void readSentSMS() {

        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Cursor cur = getContentResolver().query(Uri.parse(SENT_URI), projection, "address='" + PHONE_NO + "'", null, "date asc");
            if (cur.moveToFirst()) {

                sentMessagesArrayList.clear();

                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    ChatMessage chatMessage = new ChatMessage();
                    String strAddress = cur.getString(index_Address);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);

                    chatMessage.setMessage(strbody);
                    chatMessage.setTimestamp(longDate);
                    chatMessage.setType(ChatMessage.Type.SENT);
                    chatMessage.setSender(strAddress);

                    sentMessagesArrayList.add(chatMessage);

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.call_driver) {
            if (!TextUtils.isEmpty(PHONE_NO)) {
                if (!hasMakeCallPermission()) {
                    requestMakeCallPermission();
                } else {
                    callDriver();
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void proceed() {

        showRequestPermissionsInfoAlertDialog();

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {

                return !TextUtils.isEmpty(chatMessage.getMessage()) && sendSMS(PHONE_NO, chatMessage.getMessage());
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                abortBroadcast();
                if (intent.getAction() != null && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                    String smsSender = "";
                    StringBuilder smsBody = new StringBuilder();
                    long timeStamp = 0l;
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

                    if (smsSender.equals(PHONE_NO) /*&& smsBody.toString().startsWith(serviceProviderSmsCondition)*/) {
                        onSMSReceived(smsBody.toString(), timeStamp);
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
//        IntentFilter localIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(2000);
        registerReceiver(broadcastReceiver, intentFilter);

//        readSMS();
    }

    private void onSMSReceived(String string, long timestamp, String senderName) {
        ChatMessage chatMessage = new ChatMessage(string, timestamp, ChatMessage.Type.RECEIVED, senderName);
        chatView.addMessage(chatMessage);
    }

    private void onSMSReceived(String string, long timestamp) {
        ChatMessage chatMessage = new ChatMessage(string, timestamp, ChatMessage.Type.RECEIVED);
        chatView.addMessage(chatMessage);
    }


    private void showRequestPermissionsInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!hasReadSendSmsPermission()) {
                    requestReadAndSendSmsPermission();
                } else {
                    readSentSMS();
                    readInboxSMS();
                    mergeSMS();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public boolean sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);

            return true;
            /*Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();*/
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
            return false;
        }
    }


    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";


        } while (smsInboxCursor.moveToNext());
    }

    public void readSMS() {

        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Cursor cur = getContentResolver().query(Uri.parse(ALL_SMS_URI), projection, "address='" + PHONE_NO + "'", null, "date asc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(longDate + ", ");
                    smsBuilder.append(int_Type);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }


        /*int senderIndex = smsInboxCursor.getColumnIndex("address");
        int messageIndex = smsInboxCursor.getColumnIndex("body");
        if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;
        do {
            String sender = smsInboxCursor.getString(senderIndex);
            String message = smsInboxCursor.getString(messageIndex);
        } while (smsInboxCursor.moveToNext());


        contentResolver = getContentResolver();
        final String[] projection = new String[]{"*"};
        Uri uri = Uri.parse("content://mms-sms/conversations/");
        Cursor query = contentResolver.query(uri, projection, null, null, null);

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();*/
    }

    private boolean hasReadSendSmsPermission() {

        return RequestPermissionUtil.checkHasPermission(this, Manifest.permission.READ_SMS) &&
                RequestPermissionUtil.checkHasPermission(this, Manifest.permission.RECEIVE_SMS) &&
                RequestPermissionUtil.checkHasPermission(this, Manifest.permission.SEND_SMS) &&
                RequestPermissionUtil.checkHasPermission(this, Manifest.permission.READ_PHONE_STATE);
    }

    private boolean hasMakeCallPermission() {
        return ActivityCompat.checkSelfPermission(SMSChatActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(SMSChatActivity.this,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(SMSChatActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(SMSChatActivity.this, new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.

            readInboxSMS();
            readSentSMS();
            mergeSMS();
        } else if (requestCode == CALL_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callDriver();
        }
    }

    private void callDriver() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + PHONE_NO));
        SMSChatActivity.this.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SMS_PERMISSION_CODE && resultCode == RESULT_OK) {
            readSMS();
        }
    }

    private void requestMakeCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SMSChatActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(SMSChatActivity.this, new String[]{Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE}, CALL_PERMISSION_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }


}
