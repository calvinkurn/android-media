package com.tokopedia.tkpd.tkpdreputation.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment;

import dagger.Component;

/**
 * @author by nisie on 8/11/17.
 */

@ReputationScope
@Component(modules = ReputationModule.class, dependencies = AppComponent.class)
public interface ReputationComponent {

    void inject(InboxReputationFragment feedPlusFragment);

}
