package com.tokopedia.seller.product.data.source.cloud;

import com.tokopedia.seller.product.data.mapper.YoutubeVidToDomainMapper;
import com.tokopedia.seller.product.data.source.cloud.api.YoutubeVideoLinkApi;
import com.tokopedia.seller.product.domain.model.YoutubeVideoModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 4/11/17.
 */

public class YoutubeVideoLinkCloud {
    private YoutubeVideoLinkApi youtubeVideoLinkApi;
    private YoutubeVidToDomainMapper youtubeVidToDomainMapper;

    @Inject
    public YoutubeVideoLinkCloud(YoutubeVideoLinkApi youtubeVideoLinkApi,
                                 YoutubeVidToDomainMapper youtubeVidToDomainMapper) {
        this.youtubeVideoLinkApi = youtubeVideoLinkApi;
        this.youtubeVidToDomainMapper = youtubeVidToDomainMapper;
    }

    public Observable<YoutubeVideoModel> fetchDataFromNetwork(String videoId, String keyId) {
        return youtubeVideoLinkApi.getVideoDetail(generateParam(videoId, keyId))
                .map(youtubeVidToDomainMapper);
    }

    private Map<String, String> generateParam(String videoId, String keyId) {
        Map<String, String> param = new HashMap<>();
        param.put("part", "snippet,contentDetails");
        param.put("id", videoId);
        param.put("key", keyId);
        return param;
    }
}
