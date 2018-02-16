package com.tokopedia.otp.phoneverification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.otp.phoneverification.domain.interactor.ChangePhoneNumberUseCase;
import com.tokopedia.otp.phoneverification.view.listener.ChangePhoneNumber;
import com.tokopedia.otp.phoneverification.view.subscriber.ChangePhoneNumberSubscriber;
import com.tokopedia.session.R;

import javax.inject.Inject;

/**
 * Created by nisie on 2/24/17.
 */
public class ChangePhoneNumberPresenter extends BaseDaggerPresenter<ChangePhoneNumber.View> implements
        ChangePhoneNumber.Presenter {


    private final ChangePhoneNumberUseCase changePhoneNumberUseCase;
    private ChangePhoneNumber.View viewListener;

    @Inject
    public ChangePhoneNumberPresenter(ChangePhoneNumberUseCase changePhoneNumberUseCase) {
        this.changePhoneNumberUseCase = changePhoneNumberUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumber.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        changePhoneNumberUseCase.unsubscribe();
    }

    @Override
    public void changePhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            changePhoneNumberUseCase.execute(ChangePhoneNumberUseCase.getParam(
                    viewListener.getPhoneNumberEditText().getText().toString().replace("-", "")
                    ),
                    new ChangePhoneNumberSubscriber(viewListener));
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
