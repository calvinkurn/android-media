package com.tokopedia.tkpdtrain.search.presentation.contract;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/13/18.
 */

public interface TrainSearchReturnContract {

    interface View extends TrainSearchContract.View {
        void loadDetailSchedule(TrainScheduleViewModel viewModel);

        void hideDetailDepartureSchedule(Throwable e);

    }

    interface Presenter extends CustomerPresenter<View> {
        void getDetailSchedules(String idSchedule);
    }
}
