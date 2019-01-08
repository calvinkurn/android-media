package com.tokopedia.transaction.orders.orderdetails.view.presenter;

import android.text.TextUtils;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButtonList;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderdetails.data.OrderDetails;
import com.tokopedia.transaction.orders.orderdetails.data.PayMethod;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailPresenter extends BaseDaggerPresenter<OrderListDetailContract.View> implements OrderListDetailContract.Presenter {
    private static final String ORDER_CATEGORY = "orderCategoryStr";
    private static final String ORDER_ID = "orderId";
    private static final String DETAIL = "detail";
    private static final String ACTION = "action";
    private static final String UPSTREAM = "upstream";
    private static final String PARAM = "param";
    private static final String INVOICE = "invoice";
    GraphqlUseCase orderDetailsUseCase;
    List<ActionButton> actionButtonList;
    OrderListDetailContract.ActionInterface view;

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailsUseCase) {
        this.orderDetailsUseCase = orderDetailsUseCase;

    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment) {
        if (getView().getAppContext() == null)
            return;

        GraphqlRequest graphqlRequest;
        Map<String, Object> variables = new HashMap<>();
        if (orderCategory.equalsIgnoreCase("marketplace")) {
            variables.put("orderCategory", orderCategory);
            variables.put(ORDER_ID, orderId);
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderdetail_marketplace), DetailsData.class, variables);
        } else {
            variables.put(ORDER_CATEGORY, orderCategory);
            variables.put(ORDER_ID, orderId);
            variables.put(DETAIL, 1);
            if (fromPayment != null && fromPayment.equalsIgnoreCase("true")) {
                variables.put(ACTION, 0);
            } else {
                variables.put(ACTION, 1);
            }
            variables.put(UPSTREAM, "");
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderdetails), DetailsData.class, variables);
        }


        orderDetailsUseCase.addRequest(graphqlRequest);
        orderDetailsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("error occured" + e);
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

    @Override
    public void setActionButton(List<ActionButton> actionButtons, OrderListDetailContract.ActionInterface view, int position, boolean flag) {
        Map<String, Object> variables = new HashMap<>();
        this.view = view;
        variables.put(PARAM, actionButtons);

        orderDetailsUseCase = new GraphqlUseCase();


        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.tapactions), ActionButtonList.class, variables);

        orderDetailsUseCase.clearRequest();
        orderDetailsUseCase.setRequest(graphqlRequest);
        orderDetailsUseCase.createObservable(RequestParams.EMPTY).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonUtils.dumper("error occured" + e);
                    }

                    @Override
                    public void onNext(GraphqlResponse response) {

                        if (response != null) {
                            ActionButtonList data = response.getData(ActionButtonList.class);
                            actionButtonList = data.getActionButtonList();
                            if (actionButtonList != null)
                                if (flag) {
                                    view.setTapActionButton(position, actionButtonList);
                                } else {
                                    view.setActionButton(position, actionButtonList);
                                }
                        }
                    }
                });
    }

    @Override
    public List<ActionButton> getActionList() {
        return actionButtonList;
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
        for (int i = 0; i < details.detail().size(); i++) {
            if (!TextUtils.isEmpty(details.getDropShipper().getDropShipperName()) && !TextUtils.isEmpty(details.getDropShipper().getDropShipperPhone()) && i == 2) {
                //show dropShipper
                getView().showDropshipperInfo(details.getDropShipper());

            } else if (details.getDriverDetails() != null && i == 3) {
                //show Driver information
                getView().showDriverInfo(details.getDriverDetails());
            }
            getView().setDetail(details.detail().get(i));
        }
        if (details.getItems() != null && details.getItems().size() > 0) {
            getView().setItems(details.getItems());
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
            if (!TextUtils.isEmpty(payMethod.getValue()))
                getView().setPayMethodInfo(payMethod);
        }

        for (Pricing pricing : details.pricing()) {
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
            getView().setButtonMargin();
            if (actionButton.getLabel().equals(INVOICE)) {
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