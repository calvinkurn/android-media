package com.tokopedia.profilecompletion.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;

import rx.Observable;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoUseCase extends UseCase<GetUserInfoDomainModel> {

    private final ProfileRepository profileRepository;

    public GetUserInfoUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                              ProfileRepository profileRepository) {
        super(threadExecutor, postExecutionThread);
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
