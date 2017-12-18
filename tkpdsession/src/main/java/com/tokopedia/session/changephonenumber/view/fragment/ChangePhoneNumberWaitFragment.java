package com.tokopedia.session.changephonenumber.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 3/8/17.
 */

public class ChangePhoneNumberWaitFragment extends BasePresenterFragment {

    TextView buttonReturn;

    public static ChangePhoneNumberWaitFragment createInstance() {
        return new ChangePhoneNumberWaitFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_change_phone_number_wait;
    }

    @Override
    protected void initView(View view) {
        buttonReturn = (TextView) view.findViewById(R.id.button_return);
    }

    @Override
    protected void setViewListener() {
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
