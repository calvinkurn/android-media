package com.tokopedia.tkpdtrain.station.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.TrainStationViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.TrainStationsCityViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationsCityViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationAdapterTypeFactory extends BaseAdapterTypeFactory implements TrainStationTypeFactory {

    public TrainStationAdapterTypeFactory() {
    }

    @Override
    public int type(TrainStationViewModel trainStationViewModel) {
        return TrainStationViewHolder.LAYOUT;
    }

    @Override
    public int type(TrainStationsCityViewModel trainStationsCityViewModel) {
        return TrainStationsCityViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TrainStationViewHolder.LAYOUT) {
            return new TrainStationViewHolder(parent);
        } else if (type == TrainStationsCityViewHolder.LAYOUT) {
            return new TrainStationsCityViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
