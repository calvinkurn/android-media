package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import rx.Observable;

/**
 * @author by nisie on 10/31/17.
 */

public class GetKolCommentsUseCase extends UseCase<KolComments> {

    private FeedRepository feedRepository;

    public GetKolCommentsUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<KolComments> createObservable(RequestParams requestParams) {
        return feedRepository.getKolComments(requestParams);
    }

    public static RequestParams getParam() {
        RequestParams params = RequestParams.create();

        return params;
    }

    public static RequestParams getFirstTimeParam() {
        RequestParams params = RequestParams.create();

        return params;
    }
}
