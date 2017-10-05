package com.tokopedia.inbox.rescenter.historyaction.di.component;

import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.historyaction.HistoryActionFragment;
import com.tokopedia.inbox.rescenter.historyaction.di.module.HistoryActionModule;
import com.tokopedia.inbox.rescenter.historyaction.di.scope.HistoryActionScope;

import dagger.Component;

/**
 * Created by hangnadi on 4/17/17.
 */
@HistoryActionScope
@Component(modules = HistoryActionModule.class, dependencies = ResolutionDetailComponent.class)
public interface HistoryActionComponent {

    void inject(HistoryActionFragment fragment);

}
