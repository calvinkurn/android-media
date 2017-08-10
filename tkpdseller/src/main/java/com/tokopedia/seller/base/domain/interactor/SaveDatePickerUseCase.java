package com.tokopedia.seller.base.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
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

public class SaveDatePickerUseCase extends UseCase<Boolean> {

    private static final String DATE_PICKER_MODEL = "DATE_PICKER_MODEL";

    private DatePickerRepository datePickerRepository;

    @Inject
    public SaveDatePickerUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                 DatePickerRepository datePickerRepository) {
        super(threadExecutor, postExecutionThread);
        this.datePickerRepository = datePickerRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return datePickerRepository.saveSetting((DatePickerDomainModel) requestParams.getObject(DATE_PICKER_MODEL));
    }

    public static RequestParams createRequestParams(DatePickerViewModel datePickerViewModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DATE_PICKER_MODEL, new DatePickerDomainModel(datePickerViewModel));
        return requestParams;
    }

}
