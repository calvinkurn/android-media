package com.tokopedia.tkpdstream.channel.domain.source;

import com.tokopedia.tkpdstream.channel.domain.mapper.ChannelMapper;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.channel.data.ChannelApi;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelSource {

    ChannelApi chatApi;
    ChannelMapper channelMapper;

    @Inject
    public ChannelSource(@StreamScope ChannelApi chatApi,
                         ChannelMapper channelMapper) {
        this.chatApi = chatApi;
        this.channelMapper = channelMapper;
    }

    public Observable<ChannelListViewModel> getChannels(HashMap<String, Object> parameters) {
        return chatApi.getAllChannel(parameters)
                .map(channelMapper);
    }
}
