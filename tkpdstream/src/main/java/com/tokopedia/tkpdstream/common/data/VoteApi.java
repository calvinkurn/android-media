package com.tokopedia.tkpdstream.common.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.vote.domain.pojo.SendVotePojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public interface VoteApi {

    @GET(VoteUrl.SEND_VOTE)
    Observable<Response<DataResponse<SendVotePojo>>> sendVote(@Path(VoteUrl.PATH_POLL_ID) String pollId,
                                                              @QueryMap Map<String, Object> keyword);
}
