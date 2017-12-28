package com.tokopedia.tokocash.historytokocash.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tokocash.historytokocash.presentation.activity.HistoryTokoCashActivity;

import dagger.Component;

/**
 * Created by nabillasabbaha on 12/27/17.
 */
@HistoryTokoCashScope
@Component(modules = HistoryTokoCashModule.class, dependencies = AppComponent.class)
public interface HistoryTokoCashComponent {

    void inject(HistoryTokoCashActivity historyTokoCashActivity);
}
