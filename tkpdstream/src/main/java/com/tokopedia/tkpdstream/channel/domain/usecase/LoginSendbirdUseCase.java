package com.tokopedia.tkpdstream.channel.domain.usecase;

import android.util.Log;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class LoginSendbirdUseCase {

    public interface LoginSendbirdListener {
        void onConnected(User user);

        void onError(SendBirdException e);
    }

    @Inject
    public LoginSendbirdUseCase() {
    }

    public void execute(final String userId, final LoginSendbirdListener listener) {
        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    Log.d("NISNIS", e.getCode() + "");
                    listener.onError(e);
                } else {
                    Log.d("NISNIS", user.getNickname());
                    listener.onConnected(user);
                }
            }
        });
    }
}
