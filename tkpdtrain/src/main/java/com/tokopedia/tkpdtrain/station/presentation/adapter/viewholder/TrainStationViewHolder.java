package com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationViewHolder extends AbstractViewHolder<TrainStationViewModel> {
    public static final int LAYOUT = R.layout.view_train_station_item;
    private AppCompatTextView stationNameTextView;
    private AppCompatTextView stationCityTextView;

    public TrainStationViewHolder(View itemView) {
        super(itemView);
        stationNameTextView = itemView.findViewById(R.id.tv_station_name);
        stationCityTextView = itemView.findViewById(R.id.tv_station_city);
    }

    @Override
    public void bind(TrainStationViewModel element) {
        stationNameTextView.setText(element.getStationName());
        stationCityTextView.setText(element.getCityName());
    }
}
