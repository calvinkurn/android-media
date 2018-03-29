package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.VibrateViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.AdminAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public interface GroupChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(AdminAnnouncementViewModel adminAnnouncementViewModel);

    int type(ChatViewModel myChatViewModel);

    int type(PendingChatViewModel pendingChatViewModel);

    int type(UserActionViewModel userActionViewModel);

    int type(ImageAnnouncementViewModel imageViewModel);

    int type(VoteAnnouncementViewModel voteAnnouncementViewModel);

    int type (SprintSaleAnnouncementViewModel flashSaleViewModel);

    int type(GroupChatPointsViewModel groupChatPointsViewModel);

    int type (VibrateViewModel vibrateViewModel);
}
