package com.tokopedia.seller.transaction.neworder.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.seller.transaction.neworder.domain.GetNewOrderRepository;
import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDetailDomain;
import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDomainWidget;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public class GetNewOrderWidgetUseCase extends UseCase<DataOrderDomainWidget> {
    public static final String STATUS = "status";
    public static final String PAGE = "page";
    public static final String PER_PAGE = "per_page";
    public static final String DEADLINE = "deadline";
    private final GetNewOrderRepository getNewOrderRepository;
    private final NotificationRepository notificationRepository;

    @Inject
    public GetNewOrderWidgetUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                    GetNewOrderRepository getNewOrderRepository, NotificationRepository notificationRepository) {
        super(threadExecutor, postExecutionThread);
        this.getNewOrderRepository = getNewOrderRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Observable<DataOrderDomainWidget> createObservable(RequestParams requestParams) {
        return Observable.zip(getNewOrderRepository.getNewOrderList(requestParams),
                notificationRepository.getNotification(NotificationUseCase.getRequestParam(true).getParameters()), new Func2<List<DataOrderDetailDomain>, NotificationModel, DataOrderDomainWidget>() {
                    @Override
                    public DataOrderDomainWidget call(List<DataOrderDetailDomain> dataOrderDetailDomains, NotificationModel notificationModel) {
                        DataOrderDomainWidget dataOrderDomainWidget = new DataOrderDomainWidget();
                        dataOrderDomainWidget.setDataOrderDetailDomains(dataOrderDetailDomains);
                        dataOrderDomainWidget.setDataOrderCount(notificationModel.getNotificationData().getSales().getSalesNewOrder());
                        return dataOrderDomainWidget;
                    }
                });
    }

    public static RequestParams createRequestParams(String page, String filter, String perPage, String deadline) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(STATUS, filter);
        requestParams.putString(PAGE, page);
        requestParams.putString(PER_PAGE, perPage);
        requestParams.putString(DEADLINE, deadline);
        return requestParams;
    }
}
