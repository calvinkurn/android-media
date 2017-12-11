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

    public static final String PARAM_ID = "id";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";

    public static final int DEFAULT_LIMIT = 10;
    private static final String FIRST_CURSOR = "";
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

    public static RequestParams getFirstTimeParam(int id) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putString(PARAM_CURSOR, FIRST_CURSOR);
        params.putInt(PARAM_LIMIT, DEFAULT_LIMIT);
        return params;
    }

    public static RequestParams getParam(int id, String cursor) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putString(PARAM_CURSOR, cursor);
        params.putInt(PARAM_LIMIT, DEFAULT_LIMIT);
        return params;
    }
}
