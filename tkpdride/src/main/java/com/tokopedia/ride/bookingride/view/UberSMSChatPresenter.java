package com.tokopedia.ride.bookingride.view;

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
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.chat.utils.ChatMessage;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by sachinbansal on 3/6/18.
 */

public class UberSMSChatPresenter extends BaseDaggerPresenter<UberSMSChatContract.View>
        implements UberSMSChatContract.Presenter {

    private static final String MESSAGE_ID = "Message_id";

    private final String INBOX_URI = "content://sms/inbox";
    private final String SENT_URI = "content://sms/sent";

    private final String ID = "id";
    private final String ADDRESS = "address";
    private final String PERSON = "person";
    private final String BODY = "body";
    private final String DATE = "date";
    private final String TYPE = "type";

    private ArrayList<ChatMessage> sentMessagesArrayList = new ArrayList<>();
    private ArrayList<ChatMessage> receivedMessagesArrayList = new ArrayList<>();
    private ArrayList<ChatMessage> chatArrayList = new ArrayList<>();
    private String phoneNo;

    private static final String SMS_SENT_ACTION = "SMS_SENT_ACTION";
    private static final String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

    private BroadcastReceiver receiveSMSBroadcastReceiver;
    private BroadcastReceiver sentSMSStatusBroadcastReceiver;
    private BroadcastReceiver deliveryReportBroadcastReceiver;

    @Inject
    public UberSMSChatPresenter() {
    }

    @Override
    public void initialize() {

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
                        getView().onSMSReceived(smsBody.toString(), timeStamp);
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(1000);
        getView().getActivity().registerReceiver(receiveSMSBroadcastReceiver, intentFilter);


        sentSMSStatusBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        getView().setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_SUCCESS, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        getView().setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        getView().setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        getView().setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        getView().setMessageSentStatus(ChatMessage.DeliveryStatus.SENT_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));
                        break;
                }
            }
        };

        getView().getActivity().registerReceiver(sentSMSStatusBroadcastReceiver, new IntentFilter(SMS_SENT_ACTION));


        deliveryReportBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        getView().setMessageSentStatus(ChatMessage.DeliveryStatus.DELIVER_SUCCESS, intent.getIntExtra(MESSAGE_ID, -1));

                        break;
                    case Activity.RESULT_CANCELED:
                        getView().setMessageSentStatus(ChatMessage.DeliveryStatus.DELIVER_FAILURE, intent.getIntExtra(MESSAGE_ID, -1));

                        break;
                }
            }
        };
        getView().getActivity().registerReceiver(deliveryReportBroadcastReceiver, new IntentFilter(SMS_DELIVERED_ACTION));
    }

    @Override
    public void fetchSMSHistory(String phoneNo) {
        this.phoneNo = phoneNo;
        readSentSMS();
        readInboxSMS();
        mergeSMS();
    }

    @Override
    public void actionCallUberDriver(String phoneNo) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        getView().startActivity(callIntent);
    }

    @Override
    public void openDialerIntent(String phoneNo) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        getView().startActivity(callIntent);
    }

    @Override
    public void openSMSIntent(String phoneNo) {
        if (!TextUtils.isEmpty(phoneNo)) {
            getView().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.fromParts("sms", phoneNo, null))
            );
        }
    }

    @Override
    public boolean sendSMS(String phoneNo, ChatMessage chatMessage) {
        try {
            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(phoneNo, null, chatMessage.getMessage(),
                    PendingIntent.getBroadcast(getView().getActivity(), 0,
                            new Intent(SMS_SENT_ACTION).putExtra(MESSAGE_ID, chatMessage.getId()),
                            PendingIntent.FLAG_CANCEL_CURRENT),

                    PendingIntent.getBroadcast(getView().getActivity(), 0,
                            new Intent(SMS_DELIVERED_ACTION).putExtra(MESSAGE_ID, chatMessage.getId()),
                            PendingIntent.FLAG_CANCEL_CURRENT));

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void onDestroy() {

        if (receiveSMSBroadcastReceiver != null)
            getView().getActivity().unregisterReceiver(receiveSMSBroadcastReceiver);
        if (sentSMSStatusBroadcastReceiver != null)
            getView().getActivity().unregisterReceiver(sentSMSStatusBroadcastReceiver);
        if (deliveryReportBroadcastReceiver != null)
            getView().getActivity().unregisterReceiver(deliveryReportBroadcastReceiver);

        detachView();
    }

    private void readInboxSMS() {
        String[] projection = new String[]{ID, ADDRESS, PERSON, BODY, DATE, TYPE};
        try {
            Cursor cur = getView().getActivity().getContentResolver().query(Uri.parse(INBOX_URI), projection, "address='" + phoneNo + "'", null, "date asc");
            if (cur != null && cur.moveToFirst()) {
                receivedMessagesArrayList.clear();
                int index_Body = cur.getColumnIndex(BODY);
                int index_Date = cur.getColumnIndex(DATE);
                do {
                    ChatMessage chatMessage = new ChatMessage();
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
        String[] projection = new String[]{ID, ADDRESS, PERSON, BODY, DATE, TYPE};
        try {
            Cursor cur = getView().getActivity().getContentResolver().query(Uri.parse(SENT_URI), projection, "address='" + phoneNo + "'", null, "date asc");
            if (cur != null && cur.moveToFirst()) {
                sentMessagesArrayList.clear();
                int index_Body = cur.getColumnIndex(BODY);
                int index_Date = cur.getColumnIndex(DATE);
                do {
                    ChatMessage chatMessage = new ChatMessage();
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);

                    chatMessage.setMessage(strbody);
                    chatMessage.setTimestamp(longDate);
                    chatMessage.setType(ChatMessage.Type.SENT);
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

        getView().addMessages(chatArrayList);
    }
}
