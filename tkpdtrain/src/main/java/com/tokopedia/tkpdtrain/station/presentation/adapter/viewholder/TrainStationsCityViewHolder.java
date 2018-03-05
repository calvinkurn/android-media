package com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationsCityViewModel;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainStationsCityViewHolder extends AbstractViewHolder<TrainStationsCityViewModel> {
    public static final int LAYOUT = R.layout.view_train_station_city_item;

    public TrainStationsCityViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(TrainStationsCityViewModel element) {

    }
}
