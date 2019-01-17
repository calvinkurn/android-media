package com.tokopedia.transaction.orders.orderdetails.view.presenter;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButtonList;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderdetails.data.OrderDetails;
import com.tokopedia.transaction.orders.orderdetails.data.PayMethod;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.transaction.orders.orderdetails.domain.FinishOrderUseCase;
import com.tokopedia.transaction.orders.orderdetails.domain.PostCancelReasonUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
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
    PostCancelReasonUseCase postCancelReasonUseCase;
    FinishOrderUseCase finishOrderUseCase;
    OrderListDetailContract.ActionInterface view;
    String orderCategory;

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailsUseCase) {
        this.orderDetailsUseCase = orderDetailsUseCase;
    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment) {
        if (getView().getAppContext() == null)
            return;

        this.orderCategory = orderCategory;
        getView().showProgressBar();
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
                getView().hideProgressBar();
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
        getView().hideProgressBar();
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
            if (i == 2) {
                if (!TextUtils.isEmpty(details.getDropShipper().getDropShipperName()) && !TextUtils.isEmpty(details.getDropShipper().getDropShipperPhone())) {
                    getView().showDropshipperInfo(details.getDropShipper());
                } else if (details.getDriverDetails() != null) {
                    getView().showDriverInfo(details.getDriverDetails());
                }
            }
//                Detail detail = new Detail("No. Resi", "AA1234567890BBCC");
//                details.detail().set(3, detail);
            getView().setDetail(details.detail().get(i));
        }
        if (details.getShopInfo() != null) {
            getView().setShopInfo(details.getShopInfo());
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

        if (!(orderCategory.equalsIgnoreCase("belanja") || orderCategory.equalsIgnoreCase("marketplace"))) {
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
        } else {
            getView().setActionButtons(details.actionButtons());
        }
        getView().setMainViewVisible(View.VISIBLE);
    }


    public void updateOrderCancelReason(String cancelReason, String orderId, int cancelOrReplacement) {

        UserSession userSession = new UserSession(getView().getAppContext());
        String userId = userSession.getUserId();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("reason_cancel", cancelReason);
        requestParams.putString("user_id", userId);
        requestParams.putString("order_id", orderId);
        if (cancelOrReplacement != 1) {
            requestParams.putInt("r_code", cancelOrReplacement);
        }
        getView().showProgressBar();

        this.postCancelReasonUseCase = new PostCancelReasonUseCase(new TkpdOldAuthInterceptor(getView().getAppContext(),
                (NetworkRouter) getView().getAppContext(), new UserSession(getView().getAppContext())), getView().getAppContext());
        postCancelReasonUseCase.setRequestParams(requestParams);
        postCancelReasonUseCase.cancelOrReplaceOrder(cancelOrReplacement);
        postCancelReasonUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper(e.getStackTrace());
                getView().hideProgressBar();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<JsonObject>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                getView().hideProgressBar();
                getView().finishOrderDetail();
            }
        });
    }

    public void finishOrder(String orderId) {
        UserSession userSession = new UserSession(getView().getAppContext());
        String userId = userSession.getUserId();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("user_id", userId);
        requestParams.putString("order_id", orderId);
        getView().showProgressBar();

        this.finishOrderUseCase = new FinishOrderUseCase(new TkpdOldAuthInterceptor(getView().getAppContext(),
                (NetworkRouter) getView().getAppContext(), new UserSession(getView().getAppContext())), getView().getAppContext());
        finishOrderUseCase.setRequestParams(requestParams);
        finishOrderUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper(e.getStackTrace());
                getView().hideProgressBar();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<JsonObject>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                getView().hideProgressBar();
                getView().finishOrderDetail();
            }
        });
    }
}