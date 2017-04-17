package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.YoutubeVideoRepository;
import com.tokopedia.seller.product.domain.model.YoutubeVideoModel;

import rx.Observable;

/**
 * @author normansyahputa on 4/12/17.
 */
public class YoutubeVideoUseCase extends UseCase<YoutubeVideoModel> {
    public static final String KEY_YOUTUBE_ID = "KEY_YOUTUBE_ID";
    public static final String KEY_VIDEO_ID = "KEY_VIDEO_ID";
    private YoutubeVideoRepository youtubeVideoRepository;

    public YoutubeVideoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            YoutubeVideoRepository youtubeVideoRepository) {
        super(threadExecutor, postExecutionThread);
        this.youtubeVideoRepository = youtubeVideoRepository;
    }

    @Override
    public Observable<YoutubeVideoModel> createObservable(RequestParams requestParams) {
        return youtubeVideoRepository.fetchYoutubeVideoInfo(
                requestParams.getString(KEY_VIDEO_ID, ""),
                requestParams.getString(KEY_YOUTUBE_ID, "")
        );
    }

    public RequestParams generateParam(String keyId, String videoId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEY_YOUTUBE_ID, keyId);
        requestParams.putString(KEY_VIDEO_ID, videoId);
        return requestParams;
    }
}
