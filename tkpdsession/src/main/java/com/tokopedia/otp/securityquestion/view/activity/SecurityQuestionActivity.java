package com.tokopedia.otp.securityquestion.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.otp.securityquestion.view.fragment.SecurityQuestionFragment;
import com.tokopedia.session.R;
import com.tokopedia.session.data.viewmodel.SecurityDomain;

/**
 * @author by nisie on 10/18/17.
 */

public class SecurityQuestionActivity extends TActivity implements HasComponent {

    public static final String ARGS_QUESTION = "ARGS_QUESTION";
    public static final String ARGS_NAME = "ARGS_NAME";
    public static final String ARGS_EMAIL = "ARGS_EMAIL";
    public static final String ARGS_PHONE = "ARGS_PHONE";

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

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (SecurityQuestionFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = SecurityQuestionFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SECURITY_QUESTION;
    }

    public static Intent getCallingIntent(Context context,
                                          SecurityDomain securityDomain,
                                          String name,
                                          String email,
                                          String phone) {
        Intent intent = new Intent(context, SecurityQuestionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_QUESTION, securityDomain);
        bundle.putString(ARGS_NAME, name);
        bundle.putString(ARGS_EMAIL, email);
        bundle.putString(ARGS_PHONE, phone);
        intent.putExtras(bundle);
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
