package com.tokopedia.otp.cotp.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.otp.cotp.view.fragment.InterruptVerificationFragment;
import com.tokopedia.session.R;

/**
 * @author by nisie on 1/4/18.
 */

public class InterruptVerificationActivity extends TActivity implements HasComponent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null)
            fragment = InterruptVerificationFragment.createInstance(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, InterruptVerificationFragment.class
                .getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }
}
