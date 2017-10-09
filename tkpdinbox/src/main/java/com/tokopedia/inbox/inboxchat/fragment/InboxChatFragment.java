package com.tokopedia.inbox.inboxchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatAdapter;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.fragment.InboxMessageFragment;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatFragment extends BaseDaggerFragment implements InboxChatContract.View, InboxMessageConstant{

    RecyclerView mainList;

    SwipeToRefresh swipeToRefresh;

    FloatingActionButton fab;

    @Inject
    InboxChatPresenter presenter;

    InboxChatAdapter adapter;
    RefreshHandler refreshHandler;

    boolean isRetryShowing = false;
    public boolean isMustRefresh = false;

    LinearLayoutManager layoutManager;
    TkpdProgressDialog progressDialog;
    SnackbarRetry snackbarRetry;
    Snackbar snackbarUndo;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static InboxChatFragment createInstance(String navigation) {
        InboxChatFragment fragment = new InboxChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, navigation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_inbox_message, container, false);

        initView(parentView);
        adapter = InboxChatAdapter.createAdapter(getActivity(), presenter);
        presenter.attachView(this);
        return parentView;
    }

    private void initView(View parentView) {
        mainList = (RecyclerView) parentView.findViewById(R.id.message_list);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        fab = (FloatingActionButton) parentView.findViewById(R.id.fab);
        refreshHandler = new RefreshHandler(getActivity(), parentView, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.refreshData();
            }
        });
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(adapter);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected void initInjector() {
        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .build();
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                .appComponent(daggerAppComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
//            presenter.setUserVisibleHint(isVisibleToUser, isMustRefresh);
//            if (snackbarRetry != null) {
//                snackbarRetry.resumeRetrySnackbar();
//                if (!isVisibleToUser)
//                    snackbarRetry.pauseRetrySnackbar();
//            }
//            if (snackbarUndo != null) {
//                snackbarUndo.dismiss();
//            }
//            finishContextMode();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNav() {
        return getArguments().getString(PARAM_NAV, "");
    }

    @Override
    public void enableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(true);

        if (adapter.getSelected() == 0)
            fab.show();

//        if (presenter.hasActionListener()) {
//            adapter.setEnabled(true);
//        }
    }

    @Override
    public void disableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(false);
        if (fab.isShown())
            fab.hide();
        adapter.setEnabled(false);
    }

    @Override
    public void finishContextMode() {

    }

    @Override
    public boolean hasRetry() {
        return isRetryShowing;
    }

    @Override
    public void overridePendingTransition(int i, int i1) {

    }

    @Override
    public String getFilter() {
        return null;
    }

    @Override
    public String getKeyword() {
        return null;
    }

    @Override
    public void showEmptyState(String localizedMessage) {

    }

    @Override
    public void showError(String localizedMessage) {

    }

    @Override
    public RefreshHandler getRefreshHandler() {
        return refreshHandler;
    }

    @Override
    public InboxChatAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void finishLoading() {
        refreshHandler.finishRefresh();
        adapter.showLoading(false);
        progressDialog.dismiss();
    }

    @Override
    public void setMustRefresh(boolean isMustRefresh) {
        this.isMustRefresh = isMustRefresh;
    }

    @Override
    public void removeError() {
        adapter.showEmptyFull(false);
        adapter.showRetry(false);
        adapter.showRetryFull(false);
        isRetryShowing = false;
        NetworkErrorHelper.hideEmptyState(getView());
    }

}
