package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;

import com.sendbird.android.OpenChannel;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;

import java.util.List;

/**
 * @author by nisie on 2/6/18.
 */

public interface GroupChatContract {

    interface View extends CustomerView {

        Context getContext();

        void onSuccessGetMessage(List<Visitable> listChat);

        void onSuccessGetMessageFirstTime(List<Visitable> listChat);
    }

    interface Presenter extends CustomerPresenter<View> {

        void initMessageFirstTime(String channelUrl, OpenChannel mChannel);

        void sendReply(String replyText);

        void enterChannel(String s, LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener);

        void logoutChannel(OpenChannel mChannel);

        void loadPreviousMessages();
    }
}
