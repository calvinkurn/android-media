package com.tokopedia.tokocash.activation.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.activation.presentation.fragment.SuccessActivateFragment;
import com.tokopedia.tokocash.di.TokoCashComponent;

import com.tokopedia.tokocash.di.DaggerTokoCashComponent;

/**
 * Created by nabillasabbaha on 7/25/17.
 */

public class SuccessActivateTokoCashActivity extends BaseSimpleActivity
        implements SuccessActivateFragment.ActionListener, HasComponent<TokoCashComponent> {

    private TokoCashComponent tokoCashComponent;

    public static Intent newInstance(Context context) {
        return new Intent(context, SuccessActivateTokoCashActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tokocash_toolbar_success_activation));
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.toolbar_no_icon_back;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected Fragment getNewFragment() {
        return SuccessActivateFragment.newInstance();
    }

    @Override
    public void onBackPressToHome() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
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