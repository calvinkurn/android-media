package com.tokopedia.tkpdstream.chatroom.view.presenter;

import android.content.Context;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ChannelInfoPojo;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetChannelInfoUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LogoutGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.SendGroupChatMessageUseCase;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;
import com.tokopedia.tkpdstream.vote.domain.usecase.GetVoteUseCase;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetChannelInfoUseCase getChannelInfoUseCase;
    private final GetGroupChatMessagesFirstTimeUseCase getGroupChatMessagesFirstTimeUseCase;
    private final GetGroupChatMessagesUseCase getGroupChatMessagesUseCase;
    private final LoginGroupChatUseCase loginGroupChatUseCase;
    private final SendGroupChatMessageUseCase sendMessageUseCase;
    private final LogoutGroupChatUseCase logoutGroupChatUseCase;
    private final ChannelHandlerUseCase channelHandlerUseCase;
    private final GetVoteUseCase getVoteUseCase;

    @Inject
    public GroupChatPresenter(LoginGroupChatUseCase loginGroupChatUseCase,
                              GetChannelInfoUseCase getChannelInfoUseCase,
                              GetGroupChatMessagesFirstTimeUseCase
                                      getGroupChatMessagesFirstTimeUseCase,
                              GetGroupChatMessagesUseCase getGroupChatMessagesUseCase,
                              SendGroupChatMessageUseCase sendMessageUseCase,
                              LogoutGroupChatUseCase logoutGroupChatUseCase,
                              ChannelHandlerUseCase channelHandlerUseCase,
                              GetVoteUseCase getVoteUseCase) {
        this.getChannelInfoUseCase = getChannelInfoUseCase;
        this.loginGroupChatUseCase = loginGroupChatUseCase;
        this.getGroupChatMessagesFirstTimeUseCase = getGroupChatMessagesFirstTimeUseCase;
        this.getGroupChatMessagesUseCase = getGroupChatMessagesUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.logoutGroupChatUseCase = logoutGroupChatUseCase;
        this.channelHandlerUseCase = channelHandlerUseCase;
        this.getVoteUseCase = getVoteUseCase;

    }

    @Override
    public void initMessageFirstTime(final String channelUrl, final OpenChannel mChannel) {
        getGroupChatMessagesFirstTimeUseCase.execute(getView().getContext(), channelUrl, mChannel,
                new GetGroupChatMessagesFirstTimeUseCase.GetGroupChatMessagesFirstTimeListener() {
                    @Override
                    public void onGetMessagesFirstTime(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
                        getView().onSuccessGetMessageFirstTime(listChat, previousMessageListQuery);
                    }

                    @Override
                    public void onErrorGetMessagesFirstTime(String errorMessage) {
                        getView().onErrorGetMessageFirstTime(errorMessage);
                    }
                });
    }

    @Override
    public void sendReply(final PendingChatViewModel pendingChatViewModel, OpenChannel mChannel) {
        sendMessageUseCase.execute(getView().getContext(), pendingChatViewModel, mChannel,
                new SendGroupChatMessageUseCase.SendGroupChatMessageListener() {

                    @Override
                    public void onSuccessSendMessage(ChatViewModel viewModel) {
                        getView().onSuccessSendMessage(pendingChatViewModel, viewModel);
                    }

                    @Override
                    public void onErrorSendMessage(PendingChatViewModel pendingChatViewModel,
                                                   String errorMessage) {
                        getView().onErrorSendMessage(pendingChatViewModel, errorMessage);
                    }
                });
    }

    @Override
    public void enterChannel(String userId, String channelUrl, String userName, String userAvatar,
                             LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener) {
        loginGroupChatUseCase.execute(getView().getContext(), channelUrl, userId, userName,
                userAvatar,
                loginGroupChatListener);
    }

    @Override
    public void logoutChannel(OpenChannel mChannel) {
        logoutGroupChatUseCase.execute(mChannel);
    }

    @Override
    public void loadPreviousMessages(OpenChannel mChannel, PreviousMessageListQuery mPrevMessageListQuery) {
        if (mChannel != null && mPrevMessageListQuery != null) {
            getGroupChatMessagesUseCase.execute(getView().getContext(), mPrevMessageListQuery, new
                    GetGroupChatMessagesUseCase.GetGroupChatMessagesListener() {
                        @Override
                        public void onGetMessages(List<Visitable> listChat) {
                            getView().onSuccessGetMessage(listChat);
                        }

                        @Override
                        public void onErrorGetMessages(String errorMessage) {
                            getView().onErrorGetMessage(errorMessage);
                        }
                    });
        }
    }

    @Override
    public void shareChatRoom(GroupChatViewModel viewModel) {

    }

    @Override
    public void getChannelInfo(String channelUuid) {
        getChannelInfoUseCase.execute(GetChannelInfoUseCase.createParams(channelUuid),
                new Subscriber<ChannelInfoViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorGetChannelInfo(GroupChatErrorHandler.getErrorMessage(
                                getView().getContext(), e, false
                        ));
                    }

                    @Override
                    public void onNext(ChannelInfoViewModel channelInfoViewModel) {
                        getView().onSuccessGetChannelInfo(channelInfoViewModel);
                    }
                });
    }

    @Override
    public void getVoteInfo(final Context context) {
        getVoteUseCase.execute(getVoteUseCase.createParams(), new Subscriber<VoteInfoViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                ErrorHandler.getErrorMessage(context, throwable);
            }

            @Override
            public void onNext(VoteInfoViewModel voteInfoViewModel) {
                getView().onSuccessGetVoteInfo(voteInfoViewModel);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void setHandler(String channelUrl, ChannelHandlerUseCase.ChannelHandlerListener listener) {
        channelHandlerUseCase.execute(channelUrl, listener);
    }
}
