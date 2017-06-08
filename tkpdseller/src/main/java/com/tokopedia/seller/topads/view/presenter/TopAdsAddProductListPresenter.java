package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.domain.model.ProductListDomain;
import com.tokopedia.seller.topads.utils.DefaultErrorSubscriber;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.TopAdsSearchProductView;
import com.tokopedia.seller.topads.view.mapper.TopAdsProductModelMapper;
import com.tokopedia.seller.topads.view.model.NonPromotedTopAdsAddProductModel;
import com.tokopedia.seller.topads.view.model.PromotedTopAdsAddProductModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author normansyahputa on 2/20/17.
 */

public class TopAdsAddProductListPresenter extends BaseDaggerPresenter<TopAdsSearchProductView> {

    public static final int PAGE_ROW = 12;
    private static final int
            SEARCHVIEW_NETWORK_CALL = 0,
            LOAD_MORE_NETWORK_CALL = 1,
            PULL_TO_REFRESH_NETWORK_CALL = 2,
            RETRY_NETWORK_CALL = 3,
            ON_ACTIVITY_FOR_RESULT = 4,
            NO_NETWORK_CALL = -1;

    private SessionHandler sessionHandler;
    private TopAdsProductListUseCase topAdsProductListUseCase;
    private Map<String, String> params;
    private TopAdsSearchProductView view;
    private int page;
    private String query;
    private int selectedFilterEtalaseId = -1;
    private int selectedFilterStatus = -1;
    private DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener;
    private NetworkStatus networkStatus;
    private int networkCallCount = 0;
    public TopAdsAddProductListPresenter() {
        page = 0;
        params = new TKPDMapParam<>();
        fillParam(sessionHandler);

        // set this flag to hit network
        setNetworkStatus(NetworkStatus.PULLTOREFRESH);
    }

    public void incrementPage() {
        page++;
    }

    public void resetPage() {
        page = 0;
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    public void setTopAdsProductListUseCase(TopAdsProductListUseCase topAdsProductListUseCase) {
        this.topAdsProductListUseCase = topAdsProductListUseCase;
    }

    public void setErrorNetworkListener(DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener) {
        this.errorNetworkListener = errorNetworkListener;
    }

    private void resetHitNetwork() {
        setNetworkStatus(NetworkStatus.NONETWORKCALL);
    }

    public boolean isFirstTime(){
        return (isHitNetwork() && networkCallCount <= 0);
    }

    private void fillParam(SessionHandler sessionHandler) {
        if (sessionHandler != null)
            params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, sessionHandler.getShopID());
        if (query != null) {
            params.put(TopAdsNetworkConstant.PARAM_KEYWORD, query);
        } else {
            params.remove(TopAdsNetworkConstant.PARAM_KEYWORD);
        }

        params.put(TopAdsNetworkConstant.PARAM_ROWS, Integer.toString(PAGE_ROW));
        params.put(TopAdsNetworkConstant.PARAM_START, Integer.toString(PAGE_ROW * page));
        if (selectedFilterEtalaseId > 0) {
            params.put(TopAdsNetworkConstant.PARAM_ETALASE, Integer.toString(selectedFilterEtalaseId));
        } else {
            params.remove(TopAdsNetworkConstant.PARAM_ETALASE);
        }

        if (selectedFilterStatus >= 0) {
            params.put(TopAdsNetworkConstant.PARAM_IS_PROMOTED, Integer.toString(selectedFilterStatus));
        } else {
            params.remove(TopAdsNetworkConstant.PARAM_IS_PROMOTED);
        }
    }

    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;

