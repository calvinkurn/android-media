package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainScheduleMapper implements Func1<List<TrainScheduleDbTable>, List<TrainSchedule>> {

    public TrainScheduleMapper() {
    }

    @Override
    public List<TrainSchedule> call(List<TrainScheduleDbTable> trainScheduleDbTables) {
        return transform(trainScheduleDbTables);
    }

    public List<TrainSchedule> transform(List<TrainScheduleDbTable> trainScheduleDbTables) {
        List<TrainSchedule> trainScheduleList = new ArrayList<>();
        TrainSchedule trainSchedule = null;
        if (trainScheduleDbTables != null) {
            for (TrainScheduleDbTable trainScheduleDbTable : trainScheduleDbTables) {
                if (trainScheduleDbTable != null) {
                    trainSchedule = transform(trainScheduleDbTable);
                    if (trainSchedule != null) {
                        trainScheduleList.add(trainSchedule);
                    }
                }
            }
        }
        return trainScheduleList;
    }

    public TrainSchedule transform(TrainScheduleDbTable trainScheduleDbTable) {
        TrainSchedule trainSchedule = null;
        if (trainScheduleDbTable != null) {
            trainSchedule = new TrainSchedule();
            trainSchedule.setIdSchedule(trainScheduleDbTable.getIdSchedule());
            trainSchedule.setAdultFare(trainScheduleDbTable.getAdultFare());
            trainSchedule.setDisplayAdultFare(trainScheduleDbTable.getDisplayAdultFare());
            trainSchedule.setInfantFare(trainScheduleDbTable.getInfantFare());
            trainSchedule.setDisplayInfantFare(trainScheduleDbTable.getDisplayInfantFare());
            trainSchedule.setArrivalTimestamp(trainScheduleDbTable.getArrivalTimestamp());
            trainSchedule.setDepartureTimestamp(trainScheduleDbTable.getDepartureTimestamp());
            trainSchedule.setClassTrain(trainScheduleDbTable.getClassTrain());
            trainSchedule.setDisplayClass(trainScheduleDbTable.getDisplayClass());
            trainSchedule.setSubclass(trainScheduleDbTable.getSubclass());
            trainSchedule.setOrigin(trainScheduleDbTable.getOrigin());
            trainSchedule.setDestination(trainScheduleDbTable.getDestination());
            trainSchedule.setDisplayDuration(trainScheduleDbTable.getDisplayDuration());
            trainSchedule.setDuration(trainScheduleDbTable.getDuration());
            trainSchedule.setTrainKey(trainScheduleDbTable.getTrainKey());
            trainSchedule.setTrainName(trainScheduleDbTable.getTrainName());
            trainSchedule.setTrainNumber(trainScheduleDbTable.getTrainNumber());
        }
        return trainSchedule;
    }
}
