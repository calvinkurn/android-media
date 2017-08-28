package com.tokopedia.ride.history.view;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.common.ride.domain.model.LocationLatLng;
import com.tokopedia.ride.common.ride.domain.model.RideHistoryWrapper;
import com.tokopedia.ride.history.domain.GetHistoriesWithPaginationUseCase;
import com.tokopedia.ride.history.domain.GetRideHistoriesUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModelMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/19/17.
 */

public class RideHistoryPresenter extends BaseDaggerPresenter<RideHistoryContract.View>
        implements RideHistoryContract.Presenter {
    private GetRideHistoriesUseCase getRideHistoriesUseCase;
    private GetHistoriesWithPaginationUseCase getHistoriesWithPaginationUseCase;

    @Inject
    public RideHistoryPresenter(GetRideHistoriesUseCase getRideHistoriesUseCase,
                                GetHistoriesWithPaginationUseCase getHistoriesWithPaginationUseCase) {
        this.getRideHistoriesUseCase = getRideHistoriesUseCase;
        this.getHistoriesWithPaginationUseCase = getHistoriesWithPaginationUseCase;
    }

    @Override
    public void initialize() {
//        actionGetHistoriesData();
        actionGetHistoriesWithPaginationData();
    }

    private void actionGetHistoriesWithPaginationData() {
        getView().disableRefreshLayout();
        getView().showMainLoading();
        getView().hideMainLayout();
        getHistoriesWithPaginationUseCase.execute(RequestParams.EMPTY, new Subscriber<RideHistoryWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideMainLoading();
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().showRetryLayout();
                }
            }

            @Override
            public void onNext(RideHistoryWrapper rideHistoryWrapper) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideMainLoading();
                    ArrayList<Visitable> histories = new ArrayList<>();
                    String mapSize = getView().getMapImageSize();

                    if(rideHistoryWrapper != null) {
                        RideHistoryViewModelMapper mapper = new RideHistoryViewModelMapper(getView().getMapKey());
                        for (RideHistory rideHistory : rideHistoryWrapper.getHistories()) {
                            RideHistoryViewModel viewModel = mapper.transform(mapSize, rideHistory);
                            histories.add(viewModel);
                        }
                        getView().enableRefreshLayout();
                        getView().setRefreshLayoutToFalse();
                        getView().setPaging(rideHistoryWrapper.getPaging());
                    }

                    if (histories.size() > 0) {
                        getView().renderHistoryLists(histories);
                    } else {
                        getView().showEmptyResultLayout();
                    }
                }
            }
        });
    }

    private void actionGetHistoriesData() {
        getView().disableRefreshLayout();
        getView().showMainLoading();
        getView().hideMainLayout();
        getRideHistoriesUseCase.execute(getView().getHistoriesParam(), new Subscriber<List<RideHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideMainLoading();
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().showRetryLayout();
                }
            }

            @Override
            public void onNext(List<RideHistory> rideHistories) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideMainLoading();
                    ArrayList<Visitable> histories = new ArrayList<>();
                    String mapSize = getView().getMapImageSize();
                    RideHistoryViewModelMapper mapper = new RideHistoryViewModelMapper(getView().getMapKey());
                    for (RideHistory rideHistory : rideHistories) {
                        RideHistoryViewModel viewModel = mapper.transform(mapSize, rideHistory);

                        histories.add(viewModel);
                    }
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    if (histories.size() > 0) {
                        getView().renderHistoryLists(histories);
                        //actionGetOverviewPolyline(histories);
                    } else {
                        getView().showEmptyResultLayout();
                    }
                }
            }
        });
    }

    @Override
    public void actionRefreshHistoriesData() {
        getView().disableRefreshLayout();
        actionGetHistoriesWithPaginationData();
    }

    @Override
    public void actionLoadMore() {
        getView().disableRefreshLayout();
        getView().showLoadMoreLoading();
        getHistoriesWithPaginationUseCase.execute(getView().getHistoriesLoadMoreParam(), new Subscriber<RideHistoryWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoadMoreLoading();
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().showRetryLoadMoreLayout();
                }
            }

            @Override
            public void onNext(RideHistoryWrapper rideHistoryWrapper) {
                if (isViewAttached()) {
                    getView().hideLoadMoreLoading();
                    ArrayList<Visitable> histories = new ArrayList<>();
                    String mapSize = getView().getMapImageSize();
                    RideHistoryViewModelMapper mapper = new RideHistoryViewModelMapper(getView().getMapKey());
                    for (RideHistory rideHistory : rideHistoryWrapper.getHistories()) {
                        RideHistoryViewModel viewModel = mapper.transform(mapSize, rideHistory);
                        histories.add(viewModel);
                    }
                    getView().enableRefreshLayout();
                    getView().setRefreshLayoutToFalse();
                    getView().setPaging(rideHistoryWrapper.getPaging());
                    if (histories.size() > 0) {
                        getView().renderHistoryLoadMoreLists(histories);
                    }
                }
            }
        });
    }
}
