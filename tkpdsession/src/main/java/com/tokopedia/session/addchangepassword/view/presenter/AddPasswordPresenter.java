package com.tokopedia.session.addchangepassword.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.addchangepassword.domain.usecase.AddPasswordUseCase;
import com.tokopedia.session.addchangepassword.view.listener.AddPasswordListener;
import com.tokopedia.session.addchangepassword.view.subscriber.AddPasswordSubscriber;

import javax.inject.Inject;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordPresenter
        extends BaseDaggerPresenter<AddPasswordListener.View>
        implements AddPasswordListener.Presenter {

    private final static int MIN_COUNT = 8;
    private final static int MAX_COUNT = 32;
    private final static String ERROR_FIELD_REQUIRED = "Harus diisi";
    private final static String ERROR_MIN_CHAR = "minimum 8 karakter";
    private final static String ERROR_MAX_CHAR = "Maksimum 32 karakter";

    private AddPasswordUseCase addPasswordUseCase;

    @Inject
    public AddPasswordPresenter(AddPasswordUseCase addPasswordUseCase) {
        this.addPasswordUseCase = addPasswordUseCase;
    }

    @Override
    public void initView() {

    }

    @Override
    public void submitPassword(String password) {
        getView().showLoading();
        addPasswordUseCase.execute(
                AddPasswordUseCase.getParams(
                        SessionHandler.getLoginID(getView().getContext()), password),
                new AddPasswordSubscriber(getView()));
    }

    @Override
    public void checkPassword(String password) {
        if (isValidPassword(password)) {
            getView().enableNextButton();
        } else {
            getView().disableNextButton();
        }
    }

    private boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            getView().setErrorMessage(ERROR_FIELD_REQUIRED);
            return false;
        } else if (password.length() < MIN_COUNT) {
            getView().setErrorMessage(ERROR_MIN_CHAR);
            return false;
        } else if (password.length() > MAX_COUNT) {
            getView().setErrorMessage(ERROR_MAX_CHAR);
            return false;
        } else {
            getView().setErrorMessage("");
            return true;
        }
    }

    @Override
    public void attachView(AddPasswordListener.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        addPasswordUseCase.unsubscribe();
    }
}
