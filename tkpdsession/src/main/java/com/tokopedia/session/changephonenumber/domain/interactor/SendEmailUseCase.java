package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 28/12/17.
 */

public class SendEmailUseCase extends UseCase<Boolean> {
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String OS_TYPE_MOBILE = "mobile";

    private final ChangePhoneNumberRepository changePhoneNumberRepository;

    @Inject
    public SendEmailUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ChangePhoneNumberRepository changePhoneNumberRepository) {
        super(threadExecutor, postExecutionThread);
        this.changePhoneNumberRepository = changePhoneNumberRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return changePhoneNumberRepository.sendEmailOTP(requestParams.getParameters());
    }

    public static RequestParams getSendEmailParam() {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_OS_TYPE, OS_TYPE_MOBILE);
        return param;
    }
}
