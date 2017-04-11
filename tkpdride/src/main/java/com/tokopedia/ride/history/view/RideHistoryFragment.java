package com.tokopedia.ride.history.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.history.view.adapter.ItemClickListener;
import com.tokopedia.ride.history.view.adapter.RideHistoryAdapter;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryAdapterTypeFactory;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryTypeFactory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import butterknife.BindView;

public class RideHistoryFragment extends BaseFragment implements ItemClickListener {

    @BindView(R2.id.rv_list_rides)
    RecyclerView listRidesRecyclerView;

    RideHistoryAdapter adapter;

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
}
