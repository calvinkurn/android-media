package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.FilterStatus;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.surveyrequest.CheckBOMSurveyParams;
import com.tokopedia.transaction.orders.orderlist.data.surveyrequest.InsertBOMSurveyParams;
import com.tokopedia.transaction.orders.orderlist.data.surveyresponse.CheckSurveyResponse;
import com.tokopedia.transaction.orders.orderlist.data.surveyresponse.InsertSurveyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class OrderListPresenterImpl extends BaseDaggerPresenter<OrderListContract.View> implements OrderListContract.Presenter {

    private static final String SEARCH = "Search";
    private static final String START_DATE = "StartDate";
    private static final String END_DATE = "EndDate";
    private static final String SORT = "Sort";
    private static final String ORDER_STATUS = "OrderStatus";
    private static final String ORDER_ID = "orderId";
    private static final int PER_PAGE_COUNT = 10;
    private static final String SOURCE = "1";
    private static final String DEVICE_TYPE = "android";
    GraphqlUseCase getOrderListUseCase;
    GraphqlUseCase checkBomSurveyUseCase;
    GraphqlUseCase insertBomSurveyUseCase;

    @Inject
    public OrderListPresenterImpl() {

    }

    @Override
    public void getAllOrderData(Context context, String orderCategory, final int typeRequest, int page, int orderId) {
        if (getView().getAppContext() == null)
            return;
        getView().showProcessGetData();
        GraphqlRequest graphqlRequest;
        Map<String, Object> variables = new HashMap<>();

        if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)) {
            variables.put(OrderCategory.KEY_LABEL, orderCategory);
            variables.put(OrderCategory.PAGE, page);
            variables.put(OrderCategory.PER_PAGE, PER_PAGE_COUNT);
            variables.put(SEARCH, getView().getSearchedString());
            variables.put(START_DATE, getView().getStartDate());
            variables.put(END_DATE, getView().getEndDate());
            variables.put(SORT, "");
            variables.put(ORDER_STATUS, Integer.parseInt(getView().getSelectedFilter()));
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist_marketplace), Data.class, variables, false);
        } else {
            variables.put(OrderCategory.KEY_LABEL, orderCategory);
            variables.put(OrderCategory.PAGE, page);
            variables.put(OrderCategory.PER_PAGE, PER_PAGE_COUNT);
            variables.put(ORDER_ID, orderId);
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist), Data.class, variables, false);
        }
        getOrderListUseCase = new GraphqlUseCase();
        getOrderListUseCase.clearRequest();
        getOrderListUseCase.addRequest(graphqlRequest);

        getOrderListUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().removeProgressBarView();
                getView().unregisterScrollListener();
                getView().showErrorNetwork(
                        ErrorHandler.getErrorMessage(getView().getAppContext(), e));
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (getView() == null || getView().getAppContext() == null)
                    return;
                getView().removeProgressBarView();
                if (response != null) {
                    Data data = response.getData(Data.class);
                    if (!data.orders().isEmpty()) {
                        getView().renderDataList(data.orders());
                        getView().setLastOrderId(data.orders().get(0).getOrderId());
                        if (orderCategory.equalsIgnoreCase(OrderCategory.MARKETPLACE)) {
                            checkBomSurveyEligibility();
                        }
                    } else {
                        getView().unregisterScrollListener();
                        getView().renderEmptyList(typeRequest);
                    }
                } else {
                    getView().unregisterScrollListener();
                    getView().renderEmptyList(typeRequest);
                }

            }
        });
    }


    public void buildAndRenderFilterList(List<FilterStatus> filterItems) {
        List<QuickFilterItem> quickFilterItems = new ArrayList<>();
        int selctedIndex = 0;
        boolean isAnyItemSelected = false;
        for (FilterStatus entry : filterItems) {
            CustomViewRoundedQuickFilterItem finishFilter = new CustomViewRoundedQuickFilterItem();
            finishFilter.setName(entry.getFilterName());
            finishFilter.setType(entry.getFilterLabel());
            finishFilter.setColorBorder(R.color.tkpd_main_green);
            if (getView().getSelectedFilter().equalsIgnoreCase(entry.getFilterLabel())) {
                isAnyItemSelected = true;
                finishFilter.setSelected(true);
                selctedIndex = filterItems.indexOf(entry);
            } else {
                finishFilter.setSelected(false);
            }
            quickFilterItems.add(finishFilter);
        }
        getView().renderOrderStatus(quickFilterItems, selctedIndex);
    }


    @Override
    public void detachView() {
        getOrderListUseCase.unsubscribe();
        if (checkBomSurveyUseCase != null) {
            checkBomSurveyUseCase.unsubscribe();
        }
        if (insertBomSurveyUseCase != null) {
            insertBomSurveyUseCase.unsubscribe();
        }
        super.detachView();
    }


    public void checkBomSurveyEligibility() {
        Map<String, Object> variables = new HashMap<>();

        CheckBOMSurveyParams checkBOMSurveyParams = new CheckBOMSurveyParams();
        checkBOMSurveyParams.setSource(SOURCE);

        variables.put(OrderCategory.SURVEY_PARAM, checkBOMSurveyParams);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.checkbomsurvey), CheckSurveyResponse.class, variables);
        checkBomSurveyUseCase = new GraphqlUseCase();
        checkBomSurveyUseCase.clearRequest();
        checkBomSurveyUseCase.addRequest(graphqlRequest);


        checkBomSurveyUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showFailureMessage(e.getMessage());
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewAttached()) {
                    if (graphqlResponse != null) {
                        CheckSurveyResponse checkSurveyResponse = graphqlResponse.getData(CheckSurveyResponse.class);
                        if (checkSurveyResponse != null && checkSurveyResponse.getCheckResponseData() != null) {
                            if (checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders() != null && checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages() != null && checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().size() > 0) {
                                getView().showFailureMessage(checkSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().get(0));
                            } else {
                                if (checkSurveyResponse.getCheckResponseData().getCheckResponseSurveyData() != null) {
                                    getView().showSurveyButton(checkSurveyResponse.getCheckResponseData().getCheckResponseSurveyData().isEligible());
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    public void insertSurveyRequest(int rating, String comment) {
        Map<String, Object> variables = new HashMap<>();

        InsertBOMSurveyParams insertBOMSurveyParams = new InsertBOMSurveyParams();
        insertBOMSurveyParams.setRating(rating);
        insertBOMSurveyParams.setComments(comment);
        insertBOMSurveyParams.setDeviceType(DEVICE_TYPE);

        variables.put(OrderCategory.SURVEY_PARAM, insertBOMSurveyParams);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.insertbomsurvey), InsertSurveyResponse.class, variables);
        insertBomSurveyUseCase = new GraphqlUseCase();
        insertBomSurveyUseCase.clearRequest();
        insertBomSurveyUseCase.addRequest(graphqlRequest);

        insertBomSurveyUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached())
                    getView().showFailureMessage(e.getMessage());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewAttached()) {
                    if (graphqlResponse != null) {
                        InsertSurveyResponse insertSurveyResponse = graphqlResponse.getData(InsertSurveyResponse.class);
                        if (insertSurveyResponse != null && insertSurveyResponse.getCheckResponseData() != null) {
                            if (insertSurveyResponse.getCheckResponseData().getCheckResponseSurveyData().isSuccess()) {
                                getView().showSuccessMessage(getView().getAppContext().getResources().getString(R.string.survey_submit));
                                getView().showSurveyButton(false);
                            } else {
                                if (insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders() != null && insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages() != null && insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().size() > 0) {
                                    getView().showFailureMessage(insertSurveyResponse.getCheckResponseData().getCheckResponseHeaders().getMessages().get(0));
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
