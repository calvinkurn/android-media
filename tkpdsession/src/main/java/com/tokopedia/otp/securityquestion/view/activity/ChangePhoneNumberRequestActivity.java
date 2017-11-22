package com.tokopedia.otp.securityquestion.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.otp.securityquestion.view.fragment.ChangePhoneNumberRequestFragment;
import com.tokopedia.otp.securityquestion.view.fragment.ChangePhoneNumberWaitFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 3/2/17.
 */

public class ChangePhoneNumberRequestActivity extends TActivity implements
        ChangePhoneNumberRequestFragment.ChangePhoneNumberRequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {
        ChangePhoneNumberRequestFragment fragment = ChangePhoneNumberRequestFragment.createInstance(this);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void goToThanksPage() {
        ChangePhoneNumberWaitFragment fragment = ChangePhoneNumberWaitFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ChangePhoneNumberRequestActivity.class);
    }

}
