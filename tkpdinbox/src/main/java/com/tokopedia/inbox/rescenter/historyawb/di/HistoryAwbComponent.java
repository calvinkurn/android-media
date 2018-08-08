package com.tokopedia.inbox.rescenter.historyawb.di;

import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.historyawb.di.module.HistoryAwbModule;
import com.tokopedia.inbox.rescenter.historyawb.HistoryShippingFragment;
import com.tokopedia.inbox.rescenter.historyawb.di.scope.HistoryAwbScope;

import dagger.Component;

/**
 * Created by hangnadi on 4/17/17.
 */
@HistoryAwbScope
@Component(modules = HistoryAwbModule.class, dependencies = ResolutionDetailComponent.class)
public interface HistoryAwbComponent {

    void inject(HistoryShippingFragment fragment);

}
