package com.tokopedia.tokocash.activation.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tokocash.ApplinkConstant;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.activation.presentation.fragment.ActivateTokoCashFragment;
import com.tokopedia.tokocash.activation.presentation.fragment.RequestOTPWalletFragment;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;

/**
 * Created by nabillasabbaha on 7/24/17.
 */

public class ActivateTokoCashActivity extends BaseSimpleActivity
        implements ActivateTokoCashFragment.ActionListener, RequestOTPWalletFragment.ActionListener,
        HasComponent<TokoCashComponent> {

    private TokoCashComponent tokoCashComponent;

    @SuppressWarnings("unused")
    @DeepLink(ApplinkConstant.WALLET_ACTIVATION)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        Intent destination = ActivateTokoCashActivity.newInstance(context);
        destination.putExtra(ApplinkConstant.EXTRA_FROM_PUSH, true);
        return destination;
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, ActivateTokoCashActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return ActivateTokoCashFragment.newInstance();
    }

    @Override
    public void setTitlePage(String titlePage) {
        updateTitle(titlePage);
    }

    @Override
    public void directToSuccessActivateTokoCashPage() {
        startActivity(SuccessActivateTokoCashActivity.newInstance(getApplicationContext()));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void directPageToOTPWallet() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_view);
        if (fragment == null || !(fragment instanceof RequestOTPWalletFragment))
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_view,
                    RequestOTPWalletFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }

    @Override
    public TokoCashComponent getComponent() {
        if (tokoCashComponent == null) initInjector();
        return tokoCashComponent;
    }

    private void initInjector() {
        tokoCashComponent = DaggerTokoCashComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }
}