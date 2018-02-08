package com.tokopedia.seller.product.edit.view.presenter;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.edit.domain.interactor.YoutubeVideoUseCase;
import com.tokopedia.seller.product.edit.domain.model.YoutubeVideoModel;
import com.tokopedia.seller.product.edit.utils.YoutubeVideoLinkUtils;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoActView;
import com.tokopedia.seller.product.edit.view.model.AddUrlVideoModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author normansyahputa on 4/17/17
 */
public class YoutubeAddVideoPresenterImpl extends YoutubeAddVideoPresenter {
    private static final String TAG = "YoutubeAddVideoPresente";
    protected YoutubeAddVideoActView youtubeActView;
    private YoutubeVideoUseCase youtubeVideoUseCase;
    private YoutubeVideoLinkUtils youtubeVideoLinkUtils;

    public YoutubeAddVideoPresenterImpl(
            YoutubeVideoUseCase youtubeVideoUseCase,
            YoutubeVideoLinkUtils youtubeVideoLinkUtils
    ) {
        this.youtubeVideoUseCase = youtubeVideoUseCase;
        this.youtubeVideoLinkUtils = youtubeVideoLinkUtils;
    }

    public void fetchYoutube(String videoId) {
        RequestParams requestParams = generateParam(videoId);
        youtubeVideoUseCase.execute(requestParams, new DefaultSubscriber(videoId));
    }

    @Override
    public void fetchYoutube(List<String> videoIds) {

        if (videoIds == null || videoIds.size() <= 0)
            return;
      
        if (isViewAttached()) {
            getView().showLoading();
        }

        List<RequestParams> requestParamses = new ArrayList<>();
        for (String videoId : videoIds) {
            requestParamses.add(generateParam(videoId));
        }
        youtubeVideoUseCase.executeList(requestParamses, new DefaultListSubscriber(videoIds));
    }

    public void setYoutubeActView(YoutubeAddVideoActView youtubeActView) {
        this.youtubeActView = youtubeActView;
    }

    public void fetchYoutubeDescription(String youtubeUrl) {
        youtubeVideoLinkUtils.setYoutubeUrl(youtubeUrl);
        String videoId = youtubeVideoLinkUtils.saveVideoID();
        if (youtubeActView != null) {
            youtubeActView.addVideoIds(videoId);
        }
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

    protected void showYoutubeException(YoutubeVideoLinkUtils.YoutubeException e) {
        getView().showMessageErrorRaw(e.getMessageWithoutVideoId());
        if (youtubeActView != null) {
            youtubeActView.removeVideoId(e.getVideoId());
        }
    }

    private class DefaultListSubscriber extends Subscriber<List<YoutubeVideoModel>> {

        private List<String> videoIds;

        public DefaultListSubscriber(List<String> videoIds) {
            this.videoIds = videoIds;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, "error here : " + e);
            if (isViewAttached()) {
                getView().hideLoading();
                getView().hideRetryFull();
                if (e instanceof YoutubeVideoLinkUtils.YoutubeException) {
                    showYoutubeException((YoutubeVideoLinkUtils.YoutubeException) e);
                    return;
                }

                if (videoIds.size() > 0) {
                    getView().showMessageError(videoIds.get(0));
                }


            }
        }

        @Override
        public void onNext(List<YoutubeVideoModel> youtubeVideoModels) {
            Log.i(TAG, "result here : " + youtubeVideoModels);
            if (isViewAttached()) {
                getView().hideLoading();
                getView().hideRetryFull();
                getView().addAddUrlVideModels(convert(youtubeVideoModels));
            }
        }

        private List<AddUrlVideoModel> convert(List<YoutubeVideoModel> youtubeVideoModels) {
            List<AddUrlVideoModel> addUrlVideoModels = new ArrayList<>();
            int i = 0;
            for (YoutubeVideoModel youtubeVideoModel : youtubeVideoModels) {

                if (youtubeVideoModel.equals(YoutubeVideoModel.invalidYoutubeModel())) {
                    if (youtubeActView != null) {
                        youtubeActView.removeVideoIds(i);
                    }
                    continue;
                }

                String videoId = videoIds.get(i);

                AddUrlVideoModel addUrlVideoModel = new AddUrlVideoModel();
                addUrlVideoModel.setHeight(youtubeVideoModel.getHeight());
                addUrlVideoModel.setWidth(youtubeVideoModel.getWidth());
                addUrlVideoModel.setSnippetTitle(youtubeVideoModel.getSnippetTitle());
                addUrlVideoModel.setSnippetDescription(youtubeVideoModel.getSnippetDescription());
                addUrlVideoModel.setThumbnailUrl(youtubeVideoModel.getThumbnailUrl());
                addUrlVideoModel.setVideoId(videoId);

                addUrlVideoModels.add(addUrlVideoModel);

                i++;
            }


            return addUrlVideoModels;
        }
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
            if (isViewAttached()) {
                if (e instanceof YoutubeVideoLinkUtils.YoutubeException) {
                    showYoutubeException((YoutubeVideoLinkUtils.YoutubeException) e);
                    return;
                }

                getView().showMessageError(videoId);
                if (youtubeActView != null) {
                    youtubeActView.removeVideoId(videoId);
                }
            }
        }

        @Override
        public void onNext(YoutubeVideoModel youtubeVideoModel) {
            Log.i(TAG, "result here : " + youtubeVideoModel);
            if (isViewAttached()) {
                AddUrlVideoModel convert = convert(youtubeVideoModel);
                if (convert != null)
                    getView().addAddUrlVideModel(convert);
            }
        }

        private AddUrlVideoModel convert(YoutubeVideoModel youtubeVideoModel) {

            if (youtubeVideoModel.equals(YoutubeVideoModel.invalidYoutubeModel())) {
                if (youtubeActView != null) {
                    youtubeActView.removeVideoId(videoId);
                }
                return null;
            }

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
