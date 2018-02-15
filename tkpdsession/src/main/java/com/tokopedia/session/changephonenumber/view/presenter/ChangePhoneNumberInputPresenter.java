package com.tokopedia.session.changephonenumber.view.presenter;

import android.text.Editable;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.util.CustomPhoneNumberUtil;
import com.tokopedia.session.changephonenumber.domain.interactor.ValidateNumberUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.session.changephonenumber.view.subscriber.SubmitNumberSubscriber;
import com.tokopedia.session.changephonenumber.view.subscriber.ValidateNumberSubscriber;

import javax.inject.Inject;

import static com.tokopedia.session.changephonenumber.domain.interactor.ValidateNumberUseCase
        .getSubmitNumberParam;
import static com.tokopedia.session.changephonenumber.domain.interactor.ValidateNumberUseCase
        .getValidateNumberParam;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberInputFragmentListener.View>
        implements ChangePhoneNumberInputFragmentListener.Presenter {
    private static final int MINIMUM_NUMBER_LENGTH = 7;
    private static final int MAXIMUM_NUMBER_LENGTH = 15;

    private final ValidateNumberUseCase validateNumberUseCase;
    private ChangePhoneNumberInputFragmentListener.View view;

    @Inject
    public ChangePhoneNumberInputPresenter(ValidateNumberUseCase validateNumberUseCase) {
        this.validateNumberUseCase = validateNumberUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumberInputFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void initView() {

    }

    @Override
    public void onNewNumberTextChanged(Editable editable, int selection) {
        String newNumber = editable.toString().replaceAll("\\s+", "");
        newNumber = CustomPhoneNumberUtil.transform(newNumber);

        if (isNumberLengthValid(newNumber)) {
            view.enableNextButton();
        } else {
            view.disableNextButton();
        }

        if (editable.toString().length() != newNumber.length()) {
            int lengthDifference = newNumber.length() - editable.toString().length();
            if (selection + lengthDifference < 0)
                view.correctPhoneNumber(newNumber, 0);
            else if (selection > newNumber.length())
                view.correctPhoneNumber(newNumber, newNumber.length());
            else
                view.correctPhoneNumber(newNumber, selection + lengthDifference);

        }
    }

    private boolean isNumberLengthValid(String newNumber) {
        newNumber = newNumber.replace("-", "");
        return (newNumber.length() >= MINIMUM_NUMBER_LENGTH && newNumber.length() <=
                MAXIMUM_NUMBER_LENGTH);
    }

    @Override
    public void validateNumber(String newPhoneNumber) {
        view.showLoading();
        validateNumberUseCase.execute(getValidateNumberParam(newPhoneNumber),
                new ValidateNumberSubscriber(view));
    }

    @Override
    public void submitNumber(String newPhoneNumber) {
        view.showLoading();
        validateNumberUseCase.execute(getSubmitNumberParam(newPhoneNumber),
                new SubmitNumberSubscriber(view));
    }
}