package com.tokopedia.tokocash.autosweepmf.domain.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.tokocash.autosweepmf.data.repository.AutoSweepRepositoryImpl;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class PostAutoSweepLimitUseCase extends UseCase<AutoSweepLimit> {
    private JsonObject body;
    private AutoSweepRepositoryImpl mRepository;

    @Inject
    public PostAutoSweepLimitUseCase(AutoSweepRepositoryImpl repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<AutoSweepLimit> createObservable(RequestParams requestParams) {
        return mRepository.postAutoSweepLimit(body);
    }

    public void setBody(JsonObject body) {
        this.body = body;
    }
}
