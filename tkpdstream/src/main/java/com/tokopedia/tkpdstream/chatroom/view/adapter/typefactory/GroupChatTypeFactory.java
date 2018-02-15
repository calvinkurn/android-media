package com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.AdminAnnouncementViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public interface GroupChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(AdminAnnouncementViewModel oppositeChatViewModel);

    int type(ChatViewModel myChatViewModel);

}
