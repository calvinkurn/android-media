package com.tokopedia.session.addname.fragment;

import android.os.Bundle;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.session.register.registerphonenumber.view.fragment.AddNameRegisterPhoneFragment;

/**
 * @author by nisie on 8/10/18.
 */
public class AddNameFragment extends AddNameRegisterPhoneFragment {

    public static AddNameRegisterPhoneFragment newInstance(Bundle bundle) {
        AddNameFragment fragment = new AddNameFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onContinueClick() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.addName(etName.getText().toString());
    }
}
