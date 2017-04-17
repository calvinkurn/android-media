package com.tokopedia.seller.product.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.YoutubeVideoUseCase;
import com.tokopedia.seller.product.domain.model.YoutubeVideoModel;
import com.tokopedia.seller.product.utils.YoutubeVideoLinkUtils;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

import rx.Subscriber;

/**
 * @author normansyahputa on 4/17/17.
 */
public class YoutubeAddVideoPresenterImpl extends YoutubeAddVideoPresenter {
    private static final String TAG = "YoutubeAddVideoPresente";
    private YoutubeVideoUseCase youtubeVideoUseCase;
    private YoutubeVideoLinkUtils youtubeVideoLinkUtils;

    public YoutubeAddVideoPresenterImpl(
            YoutubeVideoUseCase youtubeVideoUseCase,
            YoutubeVideoLinkUtils youtubeVideoLinkUtils
    ) {
        this.youtubeVideoUseCase = youtubeVideoUseCase;
        this.youtubeVideoLinkUtils = youtubeVideoLinkUtils;
    }

    public void fetchYoutubeDescription(String youtubeUrl) {
        youtubeVideoLinkUtils.setYoutubeUrl(youtubeUrl);
        String videoId = youtubeVideoLinkUtils.saveVideoID();
        RequestParams requestParams = generateParam(videoId);
        youtubeVideoUseCase.execute(requestParams, new DefaultSubscriber(videoId));
    }

    protected RequestParams generateParam(String videoId) {
        return youtubeVideoUseCase.generateParam(
                YoutubeVideoLinkUtils.YOUTUBE_API_KEY,
                videoId
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        youtubeVideoUseCase.unsubscribe();
    }

    private class DefaultSubscriber extends Subscriber<YoutubeVideoModel> {
        private final String videoId;

        public DefaultSubscriber(String videoId) {
            this.videoId = videoId;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, "error here : " + e);
        }

        @Override
        public void onNext(YoutubeVideoModel youtubeVideoModel) {
            Log.i(TAG, "result here : " + youtubeVideoModel);
            if (isViewAttached()) {
                getView().addAddUrlVideModel(convert(youtubeVideoModel));
            }
        }

        private AddUrlVideoModel convert(YoutubeVideoModel youtubeVideoModel) {
            AddUrlVideoModel addUrlVideoModel = new AddUrlVideoModel();
            addUrlVideoModel.setHeight(youtubeVideoModel.getHeight());
            addUrlVideoModel.setWidth(youtubeVideoModel.getWidth());
            addUrlVideoModel.setSnippetTitle(youtubeVideoModel.getSnippetTitle());
            addUrlVideoModel.setSnippetDescription(youtubeVideoModel.getSnippetDescription());
            addUrlVideoModel.setThumbnailUrl(youtubeVideoModel.getThumbnailUrl());
            addUrlVideoModel.setVideoId(videoId);
            return addUrlVideoModel;
        }
    }
}
