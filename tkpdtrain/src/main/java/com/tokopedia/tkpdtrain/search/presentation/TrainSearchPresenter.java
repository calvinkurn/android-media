package com.tokopedia.tkpdtrain.search.presentation;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.search.domain.GetAvailabilityScheduleUseCase;
import com.tokopedia.tkpdtrain.search.domain.GetScheduleUseCase;
import com.tokopedia.tkpdtrain.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 3/13/18.
 */

public class TrainSearchPresenter extends BaseDaggerPresenter<TrainSearchContract.View> implements
        TrainSearchContract.Presenter {

    private static final String TAG = TrainSearchPresenter.class.getSimpleName();
    private GetScheduleUseCase getScheduleUseCase;
    private GetAvailabilityScheduleUseCase getAvailabilityScheduleUseCase;

    @Inject
    public TrainSearchPresenter(GetScheduleUseCase getScheduleUseCase, GetAvailabilityScheduleUseCase getAvailabilityScheduleUseCase) {
        this.getScheduleUseCase = getScheduleUseCase;
        this.getAvailabilityScheduleUseCase = getAvailabilityScheduleUseCase;
    }

    @Override
    public void getTrainSchedules() {
        getScheduleUseCase.execute(getView().getRequestParam(),
                new Subscriber<List<AvailabilityKeySchedule>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLayoutTripInfo();
                        getView().showGetListError(e);
                    }

                    @Override
                    public void onNext(List<AvailabilityKeySchedule> availabilityKeySchedules) {
                        for (AvailabilityKeySchedule available : availabilityKeySchedules) {
                            getAvailabilitySchedule(available.getIdTrain());
                        }
                    }
                });
    }

    private void getAvailabilitySchedule(String idTrain) {
        getAvailabilityScheduleUseCase.setIdTrain(idTrain);
        getAvailabilityScheduleUseCase.execute(new Subscriber<List<TrainScheduleViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLayoutTripInfo();
                getView().showGetListError(e);
            }

            @Override
            public void onNext(List<TrainScheduleViewModel> trainScheduleViewModels) {
                Log.d(TAG, "onNext size: " + trainScheduleViewModels.size());
                if (trainScheduleViewModels != null) {
                    getView().showLayoutTripInfo();
                    getView().renderList(trainScheduleViewModels);
                } else {

                }
            }
        });
    }
}
