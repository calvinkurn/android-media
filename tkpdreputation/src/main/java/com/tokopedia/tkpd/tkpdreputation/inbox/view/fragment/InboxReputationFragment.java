package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationFragment extends BaseDaggerFragment
        implements InboxReputation.View {

    private final static String PARAM_TAB = "tab";

    private RecyclerView mainList;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayoutManager layoutManager;
    private InboxReputationAdapter adapter;

    @Inject
    InboxReputationPresenter presenter;

    public static Fragment createInstance(int tab) {
        InboxReputationFragment fragment = new InboxReputationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_TAB, tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    private void initVar() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = InboxReputationAdapter.createAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation, container, false);
        mainList = (RecyclerView) parentView.findViewById(R.id.review_list);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getFirstTimeInboxReputation(getTab());
    }


    public int getTab() {
        return getArguments().getInt(PARAM_TAB, 1);
    }

    @Override
    public void showLoadingFull() {
        adapter.showLoadingFull(true);
    }

    @Override
    public void onErrorGetFirstTimeInboxReputation(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new
                NetworkErrorHelper
                        .RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getFirstTimeInboxReputation(getTab());
                    }
                });
    }

    @Override
    public void onSuccessGetFirstTimeInboxReputation(InboxReputationViewModel inboxReputationViewModel) {
        adapter.showLoadingFull(false);
        adapter.setList(inboxReputationViewModel.getList());
    }
}
