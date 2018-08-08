package com.tokopedia.seller.product.edit.data.source.cloud;

import com.tokopedia.seller.product.edit.data.source.cloud.api.YoutubeVideoLinkApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.youtube.YoutubeResponse;
import com.tokopedia.seller.product.edit.utils.YoutubeVideoLinkUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by normansyahputa on 4/11/17.
 */

public class YoutubeVideoLinkCloud {
    private YoutubeVideoLinkApi youtubeVideoLinkApi;
    private YoutubeVideoLinkUtils youtubeVideoLinkUtils;

    @Inject
    public YoutubeVideoLinkCloud(YoutubeVideoLinkApi youtubeVideoLinkApi, YoutubeVideoLinkUtils youtubeVideoLinkUtils) {
        this.youtubeVideoLinkApi = youtubeVideoLinkApi;
        this.youtubeVideoLinkUtils = youtubeVideoLinkUtils;
    }

    public Observable<Response<YoutubeResponse>> fetchDataFromNetwork(final String videoId, String keyId) {
        return youtubeVideoLinkApi
                .getVideoDetail(generateParam(videoId, keyId))
                .doOnNext(new Action1<Response<YoutubeResponse>>() {
                    @Override
                    public void call(Response<YoutubeResponse> youtubeResponseResponse) {
                        if (youtubeResponseResponse.isSuccessful()) {
                            if (youtubeResponseResponse.errorBody() != null) {
                                throw new RuntimeException(youtubeResponseResponse.errorBody().toString());
                            } else {
                                youtubeVideoLinkUtils.checkIfVideoExists(youtubeResponseResponse.body(), videoId);

                                youtubeVideoLinkUtils.checkIfVideoNotAgeRestricted(youtubeResponseResponse.body());
                            }
                        }
                    }
                });
    }

    private Map<String, String> generateParam(String videoId, String keyId) {
        Map<String, String> param = new HashMap<>();
        param.put("part", "snippet,contentDetails");
        param.put("id", videoId);
        param.put("key", keyId);
        return param;
    }
}
