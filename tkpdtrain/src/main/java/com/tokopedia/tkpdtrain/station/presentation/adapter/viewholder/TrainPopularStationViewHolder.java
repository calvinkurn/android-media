package com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationAdapterTypeFactory;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationTypeFactory;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationsCityViewModel;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainPopularStationViewHolder extends AbstractViewHolder<TrainPopularStationViewModel> {
    public static final int LAYOUT = R.layout.view_train_popular_station_item;

    private RecyclerView stationsRecyclerView;

    public TrainPopularStationViewHolder(View itemView) {
        super(itemView);
        stationsRecyclerView = itemView.findViewById(R.id.rv_stations);
    }

    @Override
    public void bind(TrainPopularStationViewModel element) {
        TrainStationTypeFactory typeFactory = new TrainStationAdapterTypeFactory();
        BaseListAdapter<TrainStationViewModel, TrainStationTypeFactory> adapter =  new BaseListAdapter<>(typeFactory);
        LinearLayoutManager trainStationLayoutManager
                = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        stationsRecyclerView.setLayoutManager(trainStationLayoutManager);
        stationsRecyclerView.setHasFixedSize(true);
        stationsRecyclerView.setNestedScrollingEnabled(false);
        stationsRecyclerView.setAdapter(adapter);
        adapter.addElement(element.getStations());
        adapter.notifyDataSetChanged();
    }
}
