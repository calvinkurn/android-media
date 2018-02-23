package com.tokopedia.tkpdstream.chatroom.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ChannelInfoPojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 2/23/18.
 */

public interface ChatroomApi {

    @GET(ChatroomUrl.GET_CHANNEL_INFO)
    Observable<Response<DataResponse<ChannelInfoPojo>>> getChannelInfo(
            @Path(ChatroomUrl.PATH_CHANNEL_UUID) String channelUuid,
            @QueryMap Map<String, Object> keyword);

}
