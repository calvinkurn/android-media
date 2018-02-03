package com.tokopedia.tkpdstream.channel.domain.source;

import com.tokopedia.tkpdstream.channel.domain.mapper.ChannelMapper;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.common.data.GroupChatApi;

import java.util.HashMap;

import rx.Observable;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelRepository {

    GroupChatApi chatApi;
    ChannelMapper channelMapper;

    public Observable<ChannelListViewModel> getChannels(HashMap<String, Object> parameters) {
        return chatApi.getAllChannel(parameters)
                .map(channelMapper);
    }
}
