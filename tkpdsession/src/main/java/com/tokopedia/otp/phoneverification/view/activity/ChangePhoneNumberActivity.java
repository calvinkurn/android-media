package com.tokopedia.otp.phoneverification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/23/17.
 */

public class ChangePhoneNumberActivity extends TActivity implements HasComponent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {

        ChangePhoneNumberFragment fragment = ChangePhoneNumberFragment.createInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();

    }

    public static Intent getChangePhoneNumberIntent(Context context, String phoneNumber) {
        Intent intent = new Intent(context, ChangePhoneNumberActivity.class);
        intent.putExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER, phoneNumber);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
