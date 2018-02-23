package com.tokopedia.tkpdstream.channel.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.domain.pojo.ChannelListPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ChannelInfoPojo;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 2/3/18.
 */

public interface ChannelApi {

    @GET(ChannelUrl.GET_ALL_CHANNEL)
    Observable<Response<DataResponse<List<ChannelListPojo>>>> getAllChannel(@QueryMap Map<String,
            Object> keyword);
}
