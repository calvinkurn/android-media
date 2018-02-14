package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.sendbird.android.BaseMessage;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.MyChatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatMessagesMapper {

    @Inject
    public GroupChatMessagesMapper() {
    }

    public List<Visitable> map(List<BaseMessage> list) {
        List<Visitable> listViewModel = new ArrayList<>();
        for(BaseMessage message : list){
            listViewModel.add(mapMessageByType(message));
        }
    }

    private Visitable mapMessageByType(BaseMessage message) {
        return new MyChatViewModel();
    }
}
