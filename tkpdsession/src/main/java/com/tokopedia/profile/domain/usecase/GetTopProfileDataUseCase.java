package com.tokopedia.profile.domain.usecase;

import com.tokopedia.profile.data.source.GetTopProfileDataSourceCloud;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 28/02/18.
 */

public class GetTopProfileDataUseCase extends UseCase<TopProfileViewModel> {

    public static final String PARAM_USER_ID = "userID";

    private final GetTopProfileDataSourceCloud getTopProfileDataSourceCloud;

    @Inject
    public GetTopProfileDataUseCase(GetTopProfileDataSourceCloud getTopProfileDataSourceCloud){
        this.getTopProfileDataSourceCloud = getTopProfileDataSourceCloud;
    }

    @Override
    public Observable<TopProfileViewModel> createObservable(RequestParams requestParams) {
        return getTopProfileDataSourceCloud.getProfileContentData(requestParams);
    }

    public static RequestParams getParams(String userId){
        RequestParams param = RequestParams.create();
        param.putInt(PARAM_USER_ID, Integer.valueOf(userId));
        return param;
    }
}
