package com.tokopedia.otp.phoneverification.view.presenter;

/**
 * Created by nisie on 2/24/17.
 */
public interface ChangePhoneNumberPresenter {
    void changePhoneNumber(String phoneNumber);

    void unsubscribeObservable();
}
