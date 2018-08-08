package com.tokopedia.otp.phoneverification.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.otp.phoneverification.data.source.ChangeMsisdnSource;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberUseCase extends UseCase<ChangePhoneNumberViewModel> {

    private static final String PARAM_MSISDN = "msisdn";
    private final ChangeMsisdnSource changeMsisdnSource;
    private final SessionHandler sessionHandler;

    @Inject
    public ChangePhoneNumberUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ChangeMsisdnSource changeMsisdnSource,
                                    SessionHandler sessionHandler) {
        super(threadExecutor, postExecutionThread);
        this.changeMsisdnSource = changeMsisdnSource;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<ChangePhoneNumberViewModel> createObservable(final RequestParams requestParams) {
        return changeMsisdnSource.changePhoneNumber(requestParams.getParameters())
                .flatMap(addPhoneNumberToViewModel(requestParams))
                .doOnNext(savePhoneNumber());
    }

    private Func1<ChangePhoneNumberViewModel, Observable<ChangePhoneNumberViewModel>> addPhoneNumberToViewModel(final RequestParams requestParams) {
        return new Func1<ChangePhoneNumberViewModel, Observable<ChangePhoneNumberViewModel>>() {
            @Override
            public Observable<ChangePhoneNumberViewModel> call(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
                changePhoneNumberViewModel.setPhoneNumber(
                        requestParams.getString(PARAM_MSISDN, ""));
                return Observable.just(changePhoneNumberViewModel);
            }
        };
    }

    private Action1<ChangePhoneNumberViewModel> savePhoneNumber() {
        return new Action1<ChangePhoneNumberViewModel>() {
            @Override
            public void call(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
                if (changePhoneNumberViewModel.isSuccess()) {
                    sessionHandler.setPhoneNumber(changePhoneNumberViewModel.getPhoneNumber());
                }
            }
        };
    }

    public static RequestParams getParam(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_MSISDN, phoneNumber);
        return params;
    }
}
