package com.tokopedia.session.addchangeemail.domain.usecase;

import com.tokopedia.session.addchangeemail.data.source.AddEmailSource;
import com.tokopedia.session.addchangeemail.view.viewmodel.RequestVerificationViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class RequestVerificationUseCase extends UseCase<RequestVerificationViewModel> {
    private final static String PARAM_EMAIL = "email";
    private final static String PARAM_OS_TYPE = "os_type";
    private final static String PARAM_TYPE = "type";

    private final static int VALUE_OS = 1;
    private final static String VALUE_TYPE = "api";

    private final AddEmailSource addEmailSource;

    public RequestVerificationUseCase(AddEmailSource addEmailSource) {
        this.addEmailSource = addEmailSource;
    }

    @Override
    public Observable<RequestVerificationViewModel> createObservable(RequestParams requestParams) {
        return addEmailSource.requestVerification(requestParams);
    }

    public static RequestParams getParams(String email) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_EMAIL, email);
        params.putInt(PARAM_OS_TYPE, VALUE_OS);
        params.putString(PARAM_TYPE, VALUE_TYPE);
        return params;
    }
}
