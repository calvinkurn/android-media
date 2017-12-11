package com.tokopedia.flight.orderlist.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase;

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
        flightGetOrdersUseCase.execute(flightGetOrdersUseCase.createRequestParam(0), new Subscriber<List<OrderEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    buildAndRenderFilterList();
                    getView().hideGetInitialOrderDataLoading();
                }
            }

            @Override
            public void onNext(List<OrderEntity> orderEntities) {
                buildAndRenderFilterList();
                getView().hideGetInitialOrderDataLoading();

            }
        });
    }

    private void buildAndRenderFilterList() {

        int[] colorBorder = new int[4];
        colorBorder[0] = R.color.filter_order_blue;
        colorBorder[1] = R.color.filter_order_green;
        colorBorder[2] = R.color.filter_order_orange;
        colorBorder[3] = R.color.filter_order_green_medium;

        Map<String, String> filtersMap = new HashMap<>();
        filtersMap.put("100", "Finished");
        filtersMap.put("700", "Confirmed");
        filtersMap.put("650", "Refunded");
        filtersMap.put("600", "Failed");
        filtersMap.put("300", "In Progress");
        filtersMap.put("200", "Ready for Queue");
        filtersMap.put("102", "Waiting for Transfer");
        filtersMap.put("101", "Waiting for Third Party");
        filtersMap.put("100", "Waiting for Payment");
        filtersMap.put("0", "Expired");

        List<QuickFilterItem> filterItems = new ArrayList<>();
        int colorInd = 0;
        for (Map.Entry<String, String> entry : filtersMap.entrySet()) {
            QuickFilterItem finishFilter = new QuickFilterItem();
            finishFilter.setName(entry.getValue());
            finishFilter.setType(entry.getKey());
            finishFilter.setColorBorder(colorBorder[colorInd]);
            finishFilter.setSelected(false);
            filterItems.add(finishFilter);
            colorInd++;
            if (colorInd >= colorBorder.length) {
                colorInd = 0;
            }
        }

        getView().renderOrderStatus(filterItems);

    }
}
