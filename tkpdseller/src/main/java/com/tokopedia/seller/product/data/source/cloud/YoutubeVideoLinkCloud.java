package com.tokopedia.seller.product.data.source.cloud;

import com.tokopedia.seller.product.data.source.cloud.api.YoutubeVideoLinkApi;
import com.tokopedia.seller.product.data.source.cloud.model.youtube.YoutubeResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 4/11/17.
 */

public class YoutubeVideoLinkCloud {
    private YoutubeVideoLinkApi youtubeVideoLinkApi;

    @Inject
    public YoutubeVideoLinkCloud(YoutubeVideoLinkApi youtubeVideoLinkApi) {
        this.youtubeVideoLinkApi = youtubeVideoLinkApi;
    }

    public Observable<Response<YoutubeResponse>> fetchDataFromNetwork(String videoId, String keyId) {
        return youtubeVideoLinkApi.getVideoDetail(generateParam(videoId, keyId));
    }

    private Map<String, String> generateParam(String videoId, String keyId) {
        Map<String, String> param = new HashMap<>();
        param.put("part", "snippet,contentDetails");
        param.put("id", videoId);
        param.put("key", keyId);
        return param;
    }
}
