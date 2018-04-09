package com.tokopedia.tokocash.accountsetting.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.accountsetting.presentation.fragment.AccountSettingFragment;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public class AccountSettingActivity extends BaseSimpleActivity implements HasComponent<TokoCashComponent>,
        AccountSettingFragment.ActionListener {

    public static final int REQUEST_CODE = 870;
    public static final String KEY_INTENT_RESULT = "result";
    public static final String VALUE_INTENT_RESULT = "delete_all";
    private TokoCashComponent tokoCashComponent;

    public static Intent newInstance(Context context) {
        return new Intent(context, AccountSettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tokocash_title_account_setting));
    }

    @Override
    protected Fragment getNewFragment() {
        return AccountSettingFragment.newInstance();
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

    @Override
    public void directPageToHome() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_INTENT_RESULT, VALUE_INTENT_RESULT);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
