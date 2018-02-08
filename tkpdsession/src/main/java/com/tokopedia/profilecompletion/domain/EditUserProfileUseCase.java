package com.tokopedia.profilecompletion.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel;

import rx.Observable;

/**
 * @author by nisie on 7/3/17.
 */

public class EditUserProfileUseCase extends UseCase<EditUserInfoDomainModel> {

    public static final int EDIT_GENDER = 1;
    public static final int EDIT_DOB = 2;
    public static final int EDIT_VERIF = 3;

    private static final String BDAY_DATE = "bday_dd";
    private static final String BDAY_MONTH = "bday_mm";
    private static final String BDAY_YEAR = "bday_yy";
    private static final String GENDER = "gender";

    public static final int MALE = 1;
    public static final int FEMALE = 2;

    private final ProfileRepository profileRepository;

    public EditUserProfileUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                  ProfileRepository profileRepository) {
        super(threadExecutor, postExecutionThread);
        this.profileRepository = profileRepository;
    }

    @Override
    public Observable<EditUserInfoDomainModel> createObservable(RequestParams requestParams) {
        return profileRepository.editUserInfo(requestParams.getParameters());
    }

    public static RequestParams generateParamDOB(String day, String month, String year) {
        RequestParams params = RequestParams.create();
        params.putString(BDAY_DATE, day);
        params.putString(BDAY_MONTH, month);
        params.putString(BDAY_YEAR, year);
        return params;
    }

    public static RequestParams generateParamGender(int gender) {
        RequestParams params = RequestParams.create();
        params.putInt(GENDER, gender);
        return params;
    }
}
