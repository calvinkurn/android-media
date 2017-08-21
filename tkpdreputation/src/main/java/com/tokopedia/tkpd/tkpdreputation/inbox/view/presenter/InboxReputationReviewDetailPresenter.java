package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationReviewDetail;

import javax.inject.Inject;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationReviewDetailPresenter
        extends BaseDaggerPresenter<InboxReputationReviewDetail.View>
        implements InboxReputationReviewDetail.Presenter {

    private InboxReputationReviewDetail.View viewListener;

    @Inject
    InboxReputationReviewDetailPresenter() {
    }

    @Override
    public void attachView(InboxReputationReviewDetail.View view) {
        super.attachView(view);
        this.viewListener = view;
    }
}
