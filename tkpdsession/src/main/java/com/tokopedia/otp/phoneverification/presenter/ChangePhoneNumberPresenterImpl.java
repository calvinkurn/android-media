package com.tokopedia.otp.phoneverification.presenter;

import com.tokopedia.otp.phoneverification.listener.ChangePhoneNumberView;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/24/17.
 */

public class ChangePhoneNumberPresenterImpl implements ChangePhoneNumberPresenter {

    private final ChangePhoneNumberView viewListener;

    public ChangePhoneNumberPresenterImpl(ChangePhoneNumberView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void changePhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            viewListener.onSuccessChangePhoneNumber();
        }
    }

    private boolean isValid(String phoneNumber) {
        boolean isValid = true;
        if (phoneNumber.length() == 0) {
            isValid = false;
            viewListener.getPhoneNumberEditText().setError(
                    viewListener.getString(R.string.error_field_required));
            viewListener.getPhoneNumberEditText().requestFocus();
        }
        return isValid;
    }
}
