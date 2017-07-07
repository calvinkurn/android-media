package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.fragment.ProfilePhoneVerifCompletionFragment;
import com.tokopedia.profilecompletion.view.subscriber.GetUserInfoSubscriber;
import com.tokopedia.profilecompletion.view.subscriber.EditUserInfoSubscriber;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfilePhoneVerifCompletionPresenterImpl implements ProfilePhoneVerifCompletionPresenter {

    private ProfilePhoneVerifCompletionFragment view;
    private final GetUserInfoUseCase getUserInfoUseCase;
    private final EditUserProfileUseCase editUserProfileUseCase;

    public ProfilePhoneVerifCompletionPresenterImpl(ProfilePhoneVerifCompletionFragment view,
                                                    GetUserInfoUseCase getUserInfoUseCase,
                                                    EditUserProfileUseCase editUserProfileUseCase) {
        this.view = view;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.editUserProfileUseCase = editUserProfileUseCase;
    }

//
//    @Override
//    public void getUserInfo() {
//        getUserInfoUseCase.execute(
//                GetUserInfoUseCase.generateParam(),
//                new GetUserInfoSubscriber(view));
//    }
//
//    @Override
//    public void editDOB(String day, String month, String year) {
//        editUserProfileUseCase.execute(
//                EditUserProfileUseCase.generateParamDOB(day, month, year),
//                new EditUserInfoSubscriber(view, EditUserProfileUseCase.EDIT_GENDER));
//    }
//
//    @Override
//    public void editGender(int gender) {
//        editUserProfileUseCase.execute(
//                EditUserProfileUseCase.generateParamGender(gender),
//                new EditUserInfoSubscriber(view, EditUserProfileUseCase.EDIT_GENDER));
//    }
}
