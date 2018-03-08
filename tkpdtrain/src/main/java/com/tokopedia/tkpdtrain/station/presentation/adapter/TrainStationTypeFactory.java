package com.tokopedia.tkpdtrain.station.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationsCityViewModel;

/**
 * Created by alvarisi on 3/5/18.
 */

public interface TrainStationTypeFactory extends AdapterTypeFactory{
    int type(TrainStationViewModel trainStationViewModel);

    int type(TrainStationsCityViewModel trainStationsCityViewModel);

    AbstractViewHolder createViewHolder(View parent, int type);

    int type(TrainPopularStationViewModel trainPopularStationViewModel);
}
