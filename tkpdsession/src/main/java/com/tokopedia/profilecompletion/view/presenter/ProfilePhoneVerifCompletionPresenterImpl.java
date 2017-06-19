package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.GetUserInfoSubscriber;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfilePhoneVerifCompletionPresenterImpl implements ProfilePhoneVerifCompletionPresenter {

    private final GetUserInfoUseCase getUserInfoUseCase;

    public ProfilePhoneVerifCompletionPresenterImpl(GetUserInfoUseCase getUserInfoUseCase) {
        this.getUserInfoUseCase = getUserInfoUseCase;
    }


    @Override
    public void getUserInfo() {
        getUserInfoUseCase.execute(
                GetUserInfoUseCase.generateParam(),
                new GetUserInfoSubscriber());
    }
}
