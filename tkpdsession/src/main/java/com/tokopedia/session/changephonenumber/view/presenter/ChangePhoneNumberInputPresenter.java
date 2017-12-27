package com.tokopedia.session.changephonenumber.view.presenter;

import android.text.Editable;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberInputFragmentListener.View>
        implements ChangePhoneNumberInputFragmentListener.Presenter {

    ChangePhoneNumberInputFragmentListener.View view;

    public ChangePhoneNumberInputPresenter(ChangePhoneNumberInputFragmentListener.View view) {
        this.view = view;
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
        if (newNumber.length() >= 7 && newNumber.length() <= 15) {
            view.enableNextButton();
        } else {
            view.disableNextButton();
        }
    }
}