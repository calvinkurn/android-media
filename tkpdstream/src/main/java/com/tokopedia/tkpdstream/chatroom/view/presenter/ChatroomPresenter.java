package com.tokopedia.tkpdstream.chatroom.view.presenter;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetChannelInfoUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoadPreviousChatMessagesUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LogoutGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.RefreshMessageUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.SendGroupChatMessageUseCase;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChatroomContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;
import com.tokopedia.tkpdstream.vote.domain.usecase.SendVoteUseCase;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 2/6/18.
 */

public class ChatroomPresenter extends BaseDaggerPresenter<ChatroomContract.View> implements
        ChatroomContract.Presenter {

    private final SendVoteUseCase sendVoteUseCase;

    private final GetGroupChatMessagesFirstTimeUseCase getGroupChatMessagesFirstTimeUseCase;
    private final RefreshMessageUseCase refreshMessageUseCase;
    private final LoadPreviousChatMessagesUseCase loadPreviousChatMessagesUseCase;
    private final SendGroupChatMessageUseCase sendMessageUseCase;

    @Inject
    public ChatroomPresenter(GetGroupChatMessagesFirstTimeUseCase
                                     getGroupChatMessagesFirstTimeUseCase,
                             RefreshMessageUseCase refreshMessageUseCase,
                             LoadPreviousChatMessagesUseCase loadPreviousChatMessagesUseCase,
                             SendGroupChatMessageUseCase sendMessageUseCase,
                             SendVoteUseCase sendVoteUseCase) {
        this.getGroupChatMessagesFirstTimeUseCase = getGroupChatMessagesFirstTimeUseCase;
        this.refreshMessageUseCase = refreshMessageUseCase;
        this.loadPreviousChatMessagesUseCase = loadPreviousChatMessagesUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.sendVoteUseCase = sendVoteUseCase;
    }

    @Override
    public void initMessageFirstTime(final OpenChannel mChannel) {
        if (mChannel != null) {
            getGroupChatMessagesFirstTimeUseCase.execute(getView().getContext(), mChannel.getUrl(), mChannel,
                    new GetGroupChatMessagesFirstTimeUseCase.GetGroupChatMessagesFirstTimeListener() {
                        @Override
                        public void onGetMessagesFirstTime(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
                            if (getView() != null) {
                                getView().onSuccessGetMessageFirstTime(listChat, previousMessageListQuery);
                            }
                        }

                        @Override
                        public void onErrorGetMessagesFirstTime(String errorMessage) {
                            if (getView() != null) {
                                getView().onErrorGetMessageFirstTime(errorMessage);
                            }
                        }
                    });
        }
    }

    @Override
    public void sendReply(final PendingChatViewModel pendingChatViewModel, OpenChannel mChannel) {
        sendMessageUseCase.execute(getView().getContext(), pendingChatViewModel, mChannel,
                new SendGroupChatMessageUseCase.SendGroupChatMessageListener() {

                    @Override
                    public void onSuccessSendMessage(ChatViewModel viewModel) {
                        if (getView() != null) {
                            getView().onSuccessSendMessage(pendingChatViewModel, viewModel);
                        }
                    }

                    @Override
                    public void onErrorSendMessage(PendingChatViewModel pendingChatViewModel,
                                                   String errorMessage) {
                        if (getView() != null) {
                            getView().onErrorSendMessage(pendingChatViewModel, errorMessage);
                        }
                    }
                });
    }

    @Override
    public void loadPreviousMessages(OpenChannel mChannel, PreviousMessageListQuery mPrevMessageListQuery) {
        if (mChannel != null && mPrevMessageListQuery != null && mPrevMessageListQuery.hasMore()) {
            getView().showLoadingPreviousList();
            loadPreviousChatMessagesUseCase.execute(getView().getContext(), mPrevMessageListQuery, new
                    LoadPreviousChatMessagesUseCase.LoadPreviousChatMessagesListener() {
                        @Override
                        public void onGetPreviousMessages(List<Visitable> listChat) {
                            if (getView() != null) {
                                getView().dismissLoadingPreviousList();
                                getView().onSuccessGetPreviousMessage(listChat);
                            }
                        }

                        @Override
                        public void onErrorGetPreviousMessages(String errorMessage) {
                            if (getView() != null) {
                                getView().dismissLoadingPreviousList();
                                getView().onErrorGetMessage(errorMessage);
                            }
                        }
                    });
        }
    }

    @Override
    public void refreshDataAfterReconnect(OpenChannel mChannel) {
        if (mChannel != null) {
            getView().showReconnectingMessage();
            refreshMessageUseCase.execute(getView().getContext(), mChannel, new RefreshMessageUseCase.RefreshMessagesListener() {
                @Override
                public void onSuccessRefreshMessage(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
                    if (getView() != null) {
                        getView().dismissReconnectingMessage();
                        getView().onSuccessRefreshReconnect(listChat, previousMessageListQuery);
                    }
                }

                @Override
                public void onErrorRefreshMessage(String errorMessage) {
                    if (getView() != null) {
                        getView().dismissReconnectingMessage();
                        getView().onErrorGetMessage(errorMessage);
                    }
                }
            });
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        sendVoteUseCase.unsubscribe();
    }

}
