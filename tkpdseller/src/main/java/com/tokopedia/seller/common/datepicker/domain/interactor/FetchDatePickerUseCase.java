package com.tokopedia.seller.common.datepicker.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.common.datepicker.domain.DatePickerRepository;
import com.tokopedia.seller.common.datepicker.domain.model.DatePickerDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchDatePickerUseCase extends CompositeUseCase<DatePickerDomainModel> {
    private DatePickerRepository datePickerRepository;

    public FetchDatePickerUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                  DatePickerRepository datePickerRepository) {
        super(threadExecutor, postExecutionThread);
        this.datePickerRepository = datePickerRepository;
    }

    @Override
    public Observable<DatePickerDomainModel> createObservable(RequestParams requestParams) {
        return datePickerRepository.fetchSetting();
    }

    public static RequestParams createRequestParams(){
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }

}
