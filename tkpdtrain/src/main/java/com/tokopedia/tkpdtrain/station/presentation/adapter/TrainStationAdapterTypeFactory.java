package com.tokopedia.tkpdtrain.station.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.TrainPopularStationViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.TrainStationGroupViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.TrainStationViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.TrainStationsCityGroupViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewholder.listener.TrainStationActionListener;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationGroupViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationsCityGroupViewModel;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationAdapterTypeFactory extends BaseAdapterTypeFactory implements TrainStationTypeFactory {
    private TrainStationActionListener trainStationActionListener;

    public TrainStationAdapterTypeFactory(TrainStationActionListener trainStationActionListener) {
        this.trainStationActionListener = trainStationActionListener;
    }

    @Override
    public int type(TrainStationViewModel trainStationViewModel) {
        return TrainStationViewHolder.LAYOUT;
    }

    @Override
    public int type(TrainStationsCityGroupViewModel trainStationsCityGroupViewModel) {
        return TrainStationsCityGroupViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TrainStationViewHolder.LAYOUT) {
            return new TrainStationViewHolder(parent, trainStationActionListener);
        } else if (type == TrainStationsCityGroupViewHolder.LAYOUT) {
            return new TrainStationsCityGroupViewHolder(parent, trainStationActionListener);
        } else if (type == TrainPopularStationViewHolder.LAYOUT) {
            return new TrainPopularStationViewHolder(parent, trainStationActionListener);
        } else if (type == TrainStationGroupViewHolder.LAYOUT) {
            return new TrainStationGroupViewHolder(parent, trainStationActionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(TrainPopularStationViewModel trainPopularStationViewModel) {
        return TrainPopularStationViewHolder.LAYOUT;
    }

    @Override
    public int type(TrainStationGroupViewModel trainStationGroupViewModel) {
        return TrainStationGroupViewHolder.LAYOUT;
    }
}
