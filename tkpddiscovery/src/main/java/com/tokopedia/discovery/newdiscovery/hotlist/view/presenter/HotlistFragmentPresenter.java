package com.tokopedia.discovery.newdiscovery.hotlist.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.domain.usecase.AddWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.RemoveWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase.GetHotlistInitializeUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase.GetHotlistLoadMoreUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber.AddWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber.GetHotlistInitializeSubscriber;
import com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber.GetHotlistLoadMoreSubscriber;
import com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber.RefreshHotlistSubscriber;
import com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber.RemoveWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.GetDynamicFilterSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenterImpl;
import com.tokopedia.discovery.newdiscovery.util.HotlistParameter;
import com.tokopedia.discovery.newdiscovery.util.WishlistActionListener;

import javax.inject.Inject;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistFragmentPresenter extends SearchSectionFragmentPresenterImpl<HotlistFragmentContract.View>
        implements HotlistFragmentContract.Presenter, WishlistActionListener {

    @Inject
    GetHotlistInitializeUseCase getHotlistInitializeUseCase;

    @Inject
    GetHotlistLoadMoreUseCase getHotlistLoadMoreUseCase;

    @Inject
    GetDynamicFilterUseCase getDynamicFilterUseCase;

    @Inject
    GetProductUseCase getProductUseCase;

    @Inject
    AddWishlistActionUseCase addWishlistActionUseCase;

    @Inject
    RemoveWishlistActionUseCase removeWishlistActionUseCase;

    private final Context context;

    public HotlistFragmentPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void requestDataForTheFirstTime(HotlistParameter parameter) {
        getHotlistInitializeUseCase.setHotlistParameter(parameter);
        getHotlistInitializeUseCase.execute(RequestParams.EMPTY, new GetHotlistInitializeSubscriber(getView()));
    }

    @Override
    public void requestLoadMore() {
        getHotlistLoadMoreUseCase.execute(getParamLoadMoreHotlist(), new GetHotlistLoadMoreSubscriber(getView()));
    }

    private RequestParams getParamLoadMoreHotlist() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_HOTLIST);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.IMAGE_SIZE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(BrowseApi.IMAGE_SQUARE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(BrowseApi.ROWS, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        requestParams.putString(BrowseApi.START, String.valueOf(getView().getStartFrom()));
        if (getView().getQueryModel() != null) {
            requestParams.putString(BrowseApi.OB, getView().getQueryModel().getOrderBy());
            requestParams.putString(BrowseApi.Q, getView().getQueryModel().getQueryKey());
            requestParams.putString(BrowseApi.H, getView().getQueryModel().getHotlistID());
            requestParams.putString(BrowseApi.SHOP_ID, getView().getQueryModel().getShopID());
            requestParams.putString(BrowseApi.FSHOP, getView().getQueryModel().getFilterGoldMerchant());
            requestParams.putString(BrowseApi.PMAX, getView().getQueryModel().getPriceMax());
            requestParams.putString(BrowseApi.PMIN, getView().getQueryModel().getPriceMin());
            requestParams.putString(BrowseApi.DEFAULT_SC, getView().getQueryModel().getCategoryID());
            requestParams.putString(BrowseApi.SC, getView().getQueryModel().getCategoryID());
            requestParams.putString(BrowseApi.NEGATIVE, getView().getQueryModel().getNegativeKeyword());
            requestParams.putString(BrowseApi.HOT_ID, getView().getQueryModel().getHotlistID());
        }

        boolean isLogin = SessionHandler.isV4Login(context);
        String uniqueID = isLogin ? SessionHandler.getLoginID(context) : GCMHandler.getRegistrationId(context);
        requestParams.putString(BrowseApi.UNIQUE_ID, uniqueID);
        if (isLogin) {
            requestParams.putString(BrowseApi.USER_ID, uniqueID);
        }

        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);

        return requestParams;
    }

    @Override
    protected void getFilterFromNetwork(RequestParams requestParams) {
        getDynamicFilterUseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
    }

    @Override
    protected RequestParams getDynamicFilterParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork2(context, requestParams.getParameters()));
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_HOTLIST);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.ROWS, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        requestParams.putString(BrowseApi.START, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_START);
        if (getView().getQueryModel() != null) {
            requestParams.putString(BrowseApi.OB, getView().getQueryModel().getOrderBy());
            requestParams.putString(BrowseApi.Q, getView().getQueryModel().getQueryKey());
            requestParams.putString(BrowseApi.H, getView().getQueryModel().getHotlistID());
            requestParams.putString(BrowseApi.SHOP_ID, getView().getQueryModel().getShopID());
            requestParams.putString(BrowseApi.FSHOP, getView().getQueryModel().getFilterGoldMerchant());
            requestParams.putString(BrowseApi.PMAX, getView().getQueryModel().getPriceMax());
            requestParams.putString(BrowseApi.PMIN, getView().getQueryModel().getPriceMin());
            requestParams.putString(BrowseApi.DEFAULT_SC, getView().getQueryModel().getCategoryID());
            requestParams.putString(BrowseApi.SC, getView().getQueryModel().getCategoryID());
            requestParams.putString(BrowseApi.NEGATIVE, getView().getQueryModel().getNegativeKeyword());
            requestParams.putString(BrowseApi.HOT_ID, getView().getQueryModel().getHotlistID());
        }
        boolean isLogin = SessionHandler.isV4Login(context);
        String uniqueID = isLogin ? SessionHandler.getLoginID(context) : GCMHandler.getRegistrationId(context);
        requestParams.putString(BrowseApi.UNIQUE_ID, uniqueID);
        if (isLogin) {
            requestParams.putString(BrowseApi.USER_ID, uniqueID);
        }
        if (getView().getSelectedSort() != null) {
            requestParams.putAll(getView().getSelectedSort());
        }

        if (getView().getSelectedFilter() != null) {
            requestParams.putAll(getView().getSelectedFilter());
        }
        return requestParams;
    }

    @Override
    public void refreshSort(HotlistHeaderViewModel headerViewModel) {
        getProductUseCase.execute(getParamRefreshHotlist(), new RefreshHotlistSubscriber(getView(), headerViewModel));
    }

    private RequestParams getParamRefreshHotlist() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_HOTLIST);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.IMAGE_SIZE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(BrowseApi.IMAGE_SQUARE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(BrowseApi.ROWS, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        requestParams.putString(BrowseApi.START, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_START);
        if (getView().getQueryModel() != null) {
            requestParams.putString(BrowseApi.OB, getView().getQueryModel().getOrderBy());
            requestParams.putString(BrowseApi.Q, getView().getQueryModel().getQueryKey());
            requestParams.putString(BrowseApi.H, getView().getQueryModel().getHotlistID());
            requestParams.putString(BrowseApi.SHOP_ID, getView().getQueryModel().getShopID());
            requestParams.putString(BrowseApi.FSHOP, getView().getQueryModel().getFilterGoldMerchant());
            requestParams.putString(BrowseApi.PMAX, getView().getQueryModel().getPriceMax());
            requestParams.putString(BrowseApi.PMIN, getView().getQueryModel().getPriceMin());
            requestParams.putString(BrowseApi.DEFAULT_SC, getView().getQueryModel().getCategoryID());
            requestParams.putString(BrowseApi.SC, getView().getQueryModel().getCategoryID());
            requestParams.putString(BrowseApi.NEGATIVE, getView().getQueryModel().getNegativeKeyword());
            requestParams.putString(BrowseApi.HOT_ID, getView().getQueryModel().getHotlistID());
        }
        boolean isLogin = SessionHandler.isV4Login(context);
        String uniqueID = isLogin ? SessionHandler.getLoginID(context) : GCMHandler.getRegistrationId(context);
        requestParams.putString(BrowseApi.UNIQUE_ID, uniqueID);
        if (isLogin) {
            requestParams.putString(BrowseApi.USER_ID, uniqueID);
        }

        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);

        return requestParams;
    }

    @Override
    public void addWishlist(String productID) {
        addWishlistActionUseCase.execute(
                AddWishlistActionUseCase.generateParam(productID, SessionHandler.getLoginID(context)),
                new AddWishlistActionSubscriber(this, productID)
        );
    }

    @Override
    public void removeWishlist(String productID) {
        removeWishlistActionUseCase.execute(
                RemoveWishlistActionUseCase.generateParam(productID, SessionHandler.getLoginID(context)),
                new RemoveWishlistActionSubscriber(this, productID)
        );
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productID) {
        getView().onEditWishlistError(
                errorMessage == null ? context.getString(R.string.msg_error_add_wishlist) : errorMessage,
                productID
        );
    }

    @Override
    public void onSuccessAddWishlist(String productID) {
        getView().onEditWishlistSuccess(context.getString(R.string.msg_add_wishlist), productID);
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productID) {
        getView().onEditWishlistError(
                errorMessage == null ? context.getString(R.string.msg_error_remove_wishlist) : errorMessage,
                productID
        );
    }

    @Override
    public void onSuccessRemoveWishlist(String productID) {
        getView().onEditWishlistSuccess(context.getString(R.string.msg_remove_wishlist), productID);
    }

    @Override
    public void detachView() {
        super.detachView();
        addWishlistActionUseCase.unsubscribe();
        removeWishlistActionUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
        getHotlistInitializeUseCase.unsubscribe();
        getHotlistLoadMoreUseCase.unsubscribe();
        getProductUseCase.unsubscribe();
    }
}
