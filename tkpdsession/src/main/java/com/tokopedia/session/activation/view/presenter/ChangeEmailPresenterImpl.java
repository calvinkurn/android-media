package com.tokopedia.session.activation.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.domain.interactor.ChangeEmailUseCase;
import com.tokopedia.session.activation.view.subscriber.ChangeEmailSubscriber;
import com.tokopedia.session.activation.view.viewListener.ChangeEmailView;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailPresenterImpl implements ChangeEmailPresenter {

    private final ChangeEmailView viewListener;
    private ChangeEmailUseCase changeEmailUseCase;

    public ChangeEmailPresenterImpl(ChangeEmailView viewListener,
                                    ChangeEmailUseCase changeEmailUseCase) {
        this.viewListener = viewListener;
        this.changeEmailUseCase = changeEmailUseCase;

    }

    @Override
    public void changeEmail(RequestParams changeEmailParam) {
        if (isValid()) {
            viewListener.showLoadingProgress();
            changeEmailUseCase.execute(changeEmailParam, new ChangeEmailSubscriber(viewListener));
        }
    }

    @Override
    public void unsubscribeObservable() {
        changeEmailUseCase.unsubscribe();
    }

    @Override
    public RequestParams getChangeEmailParam(String oldEmail, String newEmail, String password) {
        RequestParams params = RequestParams.create();
        params.putString(ChangeEmailUseCase.PARAM_NEW_EMAIL, newEmail);
        params.putString(ChangeEmailUseCase.PARAM_OLD_EMAIL, oldEmail);
        params.putString(ChangeEmailUseCase.PARAM_PASSWORD, password);
        params.putString(ChangeEmailUseCase.PARAM_SEND_EMAIL, ChangeEmailUseCase.DEFAULT_SEND_EMAIL);
        return params;
    }

    private boolean isValid() {
        boolean isValid = true;

        if (viewListener.getPasswordEditText().getText().toString().length() == 0) {
            viewListener.getPasswordEditText().setError(viewListener.getString(R.string.error_field_required));
            viewListener.getPasswordEditText().requestFocus();
            isValid = false;
        }

        if (viewListener.getNewEmailEditText().getText().toString().trim().length() == 0) {
            viewListener.getNewEmailEditText().setError(viewListener.getString(R.string.error_field_required));
            viewListener.getNewEmailEditText().requestFocus();
            isValid = false;
        } else if (!CommonUtils.EmailValidation(viewListener.getNewEmailEditText().getText().toString())) {
            viewListener.getNewEmailEditText().setError(viewListener.getString(R.string.error_invalid_email));
            viewListener.getNewEmailEditText().requestFocus();
            isValid = false;
        }
        return isValid;
    }
}
