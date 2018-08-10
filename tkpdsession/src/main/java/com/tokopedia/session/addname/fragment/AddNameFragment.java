package com.tokopedia.session.addname.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.session.R;

/**
 * @author by nisie on 8/10/18.
 */
public class AddNameFragment extends com.tokopedia.session.register.registerphonenumber.view.fragment.AddNameFragment {

    @Override
    protected void setViewListener() {
        super.setViewListener();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.addName(etName.getText().toString());
            }
        });
        btnContinue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.btn_continue || id == EditorInfo.IME_NULL) {
                    presenter.addName(etName.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }


}
