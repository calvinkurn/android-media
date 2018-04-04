package com.tokopedia.session.register.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.applink.SessionApplinkUrl;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.fragment.RegisterInitialFragment;

/**
 * @author by nisie on 10/10/17.
 */

public class RegisterInitialActivity extends TActivity implements HasComponent {

    @DeepLink({SessionApplinkUrl.REGISTER})
    public static Intent getCallingApplinkRegisterIntent(Context context, Bundle bundle) {
        if (SessionHandler.isV4Login(context)) {
            if (context.getApplicationContext() instanceof SessionRouter) {
                return ((SessionRouter) context.getApplicationContext()).getHomeIntent(context);
            } else {
                throw new RuntimeException("Applinks intent unsufficient");
            }
        } else {
            return getCallingIntent(context);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (RegisterInitialFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = RegisterInitialFragment.createInstance();
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    public String getScreenName() {
        return RegisterAnalytics.Screen.SCREEN_INITIAL_REGISTER;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterInitialActivity.class);
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
