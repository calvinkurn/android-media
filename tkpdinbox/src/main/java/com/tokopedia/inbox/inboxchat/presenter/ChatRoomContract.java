package com.tokopedia.inbox.inboxchat.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class ChatRoomContract {

    public interface View extends CustomerView{

        Bundle getArguments();

        void showLoading();

        void finishLoading();

        void setHeader();

        void setTextAreaReply(boolean b);

        ChatRoomAdapter getAdapter();

        Context getContext();

        void scrollTo(int i);

        String getReplyMessage();

        String  getString(int error_empty_report);

        void showError(String string);

        void onSuccessSendReply(ReplyActionData data, String reply);

        void setViewEnabled(boolean b);

        void addDummyMessage();

        RefreshHandler getRefreshHandler();

        void setCanLoadMore(boolean hasNext);

        void scrollToBottom();

        void hideMainLoading();

        void setOnlineDesc(String s);

        WebSocketInterface getInterface();
    }

    interface Presenter extends CustomerPresenter<View>{
    }
}
