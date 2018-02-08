package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoActView;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;

import java.util.List;

/**
 * @author normansyahputa on 4/17/17.
 */
public abstract class YoutubeAddVideoPresenter extends BaseDaggerPresenter<YoutubeAddVideoView> {


    /**
     * @param youtubeUrl full youtube url
     */
    public abstract void fetchYoutubeDescription(String youtubeUrl);

    /**
     * @param videoId only video id
     */
    public abstract void fetchYoutube(String videoId);

    public abstract void fetchYoutube(List<String> videoIds);

    public abstract void setYoutubeActView(YoutubeAddVideoActView youtubeActView);
}
