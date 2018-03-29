package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 3/29/18.
 */

public class VibrateViewModel implements Visitable<GroupChatTypeFactory> {
    public static final String TYPE = "is_vibrate";

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
