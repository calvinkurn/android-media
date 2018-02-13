package com.tokopedia.tkpdstream.channel.domain.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.data.ChannelListPojo;
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

public class ChannelMapper implements Func1<Response<DataResponse<List<ChannelListPojo>>>, ChannelListViewModel> {

    @Inject
    public ChannelMapper() {
    }

    @Override
    public ChannelListViewModel call(Response<DataResponse<List<ChannelListPojo>>> dataResponseResponse) {

        List<ChannelViewModel> list = new ArrayList<>();
        String dummyImage = "http://www.behindthevoiceactors.com/_img/games/banner_11.jpg";
        String dummyProfile = "https://orig00.deviantart.net/80ce/f/2007/349/d/f/__kingdom_hearts___coded___by_mazjojo.jpg";
        for (int i = 0; i < 10; i++) {
            ChannelViewModel channelViewModel = new ChannelViewModel("id"+i, "name"+i, dummyImage, dummyProfile, "title"+i, "subtitle"+i, i);
            list.add(channelViewModel);
        }

        return new ChannelListViewModel(list);
    }
}
