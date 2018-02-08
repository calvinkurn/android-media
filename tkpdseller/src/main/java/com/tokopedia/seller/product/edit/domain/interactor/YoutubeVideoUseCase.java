package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.edit.domain.YoutubeVideoRepository;
import com.tokopedia.seller.product.edit.domain.model.YoutubeVideoModel;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author normansyahputa on 4/12/17.
 */
public class YoutubeVideoUseCase extends UseCase<YoutubeVideoModel> {
    public static final String KEY_YOUTUBE_ID = "KEY_YOUTUBE_ID";
    public static final String KEY_VIDEO_ID = "KEY_VIDEO_ID";
    private YoutubeVideoRepository youtubeVideoRepository;
    private Subscription listSubscription;

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

    public Observable<List<YoutubeVideoModel>> createObservable(List<RequestParams> requestParams) {
        return Observable.from(requestParams)
                .flatMap(
                        new Func1<RequestParams, Observable<YoutubeVideoModel>>() {
                            @Override
                            public Observable<YoutubeVideoModel> call(RequestParams requestParams) {
                                return createObservable(requestParams);
                            }
                        }
                ).toList();
    }

    public Observable<List<YoutubeVideoModel>> createObservable(List<RequestParams> requestParams,
                                                                final YoutubeVideoUseCaseListener youtubeVideoUseCaseListener) {
        return Observable.from(requestParams)
                .doOnNext(new Action1<RequestParams>() {
                    @Override
                    public void call(RequestParams requestParams) {
                        if (youtubeVideoUseCaseListener != null) {
                            youtubeVideoUseCaseListener.onShowLoading();
                        }
                    }
                })
                .flatMap(
                        new Func1<RequestParams, Observable<YoutubeVideoModel>>() {
                            @Override
                            public Observable<YoutubeVideoModel> call(RequestParams requestParams) {
                                return createObservable(requestParams);
                            }
                        }
                ).toList();
    }

    public void executeList(List<RequestParams> requestParams, Subscriber<List<YoutubeVideoModel>> subscriber) {
        this.listSubscription = createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public void executeList(List<RequestParams> requestParams, Subscriber<List<YoutubeVideoModel>> subscriber, YoutubeVideoUseCaseListener youtubeVideoUseCaseListener) {
        this.listSubscription = createObservable(requestParams, youtubeVideoUseCaseListener)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        if (listSubscription == null)
            return;

        if (!listSubscription.isUnsubscribed()) {
            listSubscription.unsubscribe();
        }
    }

    public RequestParams generateParam(String keyId, String videoId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEY_YOUTUBE_ID, keyId);
        requestParams.putString(KEY_VIDEO_ID, videoId);
        return requestParams;
    }

    public interface YoutubeVideoUseCaseListener {
        void onShowLoading();

        void onHideLoading();
    }
}
