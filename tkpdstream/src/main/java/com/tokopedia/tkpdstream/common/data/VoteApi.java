package com.tokopedia.tkpdstream.common.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.vote.data.VoteInfoPojo;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public interface VoteApi {

    @GET(GroupChatUrl.GET_ALL_CHANNEL)
    Observable<Response<DataResponse>> getVoteInfo(@QueryMap Map<String,
                Object> keyword);
}
