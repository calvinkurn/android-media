package com.tokopedia.transaction.orders.orderdetails.view.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kotlin.util.DownloadHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.opportunity.data.pojo.CancelReplacementPojo;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButton;
import com.tokopedia.transaction.orders.orderdetails.data.ActionButtonList;
import com.tokopedia.transaction.orders.orderdetails.data.AdditionalInfo;
import com.tokopedia.transaction.orders.orderdetails.data.DataResponseCommon;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderdetails.data.Flags;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.data.MetaDataInfo;
import com.tokopedia.transaction.orders.orderdetails.data.OrderDetails;
import com.tokopedia.transaction.orders.orderdetails.data.PayMethod;
import com.tokopedia.transaction.orders.orderdetails.data.Pricing;
import com.tokopedia.transaction.orders.orderdetails.data.RequestCancelInfo;
import com.tokopedia.transaction.orders.orderdetails.data.Title;
import com.tokopedia.transaction.orders.orderdetails.data.recommendationPojo.RechargeWidgetResponse;
import com.tokopedia.transaction.orders.orderdetails.domain.FinishOrderUseCase;
import com.tokopedia.transaction.orders.orderdetails.domain.PostCancelReasonUseCase;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListAnalytics;
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants;
import com.tokopedia.transaction.common.sharedata.buyagain.ResponseBuyAgain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String TAB_ID = "tabId";
    GraphqlUseCase orderDetailsUseCase;
    List<ActionButton> actionButtonList;
    @Inject
    PostCancelReasonUseCase postCancelReasonUseCase;
    @Inject
    FinishOrderUseCase finishOrderUseCase;
    OrderListDetailContract.ActionInterface view;
    String orderCategory;
    OrderDetails orderDetails;
    @Inject
    OrderListAnalytics orderListAnalytics;
    String fromPayment = null;
    String orderId;
    RequestCancelInfo requestCancelInfo;

    private String Insurance_File_Name = "Invoice";
    public String pdfUri = " ";
    private boolean isdownloadable = false;

    @Inject
    public OrderListDetailPresenter(GraphqlUseCase orderDetailsUseCase) {
        this.orderDetailsUseCase = orderDetailsUseCase;
    }

    @Override
    public void setOrderDetailsContent(String orderId, String orderCategory, String fromPayment) {
        if (getView() == null || getView().getAppContext() == null)
            return;

        this.orderCategory = orderCategory;
        this.fromPayment = fromPayment;
        this.orderId = orderId;
        getView().showProgressBar();
        GraphqlRequest graphqlRequest;
        Map<String, Object> variables = new HashMap<>();
        if (orderCategory.equalsIgnoreCase("marketplace")) {
            variables.put("orderCategory", orderCategory);
            variables.put(ORDER_ID, orderId);
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderdetail_marketplace), DetailsData.class, variables, false);
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
                    R.raw.orderdetails), DetailsData.class, variables, false);
        }


        orderDetailsUseCase.addRequest(graphqlRequest);
        orderDetailsUseCase.addRequest(makegraphqlRequestForRecommendation());
        orderDetailsUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    CommonUtils.dumper("error occured" + e);
                    getView().hideProgressBar();
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (response != null) {
                    DetailsData data = response.getData(DetailsData.class);
                    setDetailsData(data.orderDetails());
                    orderDetails = data.orderDetails();
                    RechargeWidgetResponse rechargeWidgetResponse = response.getData(RechargeWidgetResponse.class);
                    getView().setRecommendation(rechargeWidgetResponse);
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
                R.raw.tapactions), ActionButtonList.class, variables, false);

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
                        if (view != null) {
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
                        } else {
                            if (response != null) {
                                ActionButtonList data = response.getData(ActionButtonList.class);
                                actionButtonList = data.getActionButtonList();
                                if (actionButtonList != null && actionButtonList.size() > 0)
                                getView().setActionButtons(actionButtonList);
                            }
                        }
                    }
                });
    }

    public static final String PRODUCT_ID = "product_id";
    public static final String QUANTITY = "quantity";
    public static final String NOTES = "notes";
    public static final String SHOP_ID = "shop_id";


    private JsonArray generateInputQueryBuyAgain(List<Items> items) {
        JsonArray jsonArray = new JsonArray();
        for (Items item : items) {
            JsonObject passenger = new JsonObject();

            int productId = 0;
            int quantity = 0;
            int shopId = 0;
            String notes = "";
            try {
                productId = item.getId();
                quantity = item.getQuantity();
                shopId = orderDetails.getShopInfo().getShopId();
                notes = item.getDescription();
            } catch (Exception e) {
                Log.e("error parse", e.getMessage());
            }
            passenger.addProperty(PRODUCT_ID, productId);
            passenger.addProperty(QUANTITY, quantity);
            passenger.addProperty(NOTES, notes);
            passenger.addProperty(SHOP_ID, shopId);
            jsonArray.add(passenger);
        }
        return jsonArray;
    }

    @Override
    public List<ActionButton> getActionList() {
        return actionButtonList;
    }

    @Override
    public void onBuyAgainAllItems() {
        onBuyAgainItems(orderDetails.getItems());
    }

    private GraphqlUseCase buyAgainUseCase;

    @Override
    public void onBuyAgainItems(List<Items> items) {
        Map<String, Object> variables = new HashMap<>();
        JsonObject passenger = new JsonObject();
        variables.put(PARAM, generateInputQueryBuyAgain(items));

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.buy_again), ResponseBuyAgain.class, variables, false);

        buyAgainUseCase = new GraphqlUseCase();
        buyAgainUseCase.clearRequest();
        buyAgainUseCase.addRequest(graphqlRequest);

        buyAgainUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    getView().hideProgressBar();
                    getView().showErrorMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                if (getView() != null && getView().getAppContext() != null) {
                    getView().hideProgressBar();
                    ResponseBuyAgain responseBuyAgain = objects.getData(ResponseBuyAgain.class);
                    if (responseBuyAgain.getAddToCartMulti().getData().getSuccess() == 1) {
                        getView().showSuccessMessageWithAction(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(), ","));
                    } else {
                        getView().showErrorMessage(StringUtils.convertListToStringDelimiter(responseBuyAgain.getAddToCartMulti().getData().getMessage(), ","));
                    }
                    orderListAnalytics.sendBuyAgainEvent(items, orderDetails.getShopInfo(), responseBuyAgain.getAddToCartMulti().getData().getData(), responseBuyAgain.getAddToCartMulti().getData().getSuccess() == 1);
                }

            }
        });
    }

    @Override
    public void assignInvoiceDataTo(Intent intent) {
        if (orderDetails == null) return;
        String id = orderDetails.getInvoiceId();
        String invoiceCode = orderDetails.getInvoiceCode();
        String productName = orderDetails.getProductName();
        String date = orderDetails.getBoughtDate();
        String imageUrl = orderDetails.getProductImageUrl();
        String invoiceUrl = orderDetails.getInvoiceUrl();
        String statusId = orderDetails.getStatusId();
        String status = orderDetails.getStatusInfo();
        String totalPriceAmount = orderDetails.getTotalPriceAmount();

        intent.putExtra(ApplinkConst.Chat.INVOICE_ID, id);
        intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, invoiceCode);
        intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, productName);
        intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, date);
        intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, imageUrl);
        intent.putExtra(ApplinkConst.Chat.INVOICE_URL, invoiceUrl);
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, statusId);
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, status);
        intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, totalPriceAmount);
    }

    private void setDetailsData(OrderDetails details) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        getView().hideProgressBar();
        getView().setStatus(details.status());
        getView().clearDynamicViews();
        if (details.conditionalInfo().text() != null && !details.conditionalInfo().text().equals("")) {
            getView().setConditionalInfo(details.conditionalInfo());
        }
        for (Title title : details.title()) {
            getView().setTitle(title);
        }
        getView().setInvoice(details.invoice());
        getView().setOrderToken(details.orderToken());
        for (int i = 0; i < details.detail().size(); i++) {
            if ((orderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || orderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE))) {
                if (i == 2) {
                    if (details.getDriverDetails() != null) {
                        getView().showDriverInfo(details.getDriverDetails());
                    }
                }
                if (i == details.detail().size() - 1) {
                    if (!TextUtils.isEmpty(details.getDropShipper().getDropShipperName()) && !TextUtils.isEmpty(details.getDropShipper().getDropShipperPhone())) {
                        getView().showDropshipperInfo(details.getDropShipper());
                    }
                }
            }
            getView().setDetail(details.detail().get(i));
        }


        if (details.getShopInfo() != null) {
            getView().setShopInfo(details.getShopInfo());
        }
        if (details.getItems() != null && details.getItems().size() > 0) {
            Flags flags = details.getFlags();
            if (flags != null)
                getView().setItems(details.getItems(), flags.isIsOrderTradeIn());
            else
                getView().setItems(details.getItems(), false);
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
        getView().setContactUs(details.contactUs(),details.getHelpLink());

        if (!(orderCategory.equalsIgnoreCase(OrderListContants.BELANJA) || orderCategory.equalsIgnoreCase(OrderListContants.MARKETPLACE))) {
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
        this.requestCancelInfo = details.getRequestCancelInfo();
    }


    public void updateOrderCancelReason(String cancelReason, String orderId,
                                        int cancelOrReplacement, String url) {
        if (getView() == null || getView().getAppContext() == null)
            return;

        UserSession userSession = new UserSession(getView().getAppContext());
        String userId = userSession.getUserId();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("reason_cancel", cancelReason);
        requestParams.putString("user_id", userId);
        requestParams.putString("order_id", orderId);
        requestParams.putString("device_id", userSession.getDeviceId());
        if (cancelOrReplacement != 1) {
            requestParams.putInt("r_code", cancelOrReplacement);
        }
        getView().showProgressBar();

        postCancelReasonUseCase.setRequestParams(requestParams);
        postCancelReasonUseCase.cancelOrReplaceOrder(url);
        postCancelReasonUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (getView() != null && getView().getAppContext() != null) {
                                                    CommonUtils.dumper(e.getStackTrace());
                                                    getView().showErrorMessage(e.getMessage());
                                                    getView().hideProgressBar();
                                                    getView().finishOrderDetail();
                                                }
                                            }

                                            @Override
                                            public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                                                if (getView() != null && getView().getAppContext() != null) {
                                                    Type token = new TypeToken<DataResponseCommon<CancelReplacementPojo>>() {
                                                    }.getType();
                                                    RestResponse restResponse = typeDataResponseMap.get(token);
                                                    DataResponseCommon dataResponse = restResponse.getData();
                                                    CancelReplacementPojo cancelReplacementPojo = (CancelReplacementPojo) dataResponse.getData();
                                                    if (!TextUtils.isEmpty(cancelReplacementPojo.getMessageStatus()))
                                                        getView().showSucessMessage(cancelReplacementPojo.getMessageStatus());
                                                    else if (dataResponse.getErrorMessage() != null && !dataResponse.getErrorMessage().isEmpty())
                                                        getView().showErrorMessage((String) dataResponse.getErrorMessage().get(0));
                                                    else if ((dataResponse.getMessageStatus() != null && !dataResponse.getMessageStatus().isEmpty()))
                                                        getView().showSucessMessage((String) dataResponse.getMessageStatus().get(0));
                                                    getView().hideProgressBar();
                                                    getView().finishOrderDetail();
                                                }
                                            }
                                        }
        );
    }

    public void finishOrder(String orderId, String url) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        UserSession userSession = new UserSession(getView().getAppContext());
        String userId = userSession.getUserId();

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("user_id", userId);
        requestParams.putString("order_id", orderId);
        requestParams.putString("device_id", userSession.getDeviceId());
        getView().showProgressBar();

        finishOrderUseCase.setRequestParams(requestParams);
        finishOrderUseCase.setEndPoint(url);
        finishOrderUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext() != null) {
                    CommonUtils.dumper(e.getStackTrace());
                    getView().hideProgressBar();
                    getView().showErrorMessage(e.getMessage());
                    getView().finishOrderDetail();
                }
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeDataResponseMap) {
                if (getView() != null && getView().getAppContext() != null) {
                    Type token = new TypeToken<DataResponseCommon<CancelReplacementPojo>>() {
                    }.getType();
                    RestResponse restResponse = typeDataResponseMap.get(token);
                    DataResponseCommon dataResponse = restResponse.getData();
                    CancelReplacementPojo cancelReplacementPojo = (CancelReplacementPojo) dataResponse.getData();
                    if (!TextUtils.isEmpty(cancelReplacementPojo.getMessageStatus()))
                        getView().showSucessMessage(cancelReplacementPojo.getMessageStatus());
                    else if (dataResponse.getErrorMessage() != null && !dataResponse.getErrorMessage().isEmpty())
                        getView().showErrorMessage((String) dataResponse.getErrorMessage().get(0));
                    else if ((dataResponse.getMessageStatus() != null && !dataResponse.getMessageStatus().isEmpty()))
                        getView().showSucessMessage((String) dataResponse.getMessageStatus().get(0));
                    getView().hideProgressBar();
                    getView().finishOrderDetail();
                }
            }
        });
    }

    @Override
    public void detachView() {
        orderDetailsUseCase.unsubscribe();
        if (postCancelReasonUseCase != null) {
            postCancelReasonUseCase.unsubscribe();
        }
        if (finishOrderUseCase != null) {
            finishOrderUseCase.unsubscribe();
        }
        if (buyAgainUseCase != null) {
            buyAgainUseCase.unsubscribe();
        }
        super.detachView();
    }

    public void onClick(String uri) {
        pdfUri = uri;
        if (isdownloadable(uri)) {
            getView().askPermission();
        } else {
            if (getView() != null && getView().getAppContext() != null && getView().getAppContext().getApplicationContext() != null && getView().getActivity() != null) {
                ((UnifiedOrderListRouter) getView().getAppContext().getApplicationContext()).actionOpenGeneralWebView((Activity) getView().getActivity(), uri);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void permissionGrantedContinueDownload() {
        DownloadHelper downloadHelper = new DownloadHelper(getView().getAppContext(), pdfUri, Insurance_File_Name, () -> {
            // download success call back

        });
        downloadHelper.downloadFile(this::isdownloadable);
    }
    private Boolean isdownloadable(String uri ) {
        Pattern pattern = Pattern.compile("^.+\\.([pP][dD][fF])$");
        Matcher matcher = pattern.matcher(uri);
        return (matcher.find() || this.isdownloadable);
    }

    public void sendThankYouEvent(MetaDataInfo metaDataInfo) {
        if ("true".equalsIgnoreCase(this.fromPayment)) {
            orderListAnalytics.sendThankYouEvent(metaDataInfo.getEntityProductId(), metaDataInfo.getEntityProductName(), metaDataInfo.getTotalTicketPrice(), metaDataInfo.getTotalTicketCount(), metaDataInfo.getEntityBrandName(), orderId);
        }
    }

    public void setDownloadableFlag(boolean isdownloadable) {
        this.isdownloadable = isdownloadable;
    }

    public void setDownloadableFileName(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            Insurance_File_Name = fileName;
        }
    }

    public String getCancelTime() {
        return requestCancelInfo.getRequestCancelNote();
    }

    public boolean shouldShowTimeForCancellation(){
        return requestCancelInfo != null && !requestCancelInfo.getIsRequestCancelAvail()
                && !TextUtils.isEmpty(requestCancelInfo.getRequestCancelMinTime());
    }

    private GraphqlRequest makegraphqlRequestForRecommendation() {
        GraphqlRequest graphqlRequestForRecommendation;
        Map<String, Object> variablesRecmondettion = new HashMap<>();
        variablesRecmondettion.put(TAB_ID,1);
        graphqlRequestForRecommendation = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.query_rechrage_widget), RechargeWidgetResponse.class, variablesRecmondettion);
        return graphqlRequestForRecommendation;
    }

}
