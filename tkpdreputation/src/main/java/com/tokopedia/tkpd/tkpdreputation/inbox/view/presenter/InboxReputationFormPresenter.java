package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;

import javax.inject.Inject;

/**
 * @author by nisie on 8/20/17.
 */

public class InboxReputationFormPresenter
        extends BaseDaggerPresenter<InboxReputationForm.View>
        implements InboxReputationForm.Presenter {

    private InboxReputationForm.View viewListener;

    @Inject
    InboxReputationFormPresenter() {
    }

    @Override
    public void attachView(InboxReputationForm.View view) {
        super.attachView(view);
        this.viewListener = view;
    }
}
