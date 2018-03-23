package com.tokopedia.tkpdtrain.search.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.search.domain.FilterParam;
import com.tokopedia.tkpdtrain.search.domain.FilterSearchData;
import com.tokopedia.tkpdtrain.search.domain.GetFilterSearchParamDataUseCase;
import com.tokopedia.tkpdtrain.search.domain.GetTotalScheduleUseCase;
import com.tokopedia.tkpdtrain.search.presentation.contract.TrainFilterSearchContract;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 3/20/18.
 */

public class TrainFilterSearchPresenter extends BaseDaggerPresenter<TrainFilterSearchContract.View>
        implements TrainFilterSearchContract.Presenter {

    private GetTotalScheduleUseCase getTotalScheduleUseCase;
    private GetFilterSearchParamDataUseCase getFilterSearchParamDataUseCase;

    @Inject
    public TrainFilterSearchPresenter(GetTotalScheduleUseCase getTotalScheduleUseCase, GetFilterSearchParamDataUseCase getFilterSearchParamDataUseCase) {
        this.getTotalScheduleUseCase = getTotalScheduleUseCase;
        this.getFilterSearchParamDataUseCase = getFilterSearchParamDataUseCase;
    }

    @Override
    public void getCountScheduleAvailable(FilterSearchData filterSearchData) {
        getTotalScheduleUseCase.execute(getTotalScheduleUseCase.createRequestParam(filterSearchData),
                new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                getView().showCountSchedule(integer);
            }
        });
    }

    @Override
    public void getFilterSearchData(Map<String, Object> mapParam, int scheduleVariant) {
        getView().showLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(mapParam);
        getFilterSearchParamDataUseCase.setScheduleVariant(scheduleVariant);
        getFilterSearchParamDataUseCase.execute(requestParams, new Subscriber<FilterSearchData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                e.printStackTrace();
            }

            @Override
            public void onNext(FilterSearchData filterSearchData) {
                getView().hideLoading();
                getView().renderFilterSearchData(filterSearchData);
            }
        });
    }
}
