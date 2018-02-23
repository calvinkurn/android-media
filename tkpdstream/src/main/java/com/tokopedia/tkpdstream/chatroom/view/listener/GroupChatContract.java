package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

import java.util.List;

/**
 * @author by nisie on 2/6/18.
 */

public interface GroupChatContract {

    interface View extends CustomerView {

        Context getContext();

        void onSuccessGetMessage(List<Visitable> listChat);

        void onSuccessGetMessageFirstTime(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery);

        void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, String errorMessage);

        void onSuccessSendMessage(PendingChatViewModel pendingChatViewModel, ChatViewModel viewModel);

        void onErrorGetMessage(String errorMessage);

        void onErrorGetMessageFirstTime(String errorMessage);

        void onSuccessGetVoteInfo(VoteInfoViewModel voteInfoViewModel);

        void onErrorGetChannelInfo(String errorMessage);

        void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {

        void initMessageFirstTime(String channelUrl, OpenChannel mChannel);

        void sendReply(PendingChatViewModel pendingChatViewModel, OpenChannel mChannel);

        void enterChannel(String userId, String channelUrl, String userName, String userAvatar,
                          LoginGroupChatUseCase.LoginGroupChatListener loginGroupChatListener);

        void logoutChannel(OpenChannel mChannel);

        void loadPreviousMessages(OpenChannel mChannel, PreviousMessageListQuery mPrevMessageListQuery);

        void shareChatRoom(GroupChatViewModel viewModel);

        void getVoteInfo(Context context);

        void getChannelInfo(String channelUuid);
    }
}
