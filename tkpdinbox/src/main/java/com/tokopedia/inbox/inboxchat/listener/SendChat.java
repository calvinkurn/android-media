package com.tokopedia.inbox.inboxchat.listener;

import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 10/25/17.
 */

public interface SendChat {

    interface View extends CustomerView {

        void addUrlToReply(String url);

        Context getContext();

        void removeError();

        String getString(int resId);

        void setContentError(String errorMessage);

        void showDummyMessage(String message);

        void setActionsEnabled(boolean isEnabled);

        void onSuccessSendMessage();

        void onErrorSendMessage(String errorMessage);

        void removeDummyMessage();
    }

    interface Presenter extends CustomerPresenter<View> {
        void sendMessage(String message, String source, String string, String s);

        void getAttachProductDialog(String senderId, String role);
    }

}
