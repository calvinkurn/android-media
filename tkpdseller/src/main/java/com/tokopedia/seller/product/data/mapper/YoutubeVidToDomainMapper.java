package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.youtube.Item;
import com.tokopedia.seller.product.data.source.cloud.model.youtube.YoutubeResponse;
import com.tokopedia.seller.product.domain.model.YoutubeVideoModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author normansyahputa on 4/11/17.
 */
public class YoutubeVidToDomainMapper implements Func1<Response<YoutubeResponse>, YoutubeVideoModel> {

    public static YoutubeVideoModel convert(YoutubeResponse youtubeResponse) {
        YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
        Item item = youtubeResponse.getItems().get(0);
        youtubeVideoModel.setSnippetTitle(item.getSnippet().getTitle());
        youtubeVideoModel.setSnippetDescription(item.getSnippet().getDescription());

        youtubeVideoModel.setHeight(item.getSnippet().getThumbnails().getDefault().getHeight());
        youtubeVideoModel.setWidth(item.getSnippet().getThumbnails().getDefault().getWidth());
        youtubeVideoModel.setThumbnailUrl(item.getSnippet().getThumbnails().getDefault().getUrl());

        return youtubeVideoModel;
    }

    @Override
    public YoutubeVideoModel call(Response<YoutubeResponse> youtubeResponseResponse) {
        return convert(youtubeResponseResponse.body());
    }
}
