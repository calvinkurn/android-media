package com.tokopedia.flight.orderlist.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase;
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 12/6/17.
 */

public class FlightOrderListPresenter extends BaseDaggerPresenter<FlightOrderListContract.View> implements FlightOrderListContract.Presenter {
    private FlightGetOrdersUseCase flightGetOrdersUseCase;
    private FlightGetOrderUseCase flightGetOrderUseCase;

    @Inject
    public FlightOrderListPresenter(FlightGetOrdersUseCase flightGetOrdersUseCase,
                                    FlightGetOrderUseCase flightGetOrderUseCase) {
        this.flightGetOrdersUseCase = flightGetOrdersUseCase;
        this.flightGetOrderUseCase = flightGetOrderUseCase;
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
        List<QuickFilterItem> filterItems = new ArrayList<>();

        int[] colorBorder = new int[4];
        /*colorBorder[0] = R.color.filter_inside_blue;
        colorBorder[1] = R.color.filter_inside_green;
        colorBorder[2] = R.color.filter_inside_orange;
        colorBorder[3] = R.color.filter_inside_green_medium;
        */
    }


}
