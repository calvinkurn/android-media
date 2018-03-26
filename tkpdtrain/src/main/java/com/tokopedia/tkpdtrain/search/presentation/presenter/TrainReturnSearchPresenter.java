package com.tokopedia.tkpdtrain.search.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.tkpdtrain.search.presentation.contract.TrainSearchReturnContract;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainReturnSearchPresenter extends BaseDaggerPresenter<TrainSearchReturnContract.View>
        implements TrainSearchReturnContract.Presenter {

    private GetDetailScheduleUseCase getDetailScheduleUseCase;

    @Inject
    public TrainReturnSearchPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase) {
        this.getDetailScheduleUseCase = getDetailScheduleUseCase;
    }

    @Override
    public void getDetailSchedules(String idSchedule) {
        getDetailScheduleUseCase.setIdSchedule(idSchedule);
        getDetailScheduleUseCase.execute(RequestParams.EMPTY, new Subscriber<TrainScheduleViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideDetailDepartureSchedule(e);
            }

            @Override
            public void onNext(TrainScheduleViewModel viewModel) {
                if (viewModel != null) {
                    getView().loadDetailSchedule(viewModel);
                }
            }
        });
    }
}
