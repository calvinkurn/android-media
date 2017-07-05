package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.domain.model.GetUserInfoDomainModel;
import com.tokopedia.profilecompletion.view.GetUserInfoSubscriber;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;

import rx.Subscriber;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class ProfileCompletionPresenterImpl implements ProfileCompletionPresenter{

    private final ProfileCompletionFragment view;
    private GetUserInfoUseCase getUserInfoUseCase;

    public ProfileCompletionPresenterImpl(ProfileCompletionFragment view, GetUserInfoUseCase getUserInfoUseCase) {
        this.view = view;
        this.getUserInfoUseCase = getUserInfoUseCase;
    }

    @Override
    public void getUserInfo() {
        getUserInfoUseCase.execute(GetUserInfoUseCase.generateParam(), new GetUserInfoSubscriber(view));
    }
}
