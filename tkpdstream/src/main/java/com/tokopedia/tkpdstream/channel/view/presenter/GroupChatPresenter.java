package com.tokopedia.tkpdstream.channel.view.presenter;

import android.util.Log;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.SendGroupChatMessageUseCase;
import com.tokopedia.tkpdstream.channel.view.listener.GroupChatContract;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetGroupChatMessagesFirstTimeUseCase getGroupChatMessagesFirstTimeUseCase;
    private final LoginGroupChatUseCase loginGroupChatUseCase;

    @Inject
    public GroupChatPresenter(LoginGroupChatUseCase loginGroupChatUseCase,
                              GetGroupChatMessagesFirstTimeUseCase
                                      getGroupChatMessagesFirstTimeUseCase,
                              SendGroupChatMessageUseCase sendMessageUseCase) {
        this.loginGroupChatUseCase = loginGroupChatUseCase;
        this.getGroupChatMessagesFirstTimeUseCase = getGroupChatMessagesFirstTimeUseCase;

    }

    @Override
    public void initMessageFirstTime(final String channelUrl, final OpenChannel mChannel) {
        ConnectionManager.addConnectionManagementHandler("Nisie123", ConnectionManager
                .CONNECTION_HANDLER_ID, new ConnectionManager.ConnectionManagementHandler() {
            @Override
            public void onConnected(boolean reconnect) {
                if (reconnect) {
                    getMessages(mChannel);
                } else {
                    getMessagesFirstTime(channelUrl, mChannel);
                }
            }
        });
    }

    @Override
    public void sendReply(String replyText) {

    }

    @Override
    public void enterChannel(String channelUrl, LoginGroupChatUseCase.LoginGroupChatListener
            loginGroupChatListener) {
        loginGroupChatUseCase.execute(channelUrl, loginGroupChatListener);
    }

    @Override
    public void logoutChannel(OpenChannel mChannel) {

    }

    private void getMessagesFirstTime(String channelUrl, OpenChannel mChannel) {
        getGroupChatMessagesFirstTimeUseCase.execute(channelUrl,mChannel,
                new GetGroupChatMessagesFirstTimeUseCase.GetGroupChatMessagesListener() {
                    @Override
                    public void onGetMessages(List<Visitable> listChat) {
                        Log.d("NISNIS", "onGetMessages");
                    }

                    @Override
                    public void onErrorGetMessagesFirstTime(SendBirdException e) {
                        Log.d("NISNIS", "onErrorGetMessagesFirstTime " + e.getLocalizedMessage());
                    }
                });
    }

    private void getMessages(OpenChannel mChannel) {

    }
}
