package com.tokopedia.seller.product.edit.data.mapper;

import com.tokopedia.seller.product.edit.data.source.cloud.model.youtube.Item;
import com.tokopedia.seller.product.edit.data.source.cloud.model.youtube.YoutubeResponse;
import com.tokopedia.seller.product.edit.domain.model.YoutubeVideoModel;

import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author normansyahputa on 4/11/17.
 */
public class YoutubeVidToDomainMapper implements Func1<Response<YoutubeResponse>, YoutubeVideoModel> {

    public static YoutubeVideoModel convert(YoutubeResponse youtubeResponse) {
        YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();

        // return invalid youtubeModel

        List<Item> items = youtubeResponse.getItems();

        if (items == null || items.size() <= 0) {
            return YoutubeVideoModel.invalidYoutubeModel();
        }

        Item item = items.get(0);
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
