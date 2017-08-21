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
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationDetailActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationFormPresenter;

import javax.inject.Inject;

/**
 * @author by nisie on 8/20/17.
 */

public class InboxReputationFormFragment extends BaseDaggerFragment
        implements InboxReputationForm.View {

    @Inject
    InboxReputationFormPresenter presenter;

    public static InboxReputationFormFragment createInstance(String id) {
        InboxReputationFormFragment fragment = new InboxReputationFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InboxReputationDetailActivity.ARGS_REPUTATION_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_REVIEW_FORM;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
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
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_form, container,
                false);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {

    }
}
