package com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.listener.TrainStationActionListener;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationViewHolder extends AbstractViewHolder<TrainStationViewModel> {
    public static final int LAYOUT = R.layout.view_train_station_item;
    private AppCompatTextView stationNameTextView;
    private AppCompatTextView stationCityTextView;
    private final TrainStationActionListener trainStationActionListener;
    private TrainStationViewModel element;

    public TrainStationViewHolder(View itemView, TrainStationActionListener trainStationActionListener) {
        super(itemView);
        stationNameTextView = itemView.findViewById(R.id.tv_station_name);
        stationCityTextView = itemView.findViewById(R.id.tv_station_city);
        this.trainStationActionListener = trainStationActionListener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainStationViewHolder.this.trainStationActionListener.onStationClicked(element);
            }
        });
    }

    @Override
    public void bind(TrainStationViewModel element) {
        this.element = element;
        stationNameTextView.setText(String.format("%s (%s)", element.getStationName(), element.getStationCode()));
        stationCityTextView.setText(element.getCityName());
    }
}
