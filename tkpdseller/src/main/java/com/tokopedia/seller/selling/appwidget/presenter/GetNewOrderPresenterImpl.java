package com.tokopedia.seller.selling.appwidget.presenter;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;
import com.tokopedia.seller.selling.appwidget.domain.interactor.GetNewOrderUseCase;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class GetNewOrderPresenterImpl extends GetNewOrderPresenter {
    private final GetNewOrderUseCase getNewOrderUseCase;

    public GetNewOrderPresenterImpl(GetNewOrderUseCase getNewOrderUseCase) {
        this.getNewOrderUseCase = getNewOrderUseCase;
    }

    @Override
    public void getNewOrder() {
        getNewOrderUseCase.execute(GetNewOrderUseCase.createRequestParams(), new GetSubscriberNewOrder());
    }

    private class GetSubscriberNewOrder extends Subscriber<List<DataOrder>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().onErrorGetDataOrders();
        }

        @Override
        public void onNext(List<DataOrder> dataOrders) {
            checkViewAttached();
            if(dataOrders != null) {
                getView().onSuccessGetDataOrders(dataOrders);
            }else{
                getView().onErrorGetDataOrders();
            }
        }
    }

    @Override
    public void unSubscribe() {
        getNewOrderUseCase.unsubscribe();
    }
}
