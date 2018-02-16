package com.tokopedia.session.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.data.source.CloudDiscoverDataSource;
import com.tokopedia.session.data.source.LocalDiscoverDataSource;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverUseCase extends UseCase<DiscoverViewModel> {

    public static final String PARAM_TYPE = "type";
    private static final String TYPE_REGISTER = "register";
    private final CloudDiscoverDataSource cloudDiscoverDataSource;
    private final LocalDiscoverDataSource localDiscoverDataSource;

    @Inject
    public DiscoverUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           CloudDiscoverDataSource cloudDiscoverDataSource,
                           LocalDiscoverDataSource localDiscoverDataSource) {
        super(threadExecutor, postExecutionThread);
        this.cloudDiscoverDataSource = cloudDiscoverDataSource;
        this.localDiscoverDataSource = localDiscoverDataSource;

    }

    public static RequestParams getParamRegister() {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_TYPE,TYPE_REGISTER);
        return params;
    }

    @Override
    public Observable<DiscoverViewModel> createObservable(final RequestParams requestParams) {
        return localDiscoverDataSource.getDiscover(requestParams.getString(PARAM_TYPE,""))
                .onErrorResumeNext(new Func1<Throwable, Observable<DiscoverViewModel>>() {
                    @Override
                    public Observable<DiscoverViewModel> call(Throwable throwable) {
                        return cloudDiscoverDataSource.getDiscover(requestParams);
                    }
                });
    }
}
