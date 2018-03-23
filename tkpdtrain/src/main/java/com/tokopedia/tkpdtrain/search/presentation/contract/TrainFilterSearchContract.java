package com.tokopedia.tkpdtrain.search.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdtrain.search.domain.FilterParam;
import com.tokopedia.tkpdtrain.search.domain.FilterSearchData;

import java.util.Map;

/**
 * Created by nabillasabbaha on 3/20/18.
 */

public interface TrainFilterSearchContract {

    interface View extends CustomerView {
        void showCountSchedule(int totalSchedule);

        void renderFilterSearchData(FilterSearchData filterSearchData);

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends CustomerPresenter<View> {
        void getCountScheduleAvailable(FilterSearchData filterSearchData);

        void getFilterSearchData(Map<String, Object> mapParam, int scheduleVariant);
    }
}
