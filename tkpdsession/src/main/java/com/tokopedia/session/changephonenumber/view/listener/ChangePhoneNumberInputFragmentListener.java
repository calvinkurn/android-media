package com.tokopedia.session.changephonenumber.view.listener;

import android.text.Editable;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by milhamj on 20/12/17.
 */

public interface ChangePhoneNumberInputFragmentListener {
    public interface View extends CustomerView {
        void enableNextButton();

        void disableNextButton();

        void correctPhoneNumber(String newNumber);

        void showLoading();

        void dismissLoading();

        void onValidateNumberSuccess(Boolean isSuccess);

        void onValidateNumberError(String message);

        void onValidateNumberFailed();

        void onSubmitNumberSuccess(Boolean isSuccess);

        void onSubmitNumberError(String message);

        void onSubmitNumberFailed();
    }

    public interface Presenter extends CustomerPresenter<View> {
        void initView();

        void onNewNumberTextChanged(Editable editable);

        void validateNumber(String newPhoneNumber);

        void submitNumber(String newPhoneNumber);
    }
}
