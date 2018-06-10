package com.tokopedia.session.addchangeemail.domain.usecase;

import com.tokopedia.session.addchangeemail.data.source.AddEmailSource;
import com.tokopedia.session.addchangeemail.view.viewmodel.AddEmailViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailUseCase extends UseCase<AddEmailViewModel> {
    private final static String PARAM_USER_ID = "user_id";
    public final static String PARAM_EMAIL = "email";
    private final static String PARAM_UNIQUE_CODE = "uc";

    private final AddEmailSource addEmailSource;

    public AddEmailUseCase(AddEmailSource addEmailSource) {
        this.addEmailSource = addEmailSource;
    }

    @Override
    public Observable<AddEmailViewModel> createObservable(RequestParams requestParams) {
        return addEmailSource.addEmail(requestParams);
    }

    public static RequestParams getParams(String userId, String email, String uniqueCode) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_EMAIL, email);
        params.putString(PARAM_UNIQUE_CODE, uniqueCode);
        return params;
    }
}
