package com.tokopedia.ride.history.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
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

import butterknife.BindView;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

public class RideHistoryFragment extends BaseFragment implements ItemClickListener, RideHistoryContract.View {

    @BindView(R2.id.rv_list_rides)
    RecyclerView listRidesRecyclerView;

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
}
