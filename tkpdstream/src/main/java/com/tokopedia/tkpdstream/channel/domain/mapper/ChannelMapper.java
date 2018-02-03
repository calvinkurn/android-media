package com.tokopedia.tkpdstream.channel.domain.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.data.ChannelListPojo;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;

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
        return new ChannelListViewModel();
    }
}
