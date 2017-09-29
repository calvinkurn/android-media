package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.di.DaggerInboxReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationReviewDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationReviewDetailPresenter;

import javax.inject.Inject;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationReviewDetailFragment extends BaseDaggerFragment
        implements InboxReputationReviewDetail.View {

    @Inject
    InboxReputationReviewDetailPresenter presenter;

    public static InboxReputationReviewDetailFragment createInstance() {
        InboxReputationReviewDetailFragment fragment = new InboxReputationReviewDetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION_REVIEW_DETAIL;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerInboxReputationComponent reputationComponent =
                (DaggerInboxReputationComponent) DaggerInboxReputationComponent
                        .builder()
                        .appComponent(appComponent)
                        .build();
        reputationComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_review_detail,
                container,
                false);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {

    }


}
