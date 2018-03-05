package com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationViewHolder extends AbstractViewHolder<TrainStationViewModel> {
    public static final int LAYOUT = R.layout.view_train_station_item;
    public TrainStationViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(TrainStationViewModel element) {

    }
}
