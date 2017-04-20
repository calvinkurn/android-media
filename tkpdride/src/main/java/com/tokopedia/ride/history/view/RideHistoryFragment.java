package com.tokopedia.ride.history.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.history.di.RideHistoryDependencyInjection;
import com.tokopedia.ride.history.domain.GetRideHistoriesUseCase;
import com.tokopedia.ride.history.view.adapter.ItemClickListener;
import com.tokopedia.ride.history.view.adapter.RideHistoryAdapter;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryAdapterTypeFactory;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryTypeFactory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;
import com.tokopedia.ride.ontrip.domain.GetCurrentDetailRideRequestUseCase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

public class RideHistoryFragment extends BaseFragment implements ItemClickListener, RideHistoryContract.View {

    @BindView(R2.id.container)
    FrameLayout containerFrameLayout;
    @BindView(R2.id.rv_list_rides)
    RecyclerView listRidesRecyclerView;
    @BindView(R2.id.sw_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    RideHistoryAdapter adapter;

    RideHistoryContract.Presenter mPresenter;

    public static Fragment newInstance() {
        return new RideHistoryFragment();
    }

    public RideHistoryFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ride_history;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewListener();
        mPresenter = RideHistoryDependencyInjection.createPresenter(getActivity());
        mPresenter.attachView(this);
        mPresenter.initialize();
    }

    private void setViewListener() {
        RideHistoryTypeFactory rideHistoryTypeFactory = new RideHistoryAdapterTypeFactory(this);
        adapter = new RideHistoryAdapter(rideHistoryTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listRidesRecyclerView.setLayoutManager(layoutManager);
        listRidesRecyclerView.setHasFixedSize(true);
        listRidesRecyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(getRefreshListener());
    }

    @NonNull
    private SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.actionRefreshHistoriesData();
            }
        };
    }

    @Override
    public void onHistoryClicked(RideHistoryViewModel viewModel) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public RequestParams getHistoriesParam() {
        RequestParams requestParams = RequestParams.create();
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_HASH, hash);
        requestParams.putString(GetRideHistoriesUseCase.PARAM_OS_TYPE, "1");
        return requestParams;
    }

    @Override
    public void showFailedGetHistoriesMessage() {

    }

    @Override
    public void renderHistoryLists(ArrayList<Visitable> histories) {
        adapter.clearData();
        adapter.setElement(histories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderUpdatedHistoryRow(int position, Visitable history) {
        adapter.setChangedItem(position, history);
    }

    @Override
    public void enableRefreshLayout() {
        refreshLayout.setEnabled(true);
    }

    @Override
    public void setRefreshLayoutToFalse() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void disableRefreshLayout() {
        refreshLayout.setEnabled(false);
    }

    @Override
    public void showEmptyResultLayout() {
        adapter.clearData();
        ArrayList<Visitable> emptyModels = new ArrayList<>();
        emptyModels.add(new EmptyModel());
        adapter.setElement(emptyModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showRetryLayout() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), getRetryGetHistoriesListener());
    }

    @NonNull
    private NetworkErrorHelper.RetryClickedListener getRetryGetHistoriesListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                mPresenter.actionRefreshHistoriesData();
            }
        };
    }
}
