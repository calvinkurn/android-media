package com.tokopedia.session.changephonenumber.view.presenter;

import android.text.Editable;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberInputFragmentListener.View>
        implements ChangePhoneNumberInputFragmentListener.Presenter {
    private static final int MINIMUM_NUMBER_LENGTH = 7;
    private static final int MAXIMUM_NUMBER_LENGTH = 15;

    private ChangePhoneNumberInputFragmentListener.View view;

    public ChangePhoneNumberInputPresenter() {
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
    public void onNewNumberTextChanged(Editable editable) {
        String newNumber = editable.toString().replaceAll("\\s+", "");
        newNumber = CustomPhoneNumberUtil.transform(newNumber);

        if (isNumberLengthValid(newNumber)) {
            view.enableNextButton();
        } else {
            view.disableNextButton();
        }

        if (editable.toString().length() != newNumber.length()) {
            view.correctPhoneNumber(newNumber);
        }
    }

    private boolean isNumberLengthValid(String newNumber) {
        newNumber = newNumber.replace("-", "");
        return (newNumber.length() >= MINIMUM_NUMBER_LENGTH && newNumber.length() <= MAXIMUM_NUMBER_LENGTH);
    }
}