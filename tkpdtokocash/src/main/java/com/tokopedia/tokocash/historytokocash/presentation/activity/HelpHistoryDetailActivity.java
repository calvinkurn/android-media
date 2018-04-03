package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.di.DaggerTokoCashComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.HelpHistoryDetailFragment;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public class HelpHistoryDetailActivity extends BaseSimpleActivity implements HasComponent<TokoCashComponent>,
        HelpHistoryDetailFragment.ActionListener {

    public static final String TRANSACTION_ID = "transaction_id";

    private TokoCashComponent tokoCashComponent;

    public static Intent newInstance(Context context) {
        return new Intent(context, HelpHistoryDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.title_help_history));
    }

    @Override
    protected Fragment getNewFragment() {
        return HelpHistoryDetailFragment.newInstance(getIntent().getStringExtra(TRANSACTION_ID));
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
    public void successSubmitHelp() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackToHome() {
        onBackPressed();
    }
}
