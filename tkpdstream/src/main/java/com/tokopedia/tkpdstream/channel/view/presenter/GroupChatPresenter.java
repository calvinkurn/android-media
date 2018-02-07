package com.tokopedia.tkpdstream.channel.view.presenter;

import android.util.Log;

import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.channel.domain.usecase.LoginSendbirdUseCase;
import com.tokopedia.tkpdstream.channel.view.listener.GroupChatContract;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatPresenter extends BaseDaggerPresenter<GroupChatContract.View> implements
        GroupChatContract.Presenter {

    private final LoginSendbirdUseCase loginUseCase;

    @Inject
    public GroupChatPresenter(LoginSendbirdUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void getMessagesFirstTime() {
        loginUseCase.execute("NISNIS1", new LoginSendbirdUseCase.LoginSendbirdListener() {
            @Override
            public void onConnected(User user) {
                Log.d("NISNIS", "onCONNECTED");
            }

            @Override
            public void onError(SendBirdException e) {
                Log.d("NISNIS", e.getCode() + "");

            }
        });
    }
}
