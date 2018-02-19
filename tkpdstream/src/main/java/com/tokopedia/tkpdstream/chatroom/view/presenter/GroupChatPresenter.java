package com.tokopedia.tkpdstream.chatroom.view.presenter;

import android.util.Log;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LogoutGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.SendGroupChatMessageUseCase;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;

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
    private final LogoutGroupChatUseCase logoutGroupChatUseCase;
    private final ChannelHandlerUseCase channelHandlerUseCase;

    @Inject
    public GroupChatPresenter(LoginGroupChatUseCase loginGroupChatUseCase,
                              GetGroupChatMessagesFirstTimeUseCase
                                      getGroupChatMessagesFirstTimeUseCase,
                              SendGroupChatMessageUseCase sendMessageUseCase,
                              LogoutGroupChatUseCase logoutGroupChatUseCase,
                              ChannelHandlerUseCase channelHandlerUseCase) {
        this.loginGroupChatUseCase = loginGroupChatUseCase;
        this.getGroupChatMessagesFirstTimeUseCase = getGroupChatMessagesFirstTimeUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.logoutGroupChatUseCase = logoutGroupChatUseCase;
        this.channelHandlerUseCase = channelHandlerUseCase;

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
    public void sendReply(final PendingChatViewModel pendingChatViewModel, OpenChannel mChannel) {
        sendMessageUseCase.execute(pendingChatViewModel, mChannel,
                new SendGroupChatMessageUseCase.SendGroupChatMessageListener() {

                    @Override
                    public void onSuccessSendMessage(ChatViewModel viewModel) {
                        getView().onSuccessSendMessage(pendingChatViewModel, viewModel);
                    }

                    @Override
                    public void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, SendBirdException e) {
                        getView().onErrorSendMessage(pendingChatViewModel, e.toString());
                    }
                });
    }

    @Override
    public void enterChannel(String userId, String channelUrl, String userName, String userAvatar,
                             LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener) {
        loginGroupChatUseCase.execute(channelUrl, userId, userName, userAvatar,
                loginGroupChatListener);
    }

    @Override
    public void logoutChannel(OpenChannel mChannel) {
        logoutGroupChatUseCase.execute(mChannel);
    }

    @Override
    public void loadPreviousMessages() {

    }

    @Override
    public void detachView() {

        super.detachView();
    }

    public void setReceiver(String channelUrl, ChannelHandlerUseCase.ChannelHandlerListener listener) {
        channelHandlerUseCase.execute(channelUrl, listener);
    }
}
