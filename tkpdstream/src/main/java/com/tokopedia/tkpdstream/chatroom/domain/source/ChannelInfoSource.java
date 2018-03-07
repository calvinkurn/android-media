package com.tokopedia.tkpdstream.chatroom.domain.source;

import com.tokopedia.tkpdstream.chatroom.data.ChatroomApi;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.ChannelInfoMapper;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoSource {

    ChatroomApi chatroomApi;
    ChannelInfoMapper mapper;

    @Inject
    public ChannelInfoSource(ChatroomApi chatroomApi, ChannelInfoMapper mapper) {
        this.chatroomApi = chatroomApi;
        this.mapper = mapper;
    }

    public Observable<ChannelInfoViewModel> getChannelInfo(String channelUuid) {
        return chatroomApi.getChannelInfo(channelUuid)
                .map(mapper);
    }
}
