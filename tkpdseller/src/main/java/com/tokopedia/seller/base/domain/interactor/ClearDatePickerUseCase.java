package com.tokopedia.seller.base.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.base.domain.DatePickerRepository;
import com.tokopedia.seller.base.domain.model.DatePickerDomainModel;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class ClearDatePickerUseCase extends CompositeUseCase<Boolean> {

    private DatePickerRepository datePickerRepository;

    @Inject
    public ClearDatePickerUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                  DatePickerRepository datePickerRepository) {
        super(threadExecutor, postExecutionThread);
        this.datePickerRepository = datePickerRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return datePickerRepository.clearSetting();
    }

    public static RequestParams createRequestParams(){
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }

}
