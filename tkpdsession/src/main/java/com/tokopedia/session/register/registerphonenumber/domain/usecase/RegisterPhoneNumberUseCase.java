package com.tokopedia.session.register.registerphonenumber.domain.usecase;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;
import com.tokopedia.session.register.registerphonenumber.data.source.CloudRegisterPhoneNumberSource;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class RegisterPhoneNumberUseCase extends UseCase<RegisterPhoneNumberModel> {

    private static final String PARAMS_PHONE_NUMBER = "phone";
    private static final String PARAMS_TYPE = "type";
    private static final String PARAMS_TYPE_PHONE = "phone";
    private static final String PARAMS_NAME = "fullname";
    private static final String PARAMS_OS_TYPE = "os_type";
    private static final int PARAMS_OS_TYPE_ANDROID = 1;

    private Context context;
    private final CloudRegisterPhoneNumberSource registerPhoneNumberSource;

    public RegisterPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      Context context,
                                      CloudRegisterPhoneNumberSource registerPhoneNumberSource) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.registerPhoneNumberSource = registerPhoneNumberSource;
    }

    @Override
    public Observable<RegisterPhoneNumberModel> createObservable(RequestParams requestParams) {
        return registerPhoneNumberSource.registerPhoneNumber(context, requestParams.getParameters());
    }

    public static RequestParams getParams(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAMS_PHONE_NUMBER, phoneNumber);
        params.putString(PARAMS_TYPE, PARAMS_TYPE_PHONE);
        params.putInt(PARAMS_OS_TYPE, PARAMS_OS_TYPE_ANDROID);
        return params;
    }

    public static RequestParams getParamsWithName(String phoneNumber, String name) {
        RequestParams params = RequestParams.create();
        params.putString(PARAMS_PHONE_NUMBER, phoneNumber);
        params.putString(PARAMS_NAME, name);
        params.putString(PARAMS_TYPE, PARAMS_TYPE_PHONE);
        params.putInt(PARAMS_OS_TYPE, PARAMS_OS_TYPE_ANDROID);
        return params;
    }
}
