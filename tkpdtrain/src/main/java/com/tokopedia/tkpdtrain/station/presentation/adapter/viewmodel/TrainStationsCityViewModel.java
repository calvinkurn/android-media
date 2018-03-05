package com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationTypeFactory;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainStationsCityViewModel implements Visitable<TrainStationTypeFactory> {
    @Override
    public int type(TrainStationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
