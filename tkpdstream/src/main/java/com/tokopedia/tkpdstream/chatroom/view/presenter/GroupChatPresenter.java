package com.tokopedia.tkpdstream.chatroom.view.presenter;

import android.util.Log;

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
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;
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

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final GetChannelInfoUseCase getChannelInfoUseCase;
    private final SendVoteUseCase sendVoteUseCase;

    private final GetGroupChatMessagesFirstTimeUseCase getGroupChatMessagesFirstTimeUseCase;
    private final RefreshMessageUseCase refreshMessageUseCase;
    private final LoadPreviousChatMessagesUseCase loadPreviousChatMessagesUseCase;
    private final LoginGroupChatUseCase loginGroupChatUseCase;
    private final SendGroupChatMessageUseCase sendMessageUseCase;
    private final LogoutGroupChatUseCase logoutGroupChatUseCase;
    private final ChannelHandlerUseCase channelHandlerUseCase;

    @Inject
    public GroupChatPresenter(LoginGroupChatUseCase loginGroupChatUseCase,
                              GetChannelInfoUseCase getChannelInfoUseCase,
                              GetGroupChatMessagesFirstTimeUseCase
                                      getGroupChatMessagesFirstTimeUseCase,
                              RefreshMessageUseCase refreshMessageUseCase,
                              LoadPreviousChatMessagesUseCase loadPreviousChatMessagesUseCase,
                              SendGroupChatMessageUseCase sendMessageUseCase,
                              LogoutGroupChatUseCase logoutGroupChatUseCase,
                              ChannelHandlerUseCase channelHandlerUseCase,
                              SendVoteUseCase sendVoteUseCase) {
        this.getChannelInfoUseCase = getChannelInfoUseCase;
        this.loginGroupChatUseCase = loginGroupChatUseCase;
        this.getGroupChatMessagesFirstTimeUseCase = getGroupChatMessagesFirstTimeUseCase;
        this.refreshMessageUseCase = refreshMessageUseCase;
        this.loadPreviousChatMessagesUseCase = loadPreviousChatMessagesUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.logoutGroupChatUseCase = logoutGroupChatUseCase;
        this.channelHandlerUseCase = channelHandlerUseCase;
        this.sendVoteUseCase = sendVoteUseCase;
    }

    @Override
    public void initMessageFirstTime(final String channelUrl, final OpenChannel mChannel) {
        getGroupChatMessagesFirstTimeUseCase.execute(getView().getContext(), channelUrl, mChannel,
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

            getChannelInfoUseCase.execute(GetChannelInfoUseCase.createParams(mChannel.getUrl()),
                    new Subscriber<ChannelInfoViewModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (getView() != null) {
                                getView().dismissReconnectingMessage();
                                getView().onErrorRefreshChannelInfo(GroupChatErrorHandler.getErrorMessage(
                                        getView().getContext(), e, true
                                ));
                            }
                        }

                        @Override
                        public void onNext(ChannelInfoViewModel channelInfoViewModel) {
                            if (getView() != null) {
                                getView().dismissReconnectingMessage();
                                getView().onSuccessRefreshChannelInfo(channelInfoViewModel);
                            }
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
                        if (getView() != null) {
                            getView().onErrorGetChannelInfo(GroupChatErrorHandler.getErrorMessage(
                                    getView().getContext(), e, false
                            ));
                        }
                    }

                    @Override
                    public void onNext(ChannelInfoViewModel channelInfoViewModel) {
                        if (getView() != null) {
                            getView().onSuccessGetChannelInfo(channelInfoViewModel);
                        }
                    }
                });
    }

    @Override
    public void sendVote(String pollId, boolean voted, final VoteViewModel element) {
        if (voted) {
            getView().showHasVoted();
        } else {
            sendVoteUseCase.execute(SendVoteUseCase.createParams(pollId,
                    element.getOptionId()), new Subscriber<VoteStatisticViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().onErrorVote(GroupChatErrorHandler.getErrorMessage(getView()
                                .getContext(), e, true));
                    }
                }

                @Override
                public void onNext(VoteStatisticViewModel voteStatisticViewModel) {
                    if (getView() != null) {
                        getView().onSuccessVote(element, voteStatisticViewModel);
                        getView().showSuccessVoted();
                    }
                }
            });
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        sendVoteUseCase.unsubscribe();
        getChannelInfoUseCase.unsubscribe();
    }

    public void setHandler(String channelUrl, ChannelHandlerUseCase.ChannelHandlerListener listener) {
        channelHandlerUseCase.execute(channelUrl, listener);
    }
}
