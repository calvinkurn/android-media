package com.tokopedia.discovery.presenter.browseparent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.network.entity.discovery.BrowseShopModel;
import com.tokopedia.core.network.entity.discovery.ShopModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.Pair;
import com.tokopedia.discovery.fragment.ShopFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.ErrorContainer;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.discovery.view.ShopView;

import java.util.List;


/**
 * Created by Erry on 6/30/2016.
 */
public class ShopImpl extends Shop implements DiscoveryListener {
    DiscoveryInteractor discoveryInteractor;
    NetworkParam.Shop shop;

    private int index;
    private BrowseView browseView;

    public ShopImpl(ShopView view) {
        super(view);
    }

    @Override
    public void callNetwork(BrowseView browseView) {
        // jika datanya kosong, maka itu dianggap first time.
        this.browseView = browseView;
        if (view.getDataSize() <= 0) {
            shop = new NetworkParam.Shop();
            shop.floc = browseView.getProductParam().floc;
            shop.q = browseView.getProductParam().q;
            shop.fshop = browseView.getProductParam().fshop;
            shop.start = 0;
            shop.extraFilter = browseView.getProductParam().extraFilter;
            view.setLoading(true);
            discoveryInteractor.getShops(NetworkParam.generateShopQuery(shop));
        }
    }

    @Override
    public void loadMore(Context context) {
        int startIndexForQuery = view.getStartIndexForQuery(TAG);
        if (shop == null)
            return;

        shop.start = startIndexForQuery;
        discoveryInteractor.getShops(NetworkParam.generateShopQuery(shop));
    }

    @Override
    public void initData(@NonNull Context context) {
        view.setupRecyclerView();
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
    }

    @Override
    public void fetchArguments(Bundle argument) {
        index = argument.getInt(ShopFragment.INDEX, 0);

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        view.initAdapter();
        discoveryInteractor = new DiscoveryInteractorImpl();
        discoveryInteractor.setDiscoveryListener(this);
    }

    @Override
    public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

    }

    @Override
    public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
        view.ariseRetry(type, ((ErrorContainer) data.getModel2()).body().getMessage());
    }

    private Pair<List<ShopModel>, PagingHandler.PagingHandlerModel> parseBrowseShopModel(BrowseShopModel browseShopModel) {
        List<ShopModel> shopItems = BrowseShopModel.Shops.toShopItemList(browseShopModel.result.shops);

        String uriNext = browseShopModel.result.paging.getUriNext();
        String uriPrevious = browseShopModel.result.paging.getUriPrevious();

        PagingHandler.PagingHandlerModel pagingHandlerModel = FragmentDiscoveryPresenterImpl.getPagingHandlerModel(uriNext, uriPrevious);

        return new Pair<>(shopItems, pagingHandlerModel);
    }

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
        switch (type) {
            case DiscoveryListener.BROWSE_SHOP:
                Log.i(TAG, getMessageTAG() + " fetch browse shop " + data.getModel1());
                BrowseShopModel.BrowseShopContainer browseShopContainer
                        = (BrowseShopModel.BrowseShopContainer) data.getModel2();
                BrowseShopModel browseShopModel = browseShopContainer.body();

                Log.d(TAG, getMessageTAG() + browseShopModel);
                if (browseShopModel == null)
                    return;

                Pair<List<ShopModel>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseShopModel(browseShopModel);
                view.setLoading(false);
                view.setShopData(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                fetchDynamicAttribut();
                break;
            case DiscoveryListener.DYNAMIC_ATTRIBUTE:
                DynamicFilterModel.DynamicFilterContainer dynamicFilterContainer = (DynamicFilterModel.DynamicFilterContainer) data.getModel2();
                view.setDynamicFilterAtrribute(dynamicFilterContainer.body().getData(), index);
            default:

                break;
        }
    }

    @Override
    public void fetchDynamicAttribut() {
        if (browseView.checkHasFilterAttrIsNull(index)) {
            discoveryInteractor.getDynamicAttribute(view.getContext(),
                    BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP,
                    browseView.getBrowseProductActivityModel().getDepartmentId(),
                    browseView.getBrowseProductActivityModel().getQ());
        }
    }
}
