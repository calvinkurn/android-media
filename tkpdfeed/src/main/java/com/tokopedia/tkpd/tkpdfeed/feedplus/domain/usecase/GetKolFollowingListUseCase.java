package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetKolFollowingListUseCase extends UseCase<List<KolFollowingDomain>>{

    public static final String PARAM_ID = "id";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";


    public static final int DEFAULT_LIMIT = 10;
    private FeedRepository feedRepository;

    public GetKolFollowingListUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<List<KolFollowingDomain>> createObservable(RequestParams requestParams) {
        return feedRepository.getKolFollowingList(requestParams);
    }

    public static RequestParams getParam(int id, String cursor) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putString(PARAM_CURSOR, cursor);
        params.putInt(PARAM_LIMIT, DEFAULT_LIMIT);
        return params;
    }
}
