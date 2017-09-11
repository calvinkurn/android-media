package com.tokopedia.seller.base.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.DatePickerRepository;
import com.tokopedia.seller.base.domain.model.DatePickerDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchDatePickerUseCase extends UseCase<DatePickerDomainModel> {
    private DatePickerRepository datePickerRepository;

    @Inject
    public FetchDatePickerUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                  DatePickerRepository datePickerRepository) {
        super(threadExecutor, postExecutionThread);
        this.datePickerRepository = datePickerRepository;
    }

    @Override
    public Observable<DatePickerDomainModel> createObservable(RequestParams requestParams) {
        return datePickerRepository.fetchSetting();
    }

    public static RequestParams createRequestParams() {
        return RequestParams.EMPTY;
    }

}
