package com.tokopedia.otp.phoneverification.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.otp.phoneverification.data.source.ChangeMsisdnSource;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberUseCase extends UseCase<ChangePhoneNumberViewModel> {

    private static final String PARAM_MSISDN = "msisdn";
    private final ChangeMsisdnSource changeMsisdnSource;

    @Inject
    public ChangePhoneNumberUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ChangeMsisdnSource changeMsisdnSource) {
        super(threadExecutor, postExecutionThread);
        this.changeMsisdnSource = changeMsisdnSource;
    }

    @Override
    public Observable<ChangePhoneNumberViewModel> createObservable(final RequestParams requestParams) {
        return changeMsisdnSource.changePhoneNumber(requestParams.getParameters())
                .flatMap(new Func1<ChangePhoneNumberViewModel, Observable<ChangePhoneNumberViewModel>>() {
                    @Override
                    public Observable<ChangePhoneNumberViewModel> call(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
                        changePhoneNumberViewModel.setPhoneNumber(
                                requestParams.getString(PARAM_MSISDN, ""));
                        return Observable.just(changePhoneNumberViewModel);
                    }
                });
    }

    public static RequestParams getParam(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_MSISDN, phoneNumber);
        return params;
    }
}
