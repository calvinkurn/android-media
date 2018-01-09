package com.tokopedia.session.changephonenumber.view.presenter;

import android.text.Editable;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.session.changephonenumber.domain.interactor.ValidateNumberUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.session.changephonenumber.view.subscriber.SubmitNumberSubscriber;
import com.tokopedia.session.changephonenumber.view.subscriber.ValidateNumberSubscriber;

import javax.inject.Inject;

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
            int lengthDifference = Math.abs(editable.toString().length() - newNumber.length());
            if (selection >= 0 && selection <= newNumber.length())
                view.correctPhoneNumber(newNumber, selection + lengthDifference);
            else
                view.correctPhoneNumber(newNumber, newNumber.length());
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

    private RequestParams getValidateNumberParam(String newPhoneNumber) {
        RequestParams param = RequestParams.create();
        param.putString(ValidateNumberUseCase.PARAM_ACTION, ValidateNumberUseCase.ACTION_VALIDATE);
        param.putString(ValidateNumberUseCase.PARAM_NEW_MSISDN, newPhoneNumber);
        return param;
    }

    private RequestParams getSubmitNumberParam(String newPhoneNumber) {
        RequestParams param = RequestParams.create();
        param.putString(ValidateNumberUseCase.PARAM_ACTION, ValidateNumberUseCase.ACTION_SUMBIT);
        param.putString(ValidateNumberUseCase.PARAM_NEW_MSISDN, newPhoneNumber);
        return param;
    }
}