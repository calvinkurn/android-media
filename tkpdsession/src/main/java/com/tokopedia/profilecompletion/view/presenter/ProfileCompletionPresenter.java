package com.tokopedia.profilecompletion.view.presenter;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;
import com.tokopedia.profilecompletion.view.subscriber.EditUserInfoSubscriber;
import com.tokopedia.profilecompletion.view.subscriber.GetUserInfoSubscriber;
import com.tokopedia.session.R;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class ProfileCompletionPresenter extends BaseDaggerPresenter<ProfileCompletionContract.View>
        implements ProfileCompletionContract.Presenter {

    private final EditUserProfileUseCase editUserProfileUseCase;
    private final SessionHandler sessionHandler;
    private GetUserInfoUseCase getUserInfoUseCase;

    @Inject
    ProfileCompletionPresenter(GetUserInfoUseCase getUserInfoUseCase,
                               EditUserProfileUseCase editUserProfileUseCase,
                               SessionHandler sessionHandler){
        this.getUserInfoUseCase = getUserInfoUseCase;
        this.editUserProfileUseCase = editUserProfileUseCase;
        this.sessionHandler = sessionHandler;
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
        if(date.isEmpty() || year.isEmpty() || month == 0){
            getView().onFailedEditProfile(getView().getString(R.string.invalid_date));
        }else {
            getView().disableView();
            editUserProfileUseCase.execute(EditUserProfileUseCase.generateParamDOB(date, String.valueOf(month), year), new EditUserInfoSubscriber(getView(), EditUserProfileUseCase.EDIT_DOB));
        }
    }

    @Override
    public void editUserInfo(int gender) {
        if(gender == -1) {
            getView().onFailedEditProfile(getView().getString(R.string.invalid_gender));
        }else {
            getView().disableView();
            editUserProfileUseCase.execute(EditUserProfileUseCase.generateParamGender(gender), new EditUserInfoSubscriber(getView()
                    , EditUserProfileUseCase.EDIT_GENDER));
        }
    }

    @Override
    public void editUserInfo(String verif) {

    }

    @Override
    public void skipView(String tag) {
        getView().skipView(tag);
    }

    public void setMsisdnVerifiedToCache(boolean isVerified) {
        SessionHandler.setIsMSISDNVerified(isVerified);
    }
}
