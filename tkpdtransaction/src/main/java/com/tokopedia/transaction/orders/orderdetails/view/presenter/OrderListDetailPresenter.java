package com.tokopedia.transaction.orders.orderdetails.view.presenter;

import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.Detail;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderdetails.data.OrderDetails;
import com.tokopedia.transaction.orders.orderdetails.data.PayMethod;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.transaction.orders.orderdetails.domain.OrderDetailsUseCase;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailPresenter extends BaseDaggerPresenter<OrderListDetailContract.View> implements OrderListDetailContract.Presenter {
    GraphqlUseCase orderDetailsUseCase;

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailsUseCase) {
        this.orderDetailsUseCase = orderDetailsUseCase;
    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderCategory", orderCategory);
        variables.put("orderId", orderId);
        variables.put("detail", 1);
        variables.put("action", 1);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.orderdetails), DetailsData.class, variables);


        orderDetailsUseCase.setRequest(graphqlRequest);
        orderDetailsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("sandeep", "error = " + e);
            }

            @Override
            public void onNext(GraphqlResponse response) {

                if (response != null) {
                    DetailsData data = response.getData(DetailsData.class);

                    setDetailsData(data.orderDetails());
                }
            }
        });
    }

    private void setDetailsData(OrderDetails details) {
        getView().setStatus(details.status());
        if (details.conditionalInfo().text() != null && !details.conditionalInfo().text().equals("")) {
            getView().setConditionalInfo(details.conditionalInfo());
        }
        for (Title title : details.title()) {
            getView().setTitle(title);
        }
        getView().setInvoice(details.invoice());
        getView().setOrderToken(details.orderToken());
        for (Detail detail : details.detail()) {
            getView().setDetail(detail);
        }
        if (details.getItems() != null && details.getItems().size() > 0) {
            getView().setItems(details.getItems());
        }
        if (details.additionalInfo().size() > 0) {
            getView().setAdditionInfoVisibility(View.VISIBLE);
        }
        for (AdditionalInfo additionalInfo : details.additionalInfo()) {

            getView().setAdditionalInfo(additionalInfo);
        }
        for (PayMethod payMethod : details.getPayMethods()) {
            if (payMethod.getValue() != null && !payMethod.getValue().equals(""))
                getView().setPayMethodInfo(payMethod);
        }
        for (Pricing pricing : details.pricing()) {
            if (pricing.value() != null && !pricing.value().equals(""))
                getView().setPricing(pricing);
        }
        getView().setPaymentData(details.paymentData());
//        getView().setContactUs(details.contactUs());

        if (details.actionButtons().size() == 2) {
            ActionButton leftActionButton = details.actionButtons().get(0);
            ActionButton rightActionButton = details.actionButtons().get(1);
            getView().setTopActionButton(leftActionButton);
            getView().setBottomActionButton(rightActionButton);
        } else if (details.actionButtons().size() == 1) {
            ActionButton actionButton = details.actionButtons().get(0);
            if (actionButton.getLabel().equals("invoice")) {
                getView().setBottomActionButton(actionButton);
                getView().setActionButtonsVisibility(View.GONE, View.VISIBLE);
            } else {
                getView().setTopActionButton(actionButton);
                getView().setActionButtonsVisibility(View.VISIBLE, View.GONE);

            }
        } else {
            getView().setActionButtonsVisibility(View.GONE, View.GONE);
        }


        getView().setMainViewVisible(View.VISIBLE);
    }
}