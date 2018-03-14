package com.tokopedia.tkpdtrain.search.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainSchedule;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/13/18.
 */

public interface TrainSearchContract {

    interface View extends CustomerView {
        void showSearchResult(List<TrainSchedule> schedules);

        RequestParams getRequestParam();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getTrainSchedules();
    }
}
