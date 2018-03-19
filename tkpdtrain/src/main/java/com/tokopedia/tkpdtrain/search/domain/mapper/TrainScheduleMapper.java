package com.tokopedia.tkpdtrain.search.domain.mapper;

import com.tokopedia.tkpdtrain.search.data.databasetable.TrainScheduleDbTable;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainScheduleMapper implements Func1<List<TrainScheduleDbTable>, List<TrainScheduleViewModel>> {

    public TrainScheduleMapper() {
    }

    @Override
    public List<TrainScheduleViewModel> call(List<TrainScheduleDbTable> trainScheduleDbTables) {
        return transform(trainScheduleDbTables);
    }

    public List<TrainScheduleViewModel> transform(List<TrainScheduleDbTable> trainScheduleDbTables) {
        List<TrainScheduleViewModel> trainScheduleViewModelList = new ArrayList<>();
        TrainScheduleViewModel trainScheduleViewModel = null;
        if (trainScheduleDbTables != null) {
            for (TrainScheduleDbTable trainScheduleDbTable : trainScheduleDbTables) {
                if (trainScheduleDbTable != null) {
                    trainScheduleViewModel = transform(trainScheduleDbTable);
                    if (trainScheduleViewModel != null) {
                        trainScheduleViewModelList.add(trainScheduleViewModel);
                    }
                }
            }
        }
        return trainScheduleViewModelList;
    }

    public TrainScheduleViewModel transform(TrainScheduleDbTable trainScheduleDbTable) {
        TrainScheduleViewModel trainScheduleViewModel = null;
        if (trainScheduleDbTable != null) {
            trainScheduleViewModel = new TrainScheduleViewModel();
            trainScheduleViewModel.setIdSchedule(trainScheduleDbTable.getIdSchedule());
            trainScheduleViewModel.setAdultFare(trainScheduleDbTable.getAdultFare());
            trainScheduleViewModel.setDisplayAdultFare(trainScheduleDbTable.getDisplayAdultFare());
            trainScheduleViewModel.setInfantFare(trainScheduleDbTable.getInfantFare());
            trainScheduleViewModel.setDisplayInfantFare(trainScheduleDbTable.getDisplayInfantFare());
            trainScheduleViewModel.setArrivalTimestamp(trainScheduleDbTable.getArrivalTimestamp());
            trainScheduleViewModel.setDepartureTimestamp(trainScheduleDbTable.getDepartureTimestamp());
            trainScheduleViewModel.setClassTrain(trainScheduleDbTable.getClassTrain());
            trainScheduleViewModel.setDisplayClass(trainScheduleDbTable.getDisplayClass());
            trainScheduleViewModel.setSubclass(trainScheduleDbTable.getSubclass());
            trainScheduleViewModel.setOrigin(trainScheduleDbTable.getOrigin());
            trainScheduleViewModel.setDestination(trainScheduleDbTable.getDestination());
            trainScheduleViewModel.setDisplayDuration(trainScheduleDbTable.getDisplayDuration());
            trainScheduleViewModel.setDuration(trainScheduleDbTable.getDuration());
            trainScheduleViewModel.setTrainKey(trainScheduleDbTable.getTrainKey());
            trainScheduleViewModel.setTrainName(trainScheduleDbTable.getTrainName());
            trainScheduleViewModel.setTrainNumber(trainScheduleDbTable.getTrainNumber());
            trainScheduleViewModel.setAvailableSeat(trainScheduleDbTable.getAvailableSeat());
            trainScheduleViewModel.setCheapestFlag(trainScheduleDbTable.isCheapestFlag());
            trainScheduleViewModel.setFastestFlag(trainScheduleDbTable.isFastestFlag());
        }
        return trainScheduleViewModel;
    }
}
