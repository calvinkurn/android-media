package com.tokopedia.tkpdstream.chatroom.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.ChannelInfoPojo;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author by nisie on 2/23/18.
 */

public interface ChatroomApi {

    @GET(ChatroomUrl.GET_CHANNEL_INFO)
    Observable<Response<DataResponse<ChannelInfoPojo>>> getChannelInfo(
            @Path(ChatroomUrl.PATH_CHANNEL_UUID) String channelUuid);

}
