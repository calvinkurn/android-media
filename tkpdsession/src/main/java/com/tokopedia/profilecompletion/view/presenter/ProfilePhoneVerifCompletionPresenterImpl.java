package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.core.home.GetUserInfoListener;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.GetUserInfoSubscriber;
import com.tokopedia.profilecompletion.view.fragment.ProfilePhoneVerifCompletionFragment;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfilePhoneVerifCompletionPresenterImpl implements ProfilePhoneVerifCompletionPresenter {

    private ProfilePhoneVerifCompletionFragment view;
    private final GetUserInfoUseCase getUserInfoUseCase;

    public ProfilePhoneVerifCompletionPresenterImpl(ProfilePhoneVerifCompletionFragment view, GetUserInfoUseCase getUserInfoUseCase) {
        this.view = view;
        this.getUserInfoUseCase = getUserInfoUseCase;
    }


    @Override
    public void getUserInfo() {
        getUserInfoUseCase.execute(
                GetUserInfoUseCase.generateParam(),
                new GetUserInfoSubscriber(view));
    }
}
