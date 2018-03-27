package com.tokopedia.tkpdstream.channel.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.data.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.channel.di.DaggerChannelComponent;
import com.tokopedia.tkpdstream.channel.view.activity.ChannelActivity;
import com.tokopedia.tkpdstream.channel.view.adapter.typefactory.ChannelTypeFactory;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.channel.view.presenter.ChannelPresenter;
import com.tokopedia.tkpdstream.common.design.SpaceItemDecoration;
import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;

import javax.inject.Inject;

/**
 * @author by nisie on 2/1/18.
 */


public class ChannelFragment extends BaseListFragment<ChannelViewModel, ChannelTypeFactory> implements ChannelContract.View {

    private static final int REQUEST_OPEN_GROUPCHAT = 111;
    private static final int REQUEST_LOGIN = 101;

    @Inject
    ChannelPresenter presenter;

    @Inject
    StreamAnalytics analytics;

    UserSession userSession;

    SwipeRefreshLayout swipeRefreshLayout;

    SpaceItemDecoration itemDecoration;

    public static Fragment createInstance(Bundle bundle) {
        return new ChannelFragment();
    }

    @Override
    protected String getScreenName() {
        return ChannelAnalytics.Screen.CHANNEL_LIST;
    }

    @Override
    protected void initInjector() {
        StreamComponent streamComponent = DaggerStreamComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()).build();

        DaggerChannelComponent.builder()
                .streamComponent(streamComponent)
                .build().inject(this);


        presenter.attachView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        analytics.eventClickInboxChat();
        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
        if (userSession != null && !userSession.isLoggedIn()) {
            startActivityForResult(((StreamModuleRouter) getActivity().getApplicationContext())
                    .getLoginIntent
                            (getActivity()), REQUEST_LOGIN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }


    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = super.getRecyclerView(view);
        recyclerView.addItemDecoration(new ItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_med)));
        return recyclerView;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void loadInitialData() {
        presenter.getChannelListFirstTime();
    }

    @Override
    public void loadData(int page) {
        presenter.getChannelList();
    }

    @Override
    protected ChannelTypeFactory getAdapterTypeFactory() {
        return new ChannelTypeFactory();
    }

    @NonNull
    @Override
    protected BaseListAdapter<ChannelViewModel, ChannelTypeFactory> createAdapterInstance() {
        BaseListAdapter<ChannelViewModel, ChannelTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onFailedGetChannelFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getChannelListFirstTime();
                    }
                });
    }

    @Override
    public void onSuccessGetChannelFirstTime(ChannelListViewModel channelListViewModel) {
        renderList(channelListViewModel.getChannelViewModelList(), channelListViewModel.isHasNextPage());
    }

    @Override
    public void onSuccessGetChannel(ChannelListViewModel channelListViewModel) {
        renderList(channelListViewModel.getChannelViewModelList(), channelListViewModel.isHasNextPage());
    }

    @Override
    public void onFailedGetChannel(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showLoadingFull() {
        getAdapter().showLoading();
    }

    @Override
    public void dismissLoadingFull() {
        getAdapter().hideLoading();
    }

    @Override
    public void onErrorRefreshChannel(String errorMessage) {
        swipeRefreshLayout.setRefreshing(false);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRefreshChannel(ChannelListViewModel channelListViewModel) {
        swipeRefreshLayout.setRefreshing(false);
        getAdapter().clearAllElements();
        renderList(channelListViewModel.getChannelViewModelList(), channelListViewModel.isHasNextPage());
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }


    @Override
    public void onItemClicked(ChannelViewModel channelViewModel) {
        goToChannel(channelViewModel);
    }

    private void goToChannel(ChannelViewModel channelViewModel) {
        analytics.eventClickGroupChatList(channelViewModel.getId());
        startActivityForResult(GroupChatActivity.getCallingIntent(getActivity(), channelViewModel),
                REQUEST_OPEN_GROUPCHAT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GROUPCHAT
                && resultCode == ChannelActivity.RESULT_ERROR_ENTER_CHANNEL
                && data != null
                && data.getExtras() != null) {
            String errorMessage = data.getExtras().getString(ChannelActivity.RESULT_MESSAGE, "");
            if (!TextUtils.isEmpty(errorMessage)) {
                NetworkErrorHelper.showRedCloseSnackbar(getActivity(), errorMessage);
            } else {
                presenter.refreshData();
            }
        } else if (requestCode == REQUEST_LOGIN
                && resultCode == Activity.RESULT_CANCELED) {
            Intent intent = ((StreamModuleRouter) getActivity().getApplicationContext())
                    .getHomeIntent(getActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        } else if (requestCode == REQUEST_LOGIN
                && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.removeEmptyState(getView());
            loadInitialData();
        }
    }

    @Override
    public void onSwipeRefresh() {
        presenter.refreshData();
    }

    public class ItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public ItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
                outRect.bottom= space / 2;
            } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = space;
                outRect.top = space / 2;
            } else {
                outRect.top = space / 2;
                outRect.bottom = space / 2;
            }
        }
    }
}
