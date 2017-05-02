package com.tokopedia.inbox.rescenter.historyaddress.di.component;

import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.historyaddress.di.module.HistoryAddressModule;
import com.tokopedia.inbox.rescenter.historyaddress.HistoryAddressFragment;
import com.tokopedia.inbox.rescenter.historyaddress.di.scope.HistoryAddressScope;

import dagger.Component;

/**
 * Created by hangnadi on 4/17/17.
 */
@HistoryAddressScope
@Component(modules = HistoryAddressModule.class, dependencies = ResolutionDetailComponent.class)
public interface HistoryAddressComponent {

    void inject(HistoryAddressFragment fragment);

}
