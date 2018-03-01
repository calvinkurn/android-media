package com.tokopedia.tkpdstream.channel.domain.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.domain.pojo.Channel;
import com.tokopedia.tkpdstream.channel.domain.pojo.ChannelListPojo;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelMapper implements Func1<Response<DataResponse<ChannelListPojo>>, ChannelListViewModel> {

    private static final String LAST_PAGE = "last";

    @Inject
    public ChannelMapper() {
    }

    @Override
    public ChannelListViewModel call(Response<DataResponse<ChannelListPojo>> dataResponseResponse) {
        ChannelListPojo pojo = dataResponseResponse.body().getData();

        return new ChannelListViewModel(getListChannel(pojo),
                hasNextPage(pojo.getCursor()));
    }

    private boolean hasNextPage(String cursor) {
        return !cursor.equals(LAST_PAGE);
    }

    private List<ChannelViewModel> getListChannel(ChannelListPojo pojo) {
        List<ChannelViewModel> list = new ArrayList<>();
        for (Channel channelPojo : pojo.getChannels()) {
            list.add(convertToViewModel(channelPojo));
        }
        return list;
    }

    private ChannelViewModel convertToViewModel(Channel channelPojo) {
        return new ChannelViewModel(
                String.valueOf(channelPojo.getChannelId()),
                channelPojo.getModeratorName()!= null ? channelPojo.getModeratorName() : "",
                channelPojo.getCoverUrl()!= null ? channelPojo.getCoverUrl() : "",
                channelPojo.getModeratorThumbUrl() != null ? channelPojo.getModeratorThumbUrl() : "",
                channelPojo.getTitle() != null? channelPojo.getTitle() : "",
                channelPojo.getDescription() != null ? channelPojo.getDescription() : "",
                channelPojo.getTotalParticipantsOnline()
        );
    }
}
