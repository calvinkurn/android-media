package com.tokopedia.tkpdtrain.station.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.station.domain.GetPopularStationsUseCase;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.mapper.TrainStationViewModelMapper;
import com.tokopedia.tkpdtrain.station.presentation.contract.TrainStationsContract;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationsPresenter extends BaseDaggerPresenter<TrainStationsContract.View> implements TrainStationsContract.Presenter {

    private GetPopularStationsUseCase getPopularStationsUseCase;
    private TrainStationViewModelMapper trainStationViewModelMapper;

    @Inject
    public TrainStationsPresenter(GetPopularStationsUseCase getPopularStationsUseCase,
                                  TrainStationViewModelMapper trainStationViewModelMapper) {
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
                getView().renderList(viewModel);
            }
        });
    }
}
