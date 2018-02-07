package com.tokopedia.tkpdstream.channel.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.channel.view.adapter.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/7/18.
 */

public class MyChatViewModel implements Visitable<GroupChatTypeFactory> {

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
