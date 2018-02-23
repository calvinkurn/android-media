package com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.AdminAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.UserActionViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public interface GroupChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(AdminAnnouncementViewModel adminAnnouncementViewModel);

    int type(ChatViewModel myChatViewModel);

    int type(PendingChatViewModel pendingChatViewModel);

    int type(UserActionViewModel userActionViewModel);


}
