package com.tokopedia.tkpd.tkpdreputation.inbox.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFormFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationReportFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationReviewDetailFragment;

import dagger.Component;

/**
 * @author by nisie on 8/11/17.
 */

@InboxReputationScope
@Component(modules = InboxReputationModule.class, dependencies = AppComponent.class)
public interface InboxReputationComponent {

    void inject(InboxReputationFragment inboxReputationFragment);

    void inject(InboxReputationDetailFragment inboxReputationDetailFragment);

    void inject(InboxReputationFormFragment inboxReputationFormFragment);

    void inject(InboxReputationReviewDetailFragment inboxReputationReviewDetailFragment);

    void inject(InboxReputationReportFragment inboxReputationReportFragment);

}
