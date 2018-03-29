package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;

import java.util.List;

/**
 * @author by nisie on 2/6/18.
 */

public interface ChatroomContract {

    interface View extends CustomerView {

        Context getContext();

        void onSuccessGetPreviousMessage(List<Visitable> listChat);

        void onSuccessGetMessageFirstTime(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery);

        void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, String errorMessage);

        void onSuccessSendMessage(PendingChatViewModel pendingChatViewModel, ChatViewModel viewModel);

        void onErrorGetMessage(String errorMessage);

        void onErrorGetMessageFirstTime(String errorMessage);

        void showLoadingPreviousList();

        void dismissLoadingPreviousList();

        void showReconnectingMessage();

        void dismissReconnectingMessage();

        void onSuccessRefreshReconnect(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery);

        void refreshChat();

        void setReplyTextHint();

        void setSprintSaleIcon(SprintSaleViewModel sprintSaleViewModel);

        void autoAddSprintSaleAnnouncement(SprintSaleViewModel sprintSaleViewModel, ChannelInfoViewModel channelInfoViewModel);

        interface ImageAnnouncementViewHolderListener {
            void onImageAnnouncementClicked(String url);
        }

        interface VoteAnnouncementViewHolderListener {
            void onVoteComponentClicked(String type, String name);
        }

        interface SprintSaleViewHolderListener {
            void onSprintSaleProductClicked(SprintSaleProductViewModel sprintSaleViewModel, int
                    position);

            void onSprintSaleComponentClicked(SprintSaleAnnouncementViewModel sprintSaleAnnouncementViewModel);

            void onSprintSaleIconClicked(SprintSaleViewModel sprintSaleViewModel);

        }
    }

    interface Presenter extends CustomerPresenter<View> {

        void initMessageFirstTime(OpenChannel mChannel);

        void sendReply(PendingChatViewModel pendingChatViewModel, OpenChannel mChannel);

        void loadPreviousMessages(OpenChannel mChannel, PreviousMessageListQuery mPrevMessageListQuery);

        void refreshDataAfterReconnect(OpenChannel mChannel);
    }
}
