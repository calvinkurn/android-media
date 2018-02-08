package com.tokopedia.inbox.inboxtalk.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.talk.model.model.InboxTalk;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.inbox.inboxtalk.InboxTalkFilterDialog;
import com.tokopedia.inbox.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.inbox.inboxtalk.adapter.InboxTalkAdapter;
import com.tokopedia.inbox.inboxtalk.listener.InboxTalkView;
import com.tokopedia.inbox.inboxtalk.presenter.InboxTalkPresenter;
import com.tokopedia.inbox.inboxtalk.presenter.InboxTalkPresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by stevenfredian on 4/5/16.
 */
public class InboxTalkFragment extends BasePresenterFragment<InboxTalkPresenter> implements InboxTalkView {
    public final static int GO_TO_DETAIL = 2;
    public final static int RESULT_DELETE = 4;

    String Nav;
    boolean forceUnread, isRequest;
    String filterString;
    RefreshHandler refresh;
    PagingHandler pagingHandler;
    GridLayoutManager layoutManager;
    List<RecyclerViewItem> items;
    InboxTalkAdapter adapter;
    private InboxTalkFilterDialog dialog;

    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    SnackbarRetry snackbarRetry;

    public static Fragment createInstance(String nav, Boolean forceUnread) {
        Bundle bundle = new Bundle();
        InboxTalkFragment fragment = new InboxTalkFragment();
        bundle.putString("nav", nav);
        bundle.putBoolean("unread", forceUnread);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onErrorAction(Bundle resultData, int resultCode) {
        adapter.onErrorAction(resultData, resultCode);
    }

    public void onSuccessAction(Bundle resultData, int resultCode) {
        adapter.onSuccessAction(resultData, resultCode, presenter.getPositionAction());
    }

    public String getNav() {
        return this.Nav;
    }

    public void filter(String s) {
        refresh.setIsRefreshing(true);
        refresh.setRefreshing(true);
        filterString = s;
        firstRequest();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        adapter = InboxTalkAdapter.createAdapter(getActivity(), this, items, false, true, presenter);

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getUserVisibleHint()) {
            displayLoading(true);
            requestFromCache();
        }
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.saveState(state, items, layoutManager.findLastCompletelyVisibleItemPosition(), filterString);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.restoreState(savedState);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTalkPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        Bundle bundle = this.getArguments();
        Nav = bundle.getString("nav");
        forceUnread = bundle.getBoolean("unread", false);
        filterString = "all";
        context = getActivity();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_inbox_talk;
    }

    @Override
    protected void initView(View view) {
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        recyclerView = (RecyclerView) view.findViewById(R.id.talk_list);
        progressBar = (ProgressBar) view.findViewById(R.id.include_loading);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        refresh = new RefreshHandler(getActivity(), view, onRefreshListener());
        displayLoading(true);
        dialog = InboxTalkFilterDialog.Builder(getActivity(), this);
        dialog.setView();
        dialog.setListener();
    }

