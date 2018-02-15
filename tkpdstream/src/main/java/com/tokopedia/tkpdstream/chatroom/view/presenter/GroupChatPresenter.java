package com.tokopedia.tkpdstream.chatroom.view.presenter;

import android.util.Log;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.SendGroupChatMessageUseCase;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetGroupChatMessagesFirstTimeUseCase getGroupChatMessagesFirstTimeUseCase;
    private final LoginGroupChatUseCase loginGroupChatUseCase;
    private final SendGroupChatMessageUseCase sendMessageUseCase;

    @Inject
    public GroupChatPresenter(LoginGroupChatUseCase loginGroupChatUseCase,
                              GetGroupChatMessagesFirstTimeUseCase
                                      getGroupChatMessagesFirstTimeUseCase,
                              SendGroupChatMessageUseCase sendMessageUseCase) {
        this.loginGroupChatUseCase = loginGroupChatUseCase;
        this.getGroupChatMessagesFirstTimeUseCase = getGroupChatMessagesFirstTimeUseCase;
        this.sendMessageUseCase = sendMessageUseCase;

    }

    @Override
    public void initMessageFirstTime(final String channelUrl, final OpenChannel mChannel) {
        getGroupChatMessagesFirstTimeUseCase.execute(channelUrl, mChannel,
                new GetGroupChatMessagesFirstTimeUseCase.GetGroupChatMessagesListener() {
                    @Override
                    public void onGetMessages(List<Visitable> listChat) {
                        getView().onSuccessGetMessageFirstTime(listChat);
                    }

                    @Override
                    public void onErrorGetMessagesFirstTime(SendBirdException e) {
                        Log.d("NISNIS", "onErrorGetMessagesFirstTime " + e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void sendReply(String replyText) {

    }

    @Override
    public void enterChannel(String channelUrl, LoginGroupChatUseCase.LoginGroupChatListener
            loginGroupChatListener) {
        loginGroupChatUseCase.execute(channelUrl, "Nisie123", loginGroupChatListener);
    }

    @Override
    public void logoutChannel(OpenChannel mChannel) {

    }

    @Override
    public void loadPreviousMessages() {

    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
