package com.tokopedia.session.register.domain.interactor.registerphonenumber;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.register.data.source.CheckMsisdnSource;
import com.tokopedia.session.register.domain.model.CheckMsisdnDomain;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnPhoneNumberUseCase extends UseCase<CheckMsisdnDomain> {


    private static final String PARAMS_PHONE_NUMBER = "phone";

    private Context context;
    private CheckMsisdnSource checkMsisdnSource;

    public CheckMsisdnPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         Context context,
                                         CheckMsisdnSource checkMsisdnSource) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.checkMsisdnSource = checkMsisdnSource;
    }

    @Override
    public Observable<CheckMsisdnDomain> createObservable(RequestParams requestParams) {
        return checkMsisdnSource.changePhoneNumber(context, requestParams.getParameters());
    }

    public static RequestParams getParams(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAMS_PHONE_NUMBER, phoneNumber);
        return params;
    }
}
