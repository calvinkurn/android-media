package com.tokopedia.tkpdtrain.station.domain.model.mapper;

import com.tokopedia.tkpdtrain.station.data.databasetable.TrainStationDb;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author by alvarisi on 3/8/18.
 */

public class TrainStationDbMapper implements Func1<List<TrainStationDb>, List<TrainStation>> {
    public TrainStationDbMapper() {
    }

    @Override
    public List<TrainStation> call(List<TrainStationDb> trainStationDbs) {
        return transform(trainStationDbs);
    }

    public List<TrainStation> transform(List<TrainStationDb> entities) {
        List<TrainStation> stations = new ArrayList<>();
        TrainStation station = null;
        if (entities != null) {
            for (TrainStationDb entity : entities) {
                station = transform(entity);
                if (station != null) {
                    stations.add(station);
                }
            }
        }
        return stations;
    }

    public TrainStation transform(TrainStationDb entity) {
        TrainStation station = null;
        if (entity != null) {
            station = new TrainStation();
            station.setCityId(entity.getCityId());
            station.setCityName(entity.getCityName());
            station.setIslandId(entity.getIslandId());
            station.setIslandName(entity.getIslandName());
            station.setStationCode(entity.getStationCode());
            station.setStationId(entity.getStationId());
            station.setStationCode(entity.getStationCode());
            station.setStationName(entity.getStationName());
        }
        return station;
    }
}
