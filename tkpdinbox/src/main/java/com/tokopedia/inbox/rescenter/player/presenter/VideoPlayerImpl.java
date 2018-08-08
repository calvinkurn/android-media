package com.tokopedia.inbox.rescenter.player.presenter;

import com.tokopedia.inbox.rescenter.player.listener.VideoPlayerView;

/**
 * Created by hangnadi on 2/9/17.
 */
public class VideoPlayerImpl implements VideoPlayerPresenter {

    private final VideoPlayerView viewListener;

    public VideoPlayerImpl(VideoPlayerView viewListener) {
        this.viewListener = viewListener;
    }
}
