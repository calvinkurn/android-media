package com.tokopedia.session.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.session.R;
import com.tokopedia.session.login.view.di.DaggerReloginComponent;
import com.tokopedia.session.login.view.presenter.ReloginPresenter;

import javax.inject.Inject;

/**
 * @author by nisie on 5/26/17.
 */

public class ReloginActivity extends AppCompatActivity
        implements ReloginContract.View, HasComponent {

    @Inject
    ReloginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        presenter.attachView(this);
        presenter.makeLogin();
    }

    private void initInjector() {
        AppComponent appComponent = getComponent();

        DaggerReloginComponent reloginComponent =
                (DaggerReloginComponent) DaggerReloginComponent.builder()
                        .appComponent(appComponent)
                        .build();

        reloginComponent.inject(this);
    }


    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication())
                .getApplicationComponent();
    }

    @Override
    public void onErrorRelogin(String errorMessage) {
        Intent intent = new Intent();
        intent.putExtra(OldSessionRouter.PARAM_FORCE_LOGOUT_MESSAGE, errorMessage);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onSuccessRelogin() {
        Intent intent = new Intent();
        intent.putExtra(OldSessionRouter.PARAM_FORCE_LOGOUT_MESSAGE, getString(R.string.default_relogin));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}
