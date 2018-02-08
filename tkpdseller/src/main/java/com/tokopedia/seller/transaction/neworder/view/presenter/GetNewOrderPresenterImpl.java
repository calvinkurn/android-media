package com.tokopedia.seller.transaction.neworder.view.presenter;

import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.seller.transaction.neworder.domain.interactor.GetNewOrderWidgetUseCase;
import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDomainWidget;
import com.tokopedia.seller.transaction.neworder.view.mapper.NewOrderMapperView;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class GetNewOrderPresenterImpl extends GetNewOrderPresenter {
    public static final String PAGE = "1";
    public static final String PER_PAGE = "10";
    private final GetNewOrderWidgetUseCase getNewOrderUseCase;

    public GetNewOrderPresenterImpl(GetNewOrderWidgetUseCase getNewOrderUseCase) {
        this.getNewOrderUseCase = getNewOrderUseCase;
    }

    @Override
    public void getNewOrderAndCount() {
        getNewOrderUseCase.executeSync(GetNewOrderWidgetUseCase.createRequestParams(PAGE, "", PER_PAGE, ""), new GetSubscriberNewOrder());
    }

    @Override
    public void getNewOrderAndCountAsync() {
        getNewOrderUseCase.execute(GetNewOrderWidgetUseCase.createRequestParams(PAGE, "", PER_PAGE, ""), new GetSubscriberNewOrder());
    }

    private class GetSubscriberNewOrder extends Subscriber<DataOrderDomainWidget> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().onErrorGetDataOrders();
        }

        @Override
        public void onNext(DataOrderDomainWidget dataOrders) {
            checkViewAttached();
            if(dataOrders != null) {
                getView().onSuccessGetDataOrders(NewOrderMapperView.converToModelView(dataOrders));
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
