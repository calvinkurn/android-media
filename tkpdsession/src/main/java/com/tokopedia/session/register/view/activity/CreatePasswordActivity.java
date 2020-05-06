package com.tokopedia.session.register.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.fragment.CreatePasswordFragment;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 10/12/17.
 */

public class CreatePasswordActivity extends TActivity implements HasComponent {

    public static final String ARGS_FORM_DATA = "ARGS_FORM_DATA";
    private static final String PARAM_FULL_NAME = "name";
    private static final String PARAM_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    /**
     * INTERNAL USE ONLY
     * Remember to pass PARAM_FULL_NAME and PARAM_USER_ID as well
     */
    @DeepLink({ApplinkConst.CREATE_PASSWORD})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        String name = bundle.getString(PARAM_FULL_NAME, "");
        String userId = bundle.getString(PARAM_USER_ID, "");
        Intent intent = getCallingIntentForApplink(context, name, userId);
        return intent.setData(uri.build());
    }

    private void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (CreatePasswordFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = CreatePasswordFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    public String getScreenName() {
        return RegisterAnalytics.Screen.SCREEN_CREATE_PASSWORD;
    }

    private static Intent getCallingIntentForApplink(Context context, String name, String userId) {
        Bundle bundle = new Bundle();
        CreatePasswordViewModel createPasswordViewModel = new CreatePasswordViewModel(
                "",
                name,
                0,
                0,
                0,
                new ArrayList<>(),
                userId
        );
        bundle.putParcelable(ARGS_FORM_DATA, createPasswordViewModel);
        Intent intent = new Intent(context, CreatePasswordActivity.class);
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
