package com.tokopedia.gm.statistic.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.statistic.domain.GMStatRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatClearCacheUseCase extends UseCase<Boolean> {
    private GMStatRepository gmStatRepository;

    @Inject
    public GMStatClearCacheUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatRepository gmStatRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.gmStatRepository = gmStatRepository;
    }

    public static RequestParams createRequestParam() {
        return RequestParams.create();
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gmStatRepository.clearCache();
    }
}
