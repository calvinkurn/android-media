package com.tokopedia.session.register.domain.interactor.registerthird;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.data.repository.SessionRepository;
import com.tokopedia.session.register.domain.model.CreatePasswordDomain;

import rx.Observable;

/**
 * @author by nisie on 10/16/17.
 */

public class CreatePasswordUseCase extends UseCase<CreatePasswordDomain> {

    public static final String BIRTHDAY = "bday_dd";
    public static final String BIRTHMONTH = "bday_mm";
    public static final String BIRTHYEAR = "bday_yy";
    public static final String FULLNAME = "full_name";
    public static final String MSISDN = "msisdn";
    public static final String REGISTER_TOS = "register_tos";
    public static final String CONFIRM_PASSWORD = "confirm_pass";
    public static final String NEW_PASSWORD = "new_pass";
    public static final String USER_ID = "user_id";

    SessionRepository sessionRepository;

    public CreatePasswordUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 SessionRepository sessionRepository) {
        super(threadExecutor, postExecutionThread);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Observable<CreatePasswordDomain> createObservable(RequestParams requestParams) {
        return sessionRepository.createPassword(requestParams);
    }

    public static RequestParams getParam(String fullName, int bdayDay, int bdayMonth,
                                         int bdayYear, String newPass, String confirmPass,
                                         String msisdn, String registerTos, String tempUserId) {

        RequestParams params = RequestParams.create();

        params.getParameters().putAll(AuthUtil.generateParamsNetwork2(MainApplication
                        .getAppContext(),
                params.getParameters()));
        params.putString(FULLNAME, fullName);
        params.putInt(BIRTHDAY, bdayDay);
        params.putInt(BIRTHMONTH, bdayMonth);
        params.putInt(BIRTHYEAR, bdayYear);
        params.putString(NEW_PASSWORD, newPass);
        params.putString(CONFIRM_PASSWORD, confirmPass);
        params.putString(MSISDN, msisdn);
        params.putString(REGISTER_TOS, registerTos);
        params.putString(USER_ID, tempUserId);
        return params;
    }
}
