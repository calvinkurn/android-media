package com.tokopedia.session.activation.view.viewListener;

import android.app.Activity;
import android.widget.EditText;

/**
 * Created by nisie on 4/18/17.
 */

public interface ChangeEmailView {

    EditText getOldEmailEditText();
    EditText getNewEmailEditText();
    EditText getPasswordEditText();

    String getString(int resId);

    Activity getActivity();

    void onErrorChangeEmail(String errorMessage);

    void onSuccessChangeEmail();

    void showLoadingProgress();
}
