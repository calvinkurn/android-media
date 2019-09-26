package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.topads.sdk.domain.model.WishlistModel;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.FilterStatus;
import com.tokopedia.transaction.orders.orderlist.data.Order;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.surveyrequest.CheckBOMSurveyParams;
import com.tokopedia.transaction.orders.orderlist.data.surveyrequest.InsertBOMSurveyParams;
import com.tokopedia.transaction.orders.orderlist.data.surveyresponse.CheckSurveyResponse;
import com.tokopedia.transaction.orders.orderlist.data.surveyresponse.InsertSurveyResponse;
import com.tokopedia.transaction.orders.orderlist.view.adapter.WishListResponseListener;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewHolder.OrderListRecomListViewHolder;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomTitleViewModel;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListRecomViewModel;
import com.tokopedia.transaction.orders.orderlist.view.adapter.viewModel.OrderListViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private static final String XSOURCE = "recom_widget";
    private static final String PAGE_NAME = "bom_empty";
    GraphqlUseCase getOrderListUseCase;
    GraphqlUseCase checkBomSurveyUseCase;
    GraphqlUseCase insertBomSurveyUseCase;
    private final GetRecommendationUseCase getRecommendationUseCase;
    private final AddToCartUseCase addToCartUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final TopAdsWishlishedUseCase topAdsWishlishedUseCase;
    private final UserSessionInterface userSessionInterface;
    private List<Visitable> orderList = new ArrayList<>();
    private String recomTitle;

    @Inject
    public OrderListPresenterImpl(GetRecommendationUseCase getRecommendationUseCase,
                                  AddToCartUseCase addToCartUseCase,
                                  AddWishListUseCase addWishListUseCase,
                                  RemoveWishListUseCase removeWishListUseCase,
                                  TopAdsWishlishedUseCase topAdsWishlishedUseCase,
                                  UserSessionInterface userSessionInterface) {
        this.getRecommendationUseCase = getRecommendationUseCase;
        this.addToCartUseCase = addToCartUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.topAdsWishlishedUseCase = topAdsWishlishedUseCase;
        this.userSessionInterface = userSessionInterface;
    }

    @Override
    public void processGetRecommendationData(int page, boolean isFirstTime) {
        if (getView() == null)
            return;
        getView().displayLoadMore(true);
        RequestParams requestParam = getRecommendationUseCase.getRecomParams(
                page, XSOURCE, PAGE_NAME, new ArrayList<>());
        getRecommendationUseCase.execute(requestParam, new Subscriber<List<? extends RecommendationWidget>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().displayLoadMore(false);
            }
            @Override
            public void onNext(List<? extends RecommendationWidget> recommendationWidgets) {
                getView().displayLoadMore(false);
                RecommendationWidget recommendationWidget = recommendationWidgets.get(0);
                List<Visitable> visitables = new ArrayList<>();
                if (isFirstTime && recommendationWidget.getRecommendationItemList().size() > 0) {
                    recomTitle = !TextUtils.isEmpty(recommendationWidget.getTitle())
                            ? recommendationWidget.getTitle()
                            : getView().getAppContext().getResources().getString(R.string.order_list_title_recommendation);
                    visitables.add(new OrderListRecomTitleViewModel(recomTitle));
                }
                visitables.addAll(getRecommendationVisitables(recommendationWidget));
                getView().addData(visitables, true);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (getView() == null)
            return;
        orderList.clear();
        getView().displayLoadMore(false);
    }

    @NonNull
    private List<Visitable> getRecommendationVisitables(RecommendationWidget recommendationWidget) {
        List<Visitable> recommendationList = new ArrayList<>();
        for (RecommendationItem item : recommendationWidget.getRecommendationItemList()) {
            recommendationList.add(new OrderListRecomViewModel(item, recomTitle));
        }
        return recommendationList;
    }

    @NonNull
    private List<Visitable> getOrderListVisitables(Data data) {
        List<Visitable> orderList = new ArrayList<>();
        for (Order item : data.orders()) {
            orderList.add(new OrderListViewModel(item));
        }
        return orderList;
    }

    @Override
    public void getAllOrderData(Context context, String orderCategory, final int typeRequest, int page, int orderId) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        getView().showProcessGetData();
        if (page != 0) {
            getView().displayLoadMore(true);
        }
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
                if (getView() != null && getView().getAppContext() != null) {
                    CommonUtils.dumper("error =" + e.toString());
                    getView().removeProgressBarView();
                    getView().displayLoadMore(false);
                    getView().unregisterScrollListener();
                    getView().showErrorNetwork(
                            ErrorHandler.getErrorMessage(getView().getAppContext(), e));
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (getView() == null || getView().getAppContext() == null)
                    return;
                getView().removeProgressBarView();
                getView().displayLoadMore(false);
                if (response != null) {
                    Data data = response.getData(Data.class);
                    if (!data.orders().isEmpty()) {
                        orderList.addAll(getOrderListVisitables(data));
                        getView().addData(getOrderListVisitables(data), false);
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
        if (getView() == null)
            return;
        List<QuickFilterItem> quickFilterItems = new ArrayList<>();
        int selectedIndex = 0;
        boolean isAnyItemSelected = false;
        for (FilterStatus entry : filterItems) {
            CustomViewRoundedQuickFilterItem finishFilter = new CustomViewRoundedQuickFilterItem();
            finishFilter.setName(entry.getFilterName());
            finishFilter.setType(entry.getFilterLabel());
            finishFilter.setColorBorder(R.color.tkpd_main_green);
            if (getView().getSelectedFilter().equalsIgnoreCase(entry.getFilterLabel())) {
                isAnyItemSelected = true;
                finishFilter.setSelected(true);
                selectedIndex = filterItems.indexOf(entry);
            } else {
                finishFilter.setSelected(false);
            }
            quickFilterItems.add(finishFilter);
        }
        //If there is no selected Filter then we will select the first filter item by default
        if (selectedIndex == 0 && !isAnyItemSelected) {
            quickFilterItems.get(selectedIndex).setSelected(true);
        }
        getView().renderOrderStatus(quickFilterItems, selectedIndex);
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
        if (getRecommendationUseCase != null) {
            getRecommendationUseCase.unsubscribe();
        }
        if (addToCartUseCase != null) {
            addToCartUseCase.unsubscribe();
        }
        super.detachView();
    }


    public void checkBomSurveyEligibility() {
        if (getView() == null || getView().getAppContext() == null)
            return;
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
        if (getView() == null || getView().getAppContext() == null)
            return;
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

    public void processAddToCart(Object productModel) {
        if (getView() == null)
            return;
        getView().displayLoadMore(true);

        int productId = 0;
        int shopId = 0;
        String externalSource = "";
        if (productModel instanceof OrderListRecomViewModel) {
            OrderListRecomViewModel orderListRecomViewModel = (OrderListRecomViewModel) productModel;
            productId = orderListRecomViewModel.getRecommendationItem().getProductId();
            shopId = orderListRecomViewModel.getRecommendationItem().getShopId();
            externalSource = "recommendation_list";
        }
        AddToCartRequestParams addToCartRequestParams = new AddToCartRequestParams();
        addToCartRequestParams.setProductId(productId);
        addToCartRequestParams.setShopId(shopId);
        addToCartRequestParams.setQuantity(0);
        addToCartRequestParams.setNotes("");
        addToCartRequestParams.setWarehouseId(0);
        addToCartRequestParams.setAtcFromExternalSource(externalSource);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams);
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddToCartDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (getView() != null) {
                            getView().displayLoadMore(false);
                            String errorMessage = e.getMessage();
                            getView().showFailureMessage(errorMessage);
                        }
                    }

                    @Override
                    public void onNext(AddToCartDataModel addToCartDataModel) {
                        if (getView() != null) {
                            getView().displayLoadMore(false);
                            if (addToCartDataModel.getStatus().equalsIgnoreCase(AddToCartDataModel.STATUS_OK) && addToCartDataModel.getData().getSuccess() == 1) {
                                getView().triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel);
                                if (addToCartDataModel.getData().getMessage().size() > 0) {
                                    getView().showSuccessMessage(addToCartDataModel.getData().getMessage().get(0));
                                }
                            } else {
                                if (addToCartDataModel.getErrorMessage().size() > 0) {
                                    getView().showFailureMessage(addToCartDataModel.getErrorMessage().get(0));
                                }
                            }
                        }

                    }
                });
    }

    public void addWishlist(RecommendationItem model, WishListResponseListener wishListResponseListener) {
        if (getView() == null)
            return;
        getView().displayLoadMore(true);
        if (model.isTopAds()) {
            RequestParams params = RequestParams.create();
            params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.getWishlistUrl());
            topAdsWishlishedUseCase.execute(params, new Subscriber<WishlistModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                        String errorMessage = e.getMessage();
                        getView().showFailureMessage(errorMessage);
                    }
                }

                @Override
                public void onNext(WishlistModel wishlistModel) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                        if (wishlistModel.getData() != null) {
                            wishListResponseListener.onWhishListSuccessResponse(true);
                            getView().showSuccessMessage(getView().getString(R.string.msg_success_add_wishlist));
                        }
                    }
                }
            });
        } else {
            addWishListUseCase.createObservable(String.valueOf(model.getProductId()), userSessionInterface.getUserId(), new WishListActionListener() {
                @Override
                public void onErrorAddWishList(String errorMessage, String productId) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                        getView().showFailureMessage(errorMessage);
                    }
                }

                @Override
                public void onSuccessAddWishlist(String productId) {
                    if (getView() != null) {
                        getView().displayLoadMore(false);
                        wishListResponseListener.onWhishListSuccessResponse(true);
                        getView().showSuccessMessage(getView().getString(R.string.msg_success_add_wishlist));
                    }
                }

                @Override
                public void onErrorRemoveWishlist(String errorMessage, String productId) {

                }

                @Override
                public void onSuccessRemoveWishlist(String productId) {

                }
            });
        }
    }

    public void removeWishlist(RecommendationItem model, WishListResponseListener wishListResponseListener) {
        if (getView() == null)
            return;
        getView().displayLoadMore(true);
        removeWishListUseCase.createObservable(String.valueOf(model.getProductId()), userSessionInterface.getUserId(), new WishListActionListener() {
            @Override
            public void onErrorAddWishList(String errorMessage, String productId) {

            }

            @Override
            public void onSuccessAddWishlist(String productId) {

            }

            @Override
            public void onErrorRemoveWishlist(String errorMessage, String productId) {
                if (getView() != null) {
                    getView().displayLoadMore(false);
                    getView().showFailureMessage(errorMessage);
                }
            }

            @Override
            public void onSuccessRemoveWishlist(String productId) {
                if (getView() != null) {
                    getView().displayLoadMore(false);
                    getView().showSuccessMessage(getView().getString(R.string.msg_success_remove_wishlist));
                    wishListResponseListener.onWhishListSuccessResponse(false);
                }
            }
        });
    }

}
