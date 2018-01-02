package com.tokopedia.tokocash.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tokocash.historytokocash.presentation.activity.HistoryTokoCashActivity;

import dagger.Component;

/**
 * Created by nabillasabbaha on 12/27/17.
 */
@TokoCashScope
@Component(modules = TokoCashModule.class, dependencies = AppComponent.class)
public interface TokoCashComponent {

    void inject(HistoryTokoCashActivity historyTokoCashActivity);
}
