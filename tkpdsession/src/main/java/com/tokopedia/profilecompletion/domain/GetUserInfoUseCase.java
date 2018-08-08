package com.tokopedia.profilecompletion.domain;

import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoUseCase extends UseCase<GetUserInfoDomainModel> {

    private final ProfileRepository profileRepository;

    public GetUserInfoUseCase(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Observable<GetUserInfoDomainModel> createObservable(RequestParams requestParams) {
        return profileRepository.getUserInfo(requestParams.getParameters());
    }

    public static RequestParams generateParam() {
        return RequestParams.EMPTY;
    }
}
