package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;
import com.tokopedia.profilecompletion.view.listener.EditProfileListener;
import com.tokopedia.profilecompletion.view.subscriber.EditUserInfoSubscriber;
import com.tokopedia.profilecompletion.view.subscriber.GetUserInfoSubscriber;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class ProfileCompletionPresenterImpl implements ProfileCompletionPresenter{

    private final ProfileCompletionFragment view;
    private final EditUserProfileUseCase editUserProfileUseCase;
    private GetUserInfoUseCase getUserInfoUseCase;

    public ProfileCompletionPresenterImpl(ProfileCompletionFragment view, GetUserInfoUseCase getUserInfoUseCase, EditUserProfileUseCase editUserProfileUseCase) {
        this.view = view;
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.editUserProfileUseCase = editUserProfileUseCase;
    }

    @Override
    public void getUserInfo() {
        getUserInfoUseCase.execute(GetUserInfoUseCase.generateParam(), new GetUserInfoSubscriber(view));
    }

    @Override
    public void editUserInfo(String date, int month, String year) {
        editUserProfileUseCase.execute(EditUserProfileUseCase.generateParamDOB(date, String.valueOf(month), year), new EditUserInfoSubscriber(view, EditUserProfileUseCase.EDIT_DOB));
    }

    @Override
    public void editUserInfo(int gender) {
        editUserProfileUseCase.execute(EditUserProfileUseCase.generateParamGender(gender), new EditUserInfoSubscriber(view, EditUserProfileUseCase.EDIT_GENDER));
    }

    @Override
    public void editUserInfo(String verif) {

    }

    @Override
    public void skipView(String tag) {
        view.skipView(tag);
    }
}
