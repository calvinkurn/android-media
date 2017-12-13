package com.tokopedia.flight.orderlist.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderFailedViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 12/6/17.
 */

public class FlightOrderListPresenter extends BaseDaggerPresenter<FlightOrderListContract.View> implements FlightOrderListContract.Presenter {
    private FlightGetOrdersUseCase flightGetOrdersUseCase;

    @Inject
    public FlightOrderListPresenter(FlightGetOrdersUseCase flightGetOrdersUseCase) {
        this.flightGetOrdersUseCase = flightGetOrdersUseCase;
    }


    @Override
    public void getInitialOrderData() {
        getView().showGetInitialOrderDataLoading();
        flightGetOrdersUseCase.execute(flightGetOrdersUseCase.createRequestParam(0), new Subscriber<List<FlightOrder>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideGetInitialOrderDataLoading();
                    getView().showErrorGetInitialOrders(FlightErrorUtil.getMessageFromException(e));
                }
            }

            @Override
            public void onNext(List<FlightOrder> orderEntities) {
                buildAndRenderFilterList();
                getView().hideGetInitialOrderDataLoading();
                if (orderEntities.size() > 0) {
                    getView().renderOrders(transformOrderDataToViewData(orderEntities));
                } else {
                    getView().showEmptyView();
                }
            }
        });
    }

    @Override
    public void onFilterSelected(String typeFilter) {
        getView().showGetInitialOrderDataLoading();
        flightGetOrdersUseCase.execute(flightGetOrdersUseCase.createRequestParam(0, typeFilter), new Subscriber<List<FlightOrder>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideGetInitialOrderDataLoading();
                    getView().showErrorGetOrderOnFilterChanged(FlightErrorUtil.getMessageFromException(e));
                }
            }

            @Override
            public void onNext(List<FlightOrder> orderEntities) {
                buildAndRenderFilterList();
                getView().hideGetInitialOrderDataLoading();
                if (orderEntities.size() > 0) {
                    getView().renderOrders(transformOrderDataToViewData(orderEntities));
                } else {
                    getView().showEmptyView();
                }
            }
        });
    }

    @Override
    public void onOrderLoadMore(String selectedFilter, int page) {
        getView().showLoadMoreLoading();
        flightGetOrdersUseCase.execute(flightGetOrdersUseCase.createRequestParam(page, selectedFilter), new Subscriber<List<FlightOrder>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoadMoreLoading();
                    getView().setLoadMoreStatusToFalse();
                }
            }

            @Override
            public void onNext(List<FlightOrder> orderEntities) {
                getView().hideLoadMoreLoading();
                getView().setLoadMoreStatusToFalse();
                getView().renderAddMoreData(transformOrderDataToViewData(orderEntities));
            }
        });
    }

    private List<Visitable> transformOrderDataToViewData(List<FlightOrder> flightOrders) {
        List<Visitable> visitables = new ArrayList<>();
        for (FlightOrder flightOrder : flightOrders) {
            switch (flightOrder.getStatus()) {
                case 700:
                    for (FlightOrderJourney journey : flightOrder.getJourneys()) {
                        FlightOrderSuccessViewModel successViewModel = new FlightOrderSuccessViewModel();
                        successViewModel.setCreateTime(flightOrder.getCreateTime());
                        successViewModel.setId(flightOrder.getId());
                        successViewModel.setOrderJourney(journey);
                        successViewModel.setTitle(getView().getString(R.string.flight_order_success_title));
                        successViewModel.setStatus(journey.getStatus());
                        visitables.add(successViewModel);
                    }
                    break;
                case 600:
                    FlightOrderFailedViewModel failedViewModel = new FlightOrderFailedViewModel();
                    failedViewModel.setCreateTime(flightOrder.getCreateTime());
                    failedViewModel.setId(flightOrder.getId());
                    failedViewModel.setOrderJourney(flightOrder.getJourneys());
                    failedViewModel.setStatus(flightOrder.getStatus());
                    failedViewModel.setTitle(getView().getString(R.string.flight_order_failed_title));
                    visitables.add(failedViewModel);
                    break;
                case 0:
                    FlightOrderFailedViewModel expired = new FlightOrderFailedViewModel();
                    expired.setCreateTime(flightOrder.getCreateTime());
                    expired.setId(flightOrder.getId());
                    expired.setOrderJourney(flightOrder.getJourneys());
                    expired.setStatus(flightOrder.getStatus());
                    expired.setTitle(getView().getString(R.string.flight_order_expire_title));
                    visitables.add(expired);
                    break;

            }
        }
        return visitables;

    }

    private void buildAndRenderFilterList() {

        int[] colorBorder = new int[4];
        colorBorder[0] = R.color.filter_order_green;
        colorBorder[1] = R.color.filter_order_red;
        colorBorder[2] = R.color.filter_order_orange;
        colorBorder[3] = R.color.filter_order_yellow;

        Map<String, String> filtersMap = new HashMap<>();
        filtersMap.put("100", "Berhasil");
        filtersMap.put("700", "Tidak Berhasil");
        filtersMap.put("650", "Menunggu Pembayaran");
        filtersMap.put("300", "Dalam Proses");

        List<QuickFilterItem> filterItems = new ArrayList<>();
        int colorInd = 0;
        for (Map.Entry<String, String> entry : filtersMap.entrySet()) {
            QuickFilterItem finishFilter = new QuickFilterItem();
            finishFilter.setName(entry.getValue());
            finishFilter.setType(entry.getKey());
            finishFilter.setColorBorder(colorBorder[colorInd]);
            if (getView().getSelectedFilter().equalsIgnoreCase(entry.getKey())) {
                finishFilter.setSelected(true);
            } else {
                finishFilter.setSelected(false);
            }
            filterItems.add(finishFilter);
            colorInd++;
            if (colorInd >= colorBorder.length) {
                colorInd = 0;
            }
        }

        getView().renderOrderStatus(filterItems);

    }
}
