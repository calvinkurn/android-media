package com.tokopedia.tkpdtrain.station.presentation.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.station.domain.GetPopularStationsUseCase;
import com.tokopedia.tkpdtrain.station.domain.GetStationsByKeywordUseCase;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.mapper.TrainStationViewModelMapper;
import com.tokopedia.tkpdtrain.station.presentation.contract.TrainStationsContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationsPresenter extends BaseDaggerPresenter<TrainStationsContract.View> implements TrainStationsContract.Presenter {

    private GetStationsByKeywordUseCase getStationsByKeywordUseCase;
    private GetPopularStationsUseCase getPopularStationsUseCase;
    private TrainStationViewModelMapper trainStationViewModelMapper;

    @Inject
    public TrainStationsPresenter(GetStationsByKeywordUseCase getStationsByKeywordUseCase,
                                  GetPopularStationsUseCase getPopularStationsUseCase,
                                  TrainStationViewModelMapper trainStationViewModelMapper) {
        this.getStationsByKeywordUseCase = getStationsByKeywordUseCase;
        this.getPopularStationsUseCase = getPopularStationsUseCase;
        this.trainStationViewModelMapper = trainStationViewModelMapper;
    }

    @Override
    public void actionOnInitialLoad() {
        getPopularStationsUseCase.execute(getPopularStationsUseCase.createRequest(), new Subscriber<List<TrainStation>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<TrainStation> trainStations) {
                TrainPopularStationViewModel viewModel = new TrainPopularStationViewModel();
                viewModel.setStations(trainStationViewModelMapper.transform(trainStations));
                getView().renderStationList(viewModel);
            }
        });
    }

    @Override
    public void onKeywordChange(String keyword) {
        if (keyword.length() > 2) {
            getStationsByKeywordUseCase.execute(getStationsByKeywordUseCase.createRequest(keyword), new Subscriber<List<TrainStation>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(List<TrainStation> stations) {
                    List<Visitable> visitables = new ArrayList<>();
                    List<TrainStationViewModel> viewModels = trainStationViewModelMapper.transform(stations);
                    visitables.addAll(viewModels);
                    getView().renderStationList(visitables);
                }
            });
        } else if (keyword.length() == 0) {
            actionOnInitialLoad();
        }
    }
}
