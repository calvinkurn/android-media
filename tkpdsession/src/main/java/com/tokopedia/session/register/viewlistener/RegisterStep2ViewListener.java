package com.tokopedia.session.register.viewlistener;

import android.content.Context;
import android.widget.EditText;

import com.tkpd.library.ui.widget.MaterialSpinner;
import com.tokopedia.session.register.model.RegisterViewModel;
import com.tokopedia.session.register.model.gson.RegisterResult;

/**
 * Created by nisie on 1/27/17.
 */

public interface RegisterStep2ViewListener {
    void showLoadingProgress();

    void finishLoadingProgress();

    void resetError();

    EditText getPhone();

    String getString(int resId);

    void setPhoneError(String errorMessage);

    void dropKeyboard();

    MaterialSpinner getGender();

    void setGenderError(String errorMessage);

    void onErrorRegister(String errorMessage);

    void onSuccessRegister(RegisterResult registerResult);

    void setRegisterModel(RegisterViewModel registerViewModel);

    Context getActivity();
}