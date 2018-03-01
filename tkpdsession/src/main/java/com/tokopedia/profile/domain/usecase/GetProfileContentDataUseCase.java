package com.tokopedia.profile.domain.usecase;

import com.tokopedia.profile.data.source.GetProfileContentDataSourceCloud;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nakama on 28/02/18.
 */

public class GetProfileContentDataUseCase extends UseCase<TopProfileViewModel> {

    public static final String PARAM_USER_ID = "userID";

    private final GetProfileContentDataSourceCloud getProfileContentDataSourceCloud;

    @Inject
    public GetProfileContentDataUseCase(GetProfileContentDataSourceCloud getProfileContentDataSourceCloud){
        this.getProfileContentDataSourceCloud = getProfileContentDataSourceCloud;
    }

    @Override
    public Observable<TopProfileViewModel> createObservable(RequestParams requestParams) {
        return getProfileContentDataSourceCloud.getProfileContentData(requestParams);
    }

    public static RequestParams getParams(String userId){
        RequestParams param = RequestParams.create();
        param.putInt(PARAM_USER_ID, Integer.valueOf(userId));
        return param;
    }
}
