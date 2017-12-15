package com.tokopedia.flight.orderlist.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.view.viewmodel.mapper.FlightOrderViewModelMapper;

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
    private FlightOrderViewModelMapper flightOrderViewModelMapper;

    @Inject
    public FlightOrderListPresenter(FlightGetOrdersUseCase flightGetOrdersUseCase,
                                    FlightOrderViewModelMapper flightOrderViewModelMapper) {
        this.flightGetOrdersUseCase = flightGetOrdersUseCase;
        this.flightOrderViewModelMapper = flightOrderViewModelMapper;
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
                    getView().renderOrders(flightOrderViewModelMapper.transform(orderEntities));
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
                    getView().renderOrders(flightOrderViewModelMapper.transform(orderEntities));
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
                getView().renderAddMoreData(flightOrderViewModelMapper.transform(orderEntities));
            }
        });
    }

    @Override
    public void onSwipeRefresh() {
        getView().showGetInitialOrderDataLoading();
        getView().disableSwipeRefresh();
        flightGetOrdersUseCase.execute(flightGetOrdersUseCase.createRequestParam(0), new Subscriber<List<FlightOrder>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().enableSwipeRefresh();
                    getView().hideGetInitialOrderDataLoading();
                    getView().showErrorGetInitialOrders(FlightErrorUtil.getMessageFromException(e));
                }
            }

            @Override
            public void onNext(List<FlightOrder> orderEntities) {
                getView().enableSwipeRefresh();
                buildAndRenderFilterList();
                getView().hideGetInitialOrderDataLoading();
                if (orderEntities.size() > 0) {
                    getView().renderOrders(flightOrderViewModelMapper.transform(orderEntities));
                } else {
                    getView().showEmptyView();
                }
            }
        });
    }

    private void buildAndRenderFilterList() {
        int[] colorBorder = new int[5];
        colorBorder[0] = R.color.filter_order_green;
        colorBorder[1] = R.color.filter_order_red;
        colorBorder[2] = R.color.filter_order_orange;
        colorBorder[3] = R.color.filter_order_yellow;
        colorBorder[4] = R.color.filter_order_blue;

        Map<String, String> filtersMap = new HashMap<>();
        filtersMap.put("650", getView().getString(R.string.flight_order_status_refund_label));
        filtersMap.put("100,102", getView().getString(R.string.flight_order_status_waiting_for_payment_label));
        filtersMap.put("101,200,300", getView().getString(R.string.flight_order_status_in_progress_label));
        filtersMap.put("0,600", getView().getString(R.string.flight_order_status_failed_label));
        filtersMap.put("700,800", getView().getString(R.string.flight_order_status_success_label));

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
