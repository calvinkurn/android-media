//package com.tokopedia.util;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.telephony.SmsMessage;
//
//import com.tokopedia.core.util.MethodChecker;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.inject.Inject;
//
///**
// * @author by Nisie on 7/14/16.
// */
//public class IncomingSmsReceiver extends BroadcastReceiver {
//
//    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
//
//    public interface ReceiveSMSListener {
//        void onReceiveOTP(String otpCode);
//    }
//
//    ReceiveSMSListener listener;
//
//    @Inject
//    public IncomingSmsReceiver() {
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        try {
//            final Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                SmsMessage currentMessage;
//                if (MethodChecker.createSmsFromPdu(intent) != null) {
//                    currentMessage = MethodChecker.createSmsFromPdu(intent);
//
//                    if (isTokopediaOtpSms(currentMessage)) {
//                        String regexString = Pattern.quote("Tokopedia - ") + "(.*?)" + Pattern.quote("adalah");
//                        Pattern pattern = Pattern.compile(regexString);
//                        Matcher matcher = pattern.matcher(currentMessage.getDisplayMessageBody());
//
//                        while (matcher.find()) {
//                            String otpCode = matcher.group(1).trim();
//                            if (listener != null) {
//                                listener.onReceiveOTP(otpCode);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean isTokopediaOtpSms(SmsMessage currentMessage) {
//        String senderNum = currentMessage.getDisplayOriginatingAddress() != null ?
//                currentMessage.getDisplayOriginatingAddress() : "";
//        String message = currentMessage.getDisplayMessageBody() != null ?
//                currentMessage.getDisplayMessageBody() : "";
//
//        return (senderNum != null && senderNum.equals("Tokopedia")) ||
//                (message != null && message.startsWith("Tokopedia"));
//    }
//
//    public void registerSmsReceiver(Context context) {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ACTION_SMS_RECEIVED);
//        context.registerReceiver(this, filter);
//    }
//
//    public void setListener(ReceiveSMSListener listener) {
//        this.listener = listener;
//    }
//}