package com.tokopedia.tkpdtrain.search.presentation;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.search.domain.AvailabilityKeySchedule;
import com.tokopedia.tkpdtrain.search.domain.GetScheduleUseCase;

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

    @Inject
    public TrainSearchPresenter(GetScheduleUseCase getScheduleUseCase) {
        this.getScheduleUseCase = getScheduleUseCase;
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
                            Log.d(TAG, "onNext: " + available.getIdTrain());
                        }
                    }
                });
    }
}
