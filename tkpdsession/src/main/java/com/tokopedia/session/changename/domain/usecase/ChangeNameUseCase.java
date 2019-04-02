package com.tokopedia.session.changename.domain.usecase;

import com.tokopedia.session.changename.data.source.ChangeNameSource;
import com.tokopedia.session.changename.view.viewmodel.ChangeNameViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class ChangeNameUseCase extends UseCase<ChangeNameViewModel> {
    private final static String PARAM_USER_ID = "user_id";
    public final static String PARAM_NAME = "fullname";

    private final ChangeNameSource changeNameSource;

    public ChangeNameUseCase(ChangeNameSource changeNameSource) {
        this.changeNameSource = changeNameSource;
    }

    @Override
    public Observable<ChangeNameViewModel> createObservable(RequestParams requestParams) {
        return changeNameSource.changeName(requestParams);
    }

    public static RequestParams getParams(String userId, String name) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_NAME, name);
        return params;
    }
}
