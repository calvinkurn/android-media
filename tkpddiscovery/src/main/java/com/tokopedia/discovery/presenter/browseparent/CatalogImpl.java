package com.tokopedia.discovery.presenter.browseparent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.CatalogModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.Pair;
import com.tokopedia.discovery.fragment.CatalogFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.ErrorContainer;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.presenter.BrowseView;
import com.tokopedia.discovery.presenter.FragmentDiscoveryPresenterImpl;
import com.tokopedia.discovery.view.CatalogView;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public class CatalogImpl extends Catalog implements DiscoveryListener {

    private DiscoveryInteractor discoveryInteractor;
    private NetworkParam.Catalog catalog;
    private BrowseCatalogModel catalogModel;

    private BrowseView browseView;
    private int index;

    public CatalogImpl(CatalogView view) {
        super(view);
    }

    @Override
    public void callNetwork(BrowseView browseView) {
        // jika datanya kosong, maka itu dianggap first time.
        this.browseView = browseView;
        if (view.getDataSize() <= 0) {
            catalog = new NetworkParam.Catalog();
            catalog.start = 0;
            catalog.q = browseView.getProductParam().q;
            catalog.sc = browseView.getProductParam().sc;
            catalog.id = browseView.getProductParam().id;
            catalog.ob = browseView.getProductParam().obCatalog;
            catalog.pmin = browseView.getProductParam().pmin;
            catalog.pmax = browseView.getProductParam().pmax;
            catalog.terms = browseView.getProductParam().terms;// klo terms itu, filter kaya brand, screen dll. kepake d directory product
            catalog.breadcrumb = browseView.getProductParam().breadcrumb;
            catalog.extraFilter = browseView.getProductParam().extraFilter;
            discoveryInteractor.getCatalogs(NetworkParam.generateCatalogQuery(catalog));
            view.setLoading(true);
        }
    }

    @Override
    public void loadMore(Context context) {
        int startIndexForQuery = view.getStartIndexForQuery(TAG);
        if (catalog == null)
            return;

        catalog.start = startIndexForQuery;
        discoveryInteractor.getCatalogs(NetworkParam.generateCatalogQuery(catalog));
    }

    @Override
    public void initData(@NonNull Context context) {
        view.setupRecyclerView();
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
    }

    @Override
    public void fetchArguments(Bundle argument) {
        index = argument.getInt(CatalogFragment.INDEX, 0);
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

    private Pair<List<CatalogModel>, PagingHandler.PagingHandlerModel> parseBrowseCategoryModel(BrowseCatalogModel browseCatalogModel) {
        List<CatalogModel> catalogItems = BrowseCatalogModel.Catalogs.toCatalogItemList(browseCatalogModel.result.catalogs);

        String uriNext = browseCatalogModel.result.paging.getUriNext();
        String uriPrevious = browseCatalogModel.result.paging.getUriPrevious();

        PagingHandler.PagingHandlerModel pagingHandlerModel = FragmentDiscoveryPresenterImpl.getPagingHandlerModel(uriNext, uriPrevious);

        return new Pair<>(catalogItems, pagingHandlerModel);
    }

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
        switch (type) {
            case DiscoveryListener.BROWSE_CATALOG:
                Log.i(TAG, getMessageTAG() + " fetch catalog shop " + data.getModel1());
                BrowseCatalogModel.BrowseCatalogContainer browseCatalogContainer = (BrowseCatalogModel.BrowseCatalogContainer) data.getModel2();
                catalogModel = browseCatalogContainer.body();

                Log.d(TAG, getMessageTAG() + catalogModel);
                if (catalogModel == null)
                    return;

                Pair<List<CatalogModel>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseCategoryModel(catalogModel);
                if (listPagingHandlerModelPair.getModel1().size() == 0) {
                    view.displayEmptyResult();
                } else {
                    view.notifyChangeData(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                }
                fetchDynamicAttribut();
                break;
            case DiscoveryListener.DYNAMIC_ATTRIBUTE:
                DynamicFilterModel.DynamicFilterContainer dynamicFilterContainer = (DynamicFilterModel.DynamicFilterContainer) data.getModel2();
                view.setDynamicFilterAtrribute(dynamicFilterContainer.body().getData(), index);
                break;
            default:

                break;
        }
    }

    public BrowseCatalogModel getCatalogModel() {
        return catalogModel;
    }

    @Override
    public void fetchDynamicAttribut() {
        if (browseView.checkHasFilterAttrIsNull(index)) {
            discoveryInteractor.getDynamicAttribute(
                    view.getContext(), BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG,
                    browseView.getBrowseProductActivityModel().getDepartmentId(),
                    browseView.getBrowseProductActivityModel().getQ());
        }
    }
}
