package com.tokopedia.inbox.rescenter.inboxv2.view.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.rescenter.createreso.view.di.CreateResoModule;
import com.tokopedia.inbox.rescenter.inboxv2.view.fragment.ResoInboxFragment;

import dagger.Component;

/**
 * Created by yfsx on 24/01/18.
 */


@ResoInboxScope
@Component(modules = ResoInboxModule.class, dependencies = AppComponent.class)
public interface ResoInboxComponent {
    void inject(ResoInboxFragment fragment);

}