        switch (getNetworkStatus()) {
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
            case SEARCHVIEW:
                resetPage();
                if (isViewAttached())
                    getView().resetEmptyViewHolder();
                break;
            default:
                break;
        }
    }

    public void loadMore() {
        if (isHitNetwork()) {
            fillParam(sessionHandler);
            topAdsProductListUseCase.execute(params,
                    new DefaultErrorSubscriber<ProductListDomain>(errorNetworkListener) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (ViewUtils.getErrorMessage(e) != null) {
                                showMessageError(new StringBuilder(
                                        ViewUtils.getErrorMessage(e)
                                ));
                            } else {
                                super.onError(e);
                            }
                        }

                        @Override
                        public void onNext(ProductListDomain productListDomain) {
                            if (isViewAttached()) {
                                if (productListDomain.getPage() > page) {
                                    page = productListDomain.getPage();
                                }
                                getView().dismissSnackbar();
                                getView().setLoadMoreFlag(productListDomain.isEof());
                                getView().loadMore(convertTo(productListDomain.getProductDomains()));
                                networkCallCount++;
                            }
                        }
                    });
        }
    }

    public void searchProduct() {
        if (isHitNetwork()) {
            fillParam(sessionHandler);
            topAdsProductListUseCase.execute(params,
                    new DefaultErrorSubscriber<ProductListDomain>(errorNetworkListener) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (ViewUtils.getErrorMessage(e) != null) {
                                showMessageError(new StringBuilder(
                                        ViewUtils.getErrorMessage(e)
                                ));
                            } else {
                                super.onError(e);
                            }
                        }

                        @Override
                        public void onNext(ProductListDomain productListDomain) {
                            if (isViewAttached()) {
                                if (productListDomain.getPage() > page) {
                                    page = productListDomain.getPage();
                                }
                                getView().dismissSnackbar();
                                getView().setLoadMoreFlag(productListDomain.isEof());
                                getView().loadData(convertTo(productListDomain.getProductDomains()));
                                networkCallCount++;
                            }
                        }
                    });
        }
    }

    public boolean isHitNetwork() {
        switch (networkStatus) {
            case LOADMORE:
            case PULLTOREFRESH:
            case SEARCHVIEW:
            case RETRYNETWORKCALL:
            case ONACTIVITYFORRESULT:
                return true;
            case NONETWORKCALL:
            default:
                return false;
        }
    }

    private List<TypeBasedModel> convertTo(List<ProductDomain> productDomains) {
        List<TypeBasedModel> typeBasedModels = new ArrayList<>();

        boolean skipWithAdId = getView().isExistingGroup();

        for (ProductDomain productDomain : productDomains) {

            if (skipWithAdId && productDomain.isPromoted()) {
                PromotedTopAdsAddProductModel promotedTopAdsAddProductModel
                        = new PromotedTopAdsAddProductModel(
                        productDomain.getName(),
                        productDomain.getGroupName(),
                        TopAdsProductModelMapper.convertModelFromDomainToView(productDomain)
                );
                typeBasedModels.add(promotedTopAdsAddProductModel);
            } else {
                String groupName = productDomain.getGroupName();
                NonPromotedTopAdsAddProductModel nonPromotedTopAdsAddProductModel
                        = new NonPromotedTopAdsAddProductModel(
                        productDomain.getName(),
                        (groupName == null || groupName.isEmpty()) ? null : groupName,
                        TopAdsProductModelMapper.convertModelFromDomainToView(productDomain)
                );
                typeBasedModels.add(nonPromotedTopAdsAddProductModel);
            }


        }


        return typeBasedModels;
    }

    @Override
    public void attachView(TopAdsSearchProductView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsProductListUseCase.unsubscribe();
    }

    public void putSelectedEtalaseId(int etalaseId){
        this.selectedFilterEtalaseId = etalaseId;
    }

    public void putSelectedFilterStatus(int filterStatus){
        this.selectedFilterStatus = filterStatus;
    }

    public int getSelectedFilterEtalaseId() {
        return selectedFilterEtalaseId;
    }

    public int getSelectedFilterStatus() {
        return selectedFilterStatus;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public enum NetworkStatus {
        SEARCHVIEW(SEARCHVIEW_NETWORK_CALL),
        LOADMORE(LOAD_MORE_NETWORK_CALL),
        PULLTOREFRESH(PULL_TO_REFRESH_NETWORK_CALL),
        NONETWORKCALL(NO_NETWORK_CALL),
        RETRYNETWORKCALL(RETRY_NETWORK_CALL),
        ONACTIVITYFORRESULT(ON_ACTIVITY_FOR_RESULT);

        private int type;

        NetworkStatus(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
