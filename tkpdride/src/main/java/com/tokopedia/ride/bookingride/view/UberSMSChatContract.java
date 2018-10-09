package com.tokopedia.ride.bookingride.view;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.chat.utils.ChatMessage;

import java.util.ArrayList;

/**
 * Created by sachinbansal on 3/6/18.
 */

public interface UberSMSChatContract {

    interface View extends CustomerView {
        void finish();

        void startActivity(Intent intent);

        void startActivityForResult(Intent intent, int requestCode);

        void interruptToLoginPage();

        void addMessages(ArrayList<ChatMessage> chatMessageArrayList);

        Activity getActivity();

        void setMessageSentStatus(ChatMessage.DeliveryStatus deliveryStatus, int intExtra);

        void onSMSReceived(String s, long timeStamp);

    }

    interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void fetchSMSHistory(String phoneno);

        void actionCallUberDriver(String phoneNo);

        void openDialerIntent(String phoneNo);

        void openSMSIntent(String phoneNo);

        boolean sendSMS(String phoneNo, ChatMessage chatMessage);

        void onDestroy();

    }
}
