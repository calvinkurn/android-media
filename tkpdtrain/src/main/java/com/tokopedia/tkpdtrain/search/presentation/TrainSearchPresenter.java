package com.tokopedia.tkpdtrain.search.presentation;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.search.domain.GetAvailabilityScheduleUseCase;
import com.tokopedia.tkpdtrain.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.tkpdtrain.search.domain.GetScheduleUseCase;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainSchedule;

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
        getScheduleUseCase.execute(getScheduleUseCase.createRequest("2018-01-01",
                1, 0, "BD", "Bandung", "PSE", "Jakarta"),
                new Subscriber<List<AvailabilityKeySchedule>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "onError: " + e.getMessage() );
                    }

                    @Override
                    public void onNext(List<AvailabilityKeySchedule> availabilityKeySchedules) {
                        for (AvailabilityKeySchedule available: availabilityKeySchedules) {
                            getAvailabilitySchedule(available.getIdTrain());
                        }
                    }
                });
    }

    private void getAvailabilitySchedule(String idTrain) {
        getAvailabilityScheduleUseCase.setIdTrain(idTrain);
        getAvailabilityScheduleUseCase.execute(new Subscriber<List<TrainSchedule>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "onError: " + e.getMessage() );
            }

            @Override
            public void onNext(List<TrainSchedule> trainSchedules) {
                Log.d(TAG, "onNext size: " + trainSchedules.size());
            }
        });
    }
}
