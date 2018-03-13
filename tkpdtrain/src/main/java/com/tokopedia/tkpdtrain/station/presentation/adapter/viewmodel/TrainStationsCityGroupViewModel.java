package com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationTypeFactory;

import java.util.List;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationsCityGroupViewModel implements Visitable<TrainStationTypeFactory> {
    private List<TrainStationViewModel> cities;

    @Override
    public int type(TrainStationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public List<TrainStationViewModel> getCities() {
        return cities;
    }

    public void setCities(List<TrainStationViewModel> cities) {
        this.cities = cities;
    }
}
