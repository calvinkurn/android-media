package com.tokopedia.tokocash.historytokocash.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tokocash.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.DetailTransactionFragment;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;

import com.tokopedia.tokocash.di.DaggerTokoCashComponent;

/**
 * Created by nabillasabbaha on 2/12/18.
 */

public class DetailTransactionActivity extends BaseSimpleActivity
        implements HasComponent<TokoCashComponent>, DetailTransactionFragment.ActionListener {

    private TokoCashComponent tokoCashComponent;

    public static Intent newInstance(Context context, ItemHistory itemHistory) {
        Intent intent = new Intent(context, DetailTransactionActivity.class);
        intent.putExtra(DetailTransactionFragment.ITEM_HISTORY_KEY, itemHistory);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return DetailTransactionFragment
                .newInstance((ItemHistory) getIntent()
                        .getParcelableExtra(DetailTransactionFragment.ITEM_HISTORY_KEY));
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
    public void setTitle(String title) {
        updateTitle(title);
    }
}
