package com.tokopedia.profilecompletion.view.presenter;

import android.os.Bundle;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;
import com.tokopedia.profilecompletion.view.subscriber.EditUserInfoSubscriber;
import com.tokopedia.profilecompletion.view.subscriber.GetUserInfoSubscriber;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class ProfileCompletionPresenter extends BaseDaggerPresenter<ProfileCompletionContract.View> implements ProfileCompletionContract.Presenter {

    private final EditUserProfileUseCase editUserProfileUseCase;
    private GetUserInfoUseCase getUserInfoUseCase;

    @Inject
    ProfileCompletionPresenter(GetUserInfoUseCase getUserInfoUseCase,
                               EditUserProfileUseCase editUserProfileUseCase){
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.editUserProfileUseCase = editUserProfileUseCase;
    }

    @Override
    public void attachView(ProfileCompletionContract.View view) {
        super.attachView(view);
        getUserInfo();
    }

    @Override
    public void detachView() {
        super.detachView();
        getUserInfoUseCase.unsubscribe();
        editUserProfileUseCase.unsubscribe();
    }

    @Override
    public void getUserInfo() {
        getUserInfoUseCase.execute(GetUserInfoUseCase.generateParam(), new GetUserInfoSubscriber(getView()));
    }

    @Override
    public void editUserInfo(String date, int month, String year) {
        editUserProfileUseCase.execute(EditUserProfileUseCase.generateParamDOB(date, String.valueOf(month), year), new EditUserInfoSubscriber(getView(), EditUserProfileUseCase.EDIT_DOB));
    }

    @Override
    public void editUserInfo(int gender) {
        editUserProfileUseCase.execute(EditUserProfileUseCase.generateParamGender(gender), new EditUserInfoSubscriber(getView()
                , EditUserProfileUseCase.EDIT_GENDER));
    }

    @Override
    public void editUserInfo(String verif) {

    }

    @Override
    public void skipView(String tag) {
        getView().skipView(tag);
    }
}
