package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/22/18.
 */

public class GetFilterSearchParamDataUseCase extends UseCase<FilterSearchData> {

    private TrainRepository trainRepository;
    private int scheduleVariant;

    public GetFilterSearchParamDataUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public Observable<FilterSearchData> createObservable(RequestParams requestParams) {
        return trainRepository.getFilterSearchParamData(requestParams.getParameters(), scheduleVariant)
                .map(new Func1<List<TrainScheduleViewModel>, FilterSearchData>() {
                    @Override
                    public FilterSearchData call(List<TrainScheduleViewModel> trainScheduleViewModels) {
                        long minPrice = trainScheduleViewModels.get(0).getAdultFare();
                        long maxPrice = minPrice;
                        List<String> trainNameList = new ArrayList<>();
                        LinkedHashSet<String> trainNameSet = new LinkedHashSet<>();
                        List<String> trainClassList = new ArrayList<>();
                        LinkedHashSet<String> trainClassSet = new LinkedHashSet<>();

                        FilterSearchData filterSearchData = new FilterSearchData();
                        for (int i = 0; i < trainScheduleViewModels.size(); i++) {
                            if (minPrice > trainScheduleViewModels.get(i).getAdultFare()) {
                                minPrice = trainScheduleViewModels.get(i).getAdultFare();
                            }
                            if (maxPrice < trainScheduleViewModels.get(i).getAdultFare()) {
                                maxPrice = trainScheduleViewModels.get(i).getAdultFare();
                            }

                            trainNameSet.add(trainScheduleViewModels.get(i).getTrainName());
                            trainClassSet.add(trainScheduleViewModels.get(i).getDisplayClass());
                        }
                        filterSearchData.setMinPrice(minPrice);
                        filterSearchData.setMaxPrice(maxPrice);
                        trainNameList.addAll(trainNameSet);
                        filterSearchData.setTrains(trainNameList);
                        trainClassList.addAll(trainClassSet);
                        filterSearchData.setTrainClass(trainClassList);
                        return filterSearchData;
                    }
                });
    }
}