    @Override
    protected void setViewListener() {
        recyclerView.addOnScrollListener(OnScrollRecyclerView());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("steven", "testing FAB");
                dialog.show();
            }
        });
    }

    @Override
    protected void initialVar() {
        if (forceUnread) {
            filterString = "unread";
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {

    }

    private RecyclerView.OnScrollListener OnScrollRecyclerView() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastPosition() && !isRequest && !refresh.isRefreshing() && !isSnackbarVisible(snackbarRetry)) {
                    request();
                }
            }
        };
    }

    private boolean isSnackbarVisible(SnackbarRetry snackbarRetry) {
        return snackbarRetry != null && snackbarRetry.isShown();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (snackbarRetry != null) {
            snackbarRetry.resumeRetrySnackbar();
            if (!isVisibleToUser) {
                snackbarRetry.pauseRetrySnackbar();
            }
        }

        if (isVisibleToUser
                && adapter != null
                && adapter.getData() != null
                && adapter.getData().isEmpty()
                && (snackbarRetry == null || (snackbarRetry != null && !snackbarRetry.isShown()))) {
            displayLoading(true);
            requestFromCache();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
        param.put("filter", filterString);
        param.put("nav", Nav);
        return param;
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                OnRefreshReputationProduct();
            }
        };
    }

    private void OnRefreshReputationProduct() {
        if (!isRequest && !isSnackbarVisible(snackbarRetry)) {
            doRefresh();
        } else {
            refresh.finishRefresh();
        }
    }

    private void doRefresh() {
        if (!refresh.isRefreshing()) {
            refresh.setRefreshing(true);
        }
        isRequest = true;
        removeLoadingFooter();
        firstRequest();
    }

    @Override
    public void onConnectionResponse(List<InboxTalk> list, int page,
                                     int isUnread) {
        floatingActionButton.show();
        isRequest = false;
        if (page == 1) {
            refresh.finishRefresh();
            items.clear();
        }
        items.addAll(list);
        if (!(getActivity() instanceof InboxTalkActivity))
            adapter.setEnableAction(false);
        else
            adapter.setEnableAction(true);
        adapter.notifyDataSetChanged();
        displayView(true);
        displayLoading(false);
    }

    @Override
    public void onTimeoutResponse(int page) {
        onTimeoutResponse("", page);
    }

    @Override
    public void onTimeoutResponse(String error, int page) {
        floatingActionButton.hide();
        isRequest = false;
        if (page == 1) {
            refresh.finishRefresh();
            if (items.size() > 0) {
                removeLoadingFooter();
                adapter.notifyDataSetChanged();
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                displayView(false);
                if (error.length() <= 0) {
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryListener());
                } else {
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, retryListener());
                }
            }
        } else {
            removeLoadingFooter();
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), retrySnackbarListener());
            adapter.setEnableAction(false);
            adapter.notifyDataSetChanged();
            snackbarRetry.showRetrySnackbar();
        }
    }

    private NetworkErrorHelper.RetryClickedListener refreshSnackbarListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (!isRequest) {
                    doRefresh();
                } else {
                    refresh.finishRefresh();
                }
            }
        };
    }

    private NetworkErrorHelper.RetryClickedListener retrySnackbarListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                request();
                setLoadingFooter();
                adapter.notifyDataSetChanged();
            }
        };
    }

    private NetworkErrorHelper.RetryClickedListener retryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                displayLoading(true);
                firstRequest();
            }
        };
    }

    @Override
    public void onStateResponse(List<RecyclerViewItem> list, int position, int page, boolean hasNext, String filterString) {
        floatingActionButton.show();
        isRequest = false;
//        if (pagingHandler.getPage() == 1) {
//            refresh.finishRefresh();
//            items.clear();
//        }
        items.addAll(list);
        adapter.notifyDataSetChanged();
        dialog.setSelection(filterString);
        displayLoading(false);
    }

    @Override
    public void onCacheResponse(List<InboxTalk> list, int isUnread) {
        floatingActionButton.show();
        isRequest = false;
        items.addAll(list);
        adapter.notifyDataSetChanged();
        displayLoading(false);
    }

    @Override
    public void onCacheNoResult() {
        firstRequest();
    }

    private void request() {
        floatingActionButton.hide();
        isRequest = true;
        presenter.getInboxTalk(getActivity(), getParam());
    }


    private void firstRequest() {
        floatingActionButton.hide();
        isRequest = true;
        presenter.refreshInboxTalk(getActivity(), getParam());
    }

    @Override
    public void cancelRequest() {
        floatingActionButton.show();
        isRequest = false;
    }

    @Override
    public void setMenuListEnabled(boolean isEnabled) {
        adapter.setEnableAction(isEnabled);
    }


    private void requestFromCache() {
        presenter.getInboxTalkFromCache(getParam());
    }


    @Override
    public void showError(String s) {
        SnackbarManager.make(getActivity(), s, Snackbar.LENGTH_SHORT).show();
    }

    public void displayLoading(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
            coordinatorLayout.setVisibility(View.GONE);
//            filterLayout.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
//            filterLayout.setVisibility(View.VISIBLE);
        }
    }

    public void displayView(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
//            filterLayout.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
//            filterLayout.setVisibility(View.GONE);
        }
    }

    private boolean isLastPosition() {
        int lastIndex = layoutManager.findLastCompletelyVisibleItemPosition();
        int size = layoutManager.getItemCount() - 1;
        return lastIndex == size;
    }

    public void setLoadingFooter() {
        adapter.setIsLoading(true);
    }

    public void removeLoadingFooter() {
        adapter.setIsLoading(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GO_TO_DETAIL:
                if (data == null) {
                    return;
                }

                if (resultCode == RESULT_DELETE) {
                    int position = data.getExtras().getInt("position");
                    items.remove(position);
                    adapter.notifyDataSetChanged();
                    SnackbarManager.make(getActivity(),
                            getString(R.string.message_success_delete_talk), Snackbar.LENGTH_LONG).show();
                } else if (resultCode == Activity.RESULT_OK) {
                    int position = data.getExtras().getInt("position");
                    int size = data.getExtras().getInt("total_comment");
                    int followStatus = data.getExtras().getInt("is_follow");
                    int readStatus = data.getExtras().getInt("read_status");
                    ((InboxTalk) items.get(position)).setTalkTotalComment(String.valueOf(size));
                    ((InboxTalk) items.get(position)).setTalkFollowStatus(followStatus);
                    ((InboxTalk) items.get(position)).setTalkReadStatus(readStatus);
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                doRefresh();
                break;
        }

    }


}
