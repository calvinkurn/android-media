package com.tokopedia.tkpdtrain.station.presentation.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.station.domain.TrainGetPopularStationsUseCase;
import com.tokopedia.tkpdtrain.station.domain.TrainGetStationCitiesByKeywordUseCase;
import com.tokopedia.tkpdtrain.station.domain.TrainGetStationsByKeywordUseCase;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationGroupViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationsCityGroupViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.mapper.TrainStationViewModelMapper;
import com.tokopedia.tkpdtrain.station.presentation.contract.TrainStationsContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationsPresenter extends BaseDaggerPresenter<TrainStationsContract.View> implements TrainStationsContract.Presenter {

    private TrainGetStationsByKeywordUseCase trainGetStationsByKeywordUseCase;
    private TrainGetPopularStationsUseCase trainGetPopularStationsUseCase;
    private TrainGetStationCitiesByKeywordUseCase trainGetStationCitiesByKeywordUseCase;
    private TrainStationViewModelMapper trainStationViewModelMapper;

    @Inject
    public TrainStationsPresenter(TrainGetStationsByKeywordUseCase trainGetStationsByKeywordUseCase,
                                  TrainGetPopularStationsUseCase trainGetPopularStationsUseCase,
                                  TrainGetStationCitiesByKeywordUseCase trainGetStationCitiesByKeywordUseCase,
                                  TrainStationViewModelMapper trainStationViewModelMapper) {
        this.trainGetStationsByKeywordUseCase = trainGetStationsByKeywordUseCase;
        this.trainGetPopularStationsUseCase = trainGetPopularStationsUseCase;
        this.trainGetStationCitiesByKeywordUseCase = trainGetStationCitiesByKeywordUseCase;
        this.trainStationViewModelMapper = trainStationViewModelMapper;
    }

    @Override
    public void actionOnInitialLoad() {
        trainGetPopularStationsUseCase.execute(trainGetPopularStationsUseCase.createRequest(), new Subscriber<List<TrainStation>>() {
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
        getView().clearStationList();
        getView().showLoading();
        if (keyword.length() > 2) {
            trainGetStationsByKeywordUseCase.createObservable(trainGetStationsByKeywordUseCase.createRequest(keyword))
                    .onErrorReturn(new Func1<Throwable, List<TrainStation>>() {
                        @Override
                        public List<TrainStation> call(Throwable throwable) {
                            return new ArrayList<>();
                        }
                    })
                    .zipWith(
                            trainGetStationCitiesByKeywordUseCase.createObservable(trainGetStationCitiesByKeywordUseCase.createRequest(keyword))
                                    .onErrorReturn(new Func1<Throwable, List<TrainStation>>() {
                                        @Override
                                        public List<TrainStation> call(Throwable throwable) {
                                            return new ArrayList<>();
                                        }
                                    }),
                            new Func2<List<TrainStation>, List<TrainStation>, List<Visitable>>() {
                                @Override
                                public List<Visitable> call(List<TrainStation> stations, List<TrainStation> stationCities) {
                                    List<Visitable> visitables = new ArrayList<>();

                                    if (stationCities.size() > 0) {
                                        List<TrainStationViewModel> viewModels = trainStationViewModelMapper.transform(stationCities);
                                        TrainStationsCityGroupViewModel cityGroupViewModel = new TrainStationsCityGroupViewModel();
                                        cityGroupViewModel.setCities(viewModels);
                                        visitables.add(cityGroupViewModel);
                                    }
                                    if (stations.size() > 0) {
                                        List<TrainStationViewModel> viewModels = trainStationViewModelMapper.transform(stations);
                                        TrainStationGroupViewModel stationGroupViewModel = new TrainStationGroupViewModel();
                                        stationGroupViewModel.setStations(viewModels);
                                        visitables.add(stationGroupViewModel);
                                    }

                                    return visitables;
                                }
                            }
                    ).subscribe(new Subscriber<List<Visitable>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<Visitable> visitables) {
                    getView().hideLoading();
                    getView().renderStationList(visitables);
                }
            });
        } else if (keyword.length() == 0) {
            actionOnInitialLoad();
        }
    }
}
