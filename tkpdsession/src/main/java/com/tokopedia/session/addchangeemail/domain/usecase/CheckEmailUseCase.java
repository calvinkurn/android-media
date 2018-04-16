package com.tokopedia.session.addchangeemail.domain.usecase;

import com.tokopedia.session.addchangeemail.data.source.AddEmailSource;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailUseCase extends UseCase<CheckEmailViewModel> {
    private final static String PARAM_EMAIL = "email";

    private final AddEmailSource addEmailSource;

    public CheckEmailUseCase(AddEmailSource addEmailSource) {
        this.addEmailSource = addEmailSource;
    }

    @Override
    public Observable<CheckEmailViewModel> createObservable(RequestParams requestParams) {
        return addEmailSource.checkEmail(requestParams);
    }

    public static RequestParams getParams(String email) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_EMAIL, email);
        return params;
    }
}
