package com.tokopedia.inbox.rescenter.inboxv2.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.ResoInboxAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.di.DaggerResoInboxComponent;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.presenter.ResoInboxFragmentPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;

import javax.inject.Inject;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxFragment extends BaseDaggerFragment implements ResoInboxFragmentListener.View {

    public static final int REQUEST_DETAIL_RESO = 1234;

    private ResoInboxAdapter inboxAdapter;
    private LinearLayoutManager rvInboxLayoutManager;

    private RecyclerView rvInbox, rvQuickFilter;
    private ProgressBar progressBar;

    private boolean isSeller;
    private boolean isCanLoadMore;
    private String lastCursor = "";

    @Inject
    ResoInboxFragmentPresenter presenter;


    public static Fragment getFragmentInstance(Bundle bundle) {
        Fragment fragment = new ResoInboxFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.fragment_reso_inbox, container, false);
        rvInbox = (RecyclerView) view.findViewById(R.id.rv_inbox);
        rvQuickFilter = (RecyclerView) view.findViewById(R.id.rv_quick_filter);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        rvInboxLayoutManager = new LinearLayoutManager(getActivity());
        rvInbox.setLayoutManager(rvInboxLayoutManager);
        isSeller = getArguments().getBoolean(ResoInboxActivity.PARAM_IS_SELLER);
        rvInbox.addOnScrollListener(rvInboxScrollListener);
        initView();
    }

    public void initView() {
        presenter.initPresenterData(getActivity(), isSeller);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerResoInboxComponent daggerCreateResoComponent =
                (DaggerResoInboxComponent) DaggerResoInboxComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
    }

    private RecyclerView.OnScrollListener rvInboxScrollListener
            = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = rvInboxLayoutManager.getChildCount();
            int totalItemCount = rvInboxLayoutManager.getItemCount();
            int firstVisiblesItems = rvInboxLayoutManager.findFirstVisibleItemPosition();
            if (isCanLoadMore && (visibleItemCount + firstVisiblesItems) >= totalItemCount) {
                inboxAdapter.addLoadingItem();
                isCanLoadMore = false;
                presenter.loadMoreInbox(lastCursor);
            }
        }
    };

    @Override
    public void onSuccessGetInbox(InboxItemResultViewModel result) {
        dismissProgressBar();
        inboxAdapter = new ResoInboxAdapter(
                getActivity(),
                this,
                result.getInboxItemViewModels());
        rvInbox.setAdapter(inboxAdapter);
        inboxAdapter.notifyDataSetChanged();
        updateParams(true, result);
    }

    @Override
    public void onErrorGetInbox(String err) {
        dismissProgressBar();
        showErrorWithRetry(err);
    }

    @Override
    public void onSuccessLoadMoreInbox(InboxItemResultViewModel result) {
        inboxAdapter.removeLoadingItem();
        inboxAdapter.addMoreItem(result.getInboxItemViewModels());
        updateParams(true, result);
    }

    @Override
    public void onErrorLoadMoreInbox(String err) {
        inboxAdapter.removeLoadingItem();
        isCanLoadMore = false;
        lastCursor = "";
    }

    @Override
    public void onItemClicked(int resolutionId, String sellerName, String customerName) {
        Intent intent;
        if (isSeller) {
            intent = DetailResChatActivity.newSellerInstance(
                    getActivity(),
                    String.valueOf(resolutionId),
                    customerName);
        } else {
            intent = DetailResChatActivity.newBuyerInstance(
                    getActivity(),
                    String.valueOf(resolutionId),
                    sellerName);
        }
        startActivityForResult(intent, REQUEST_DETAIL_RESO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAIL_RESO) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    private void showErrorWithRetry(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getInbox();
            }
        });
    }

    @Override
    public void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismissProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void updateParams(boolean isCanLoadMore, InboxItemResultViewModel resultViewModel) {
        this.isCanLoadMore = isCanLoadMore;
        this.lastCursor = String.valueOf(resultViewModel.getInboxItemViewModels()
                .get(resultViewModel.getInboxItemViewModels().size() - 1).getId());
    }

    private void resetParams() {
        this.isCanLoadMore = false;
        this.lastCursor = "";
    }
}
