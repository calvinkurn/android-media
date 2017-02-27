package com.tokopedia.discovery.presenter.browseparent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.adapter.browseparent.BrowserSectionsPagerAdapter;
import com.tokopedia.discovery.fragment.browseparent.BrowseParentFragment;
import com.tokopedia.discovery.fragment.browseparent.ProductFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.view.BrowseProductParentView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Erry on 6/30/2016.
 */
public class BrowseProductParentImpl extends BrowseProductParent implements DiscoveryListener {

    private DiscoveryInteractor discoveryInteractor;
    private static final String TAG = BrowseProductParentImpl.class.getSimpleName();
    // this will get for product only
    BrowseProductModel browseProductModel;
    NetworkParam.Product p;
    HotListBannerModel hotListBannerModel;
    BrowseProductActivityModel browseProductActivityModel;
    private int index;

    public BrowseProductParentImpl(BrowseProductParentView view) {
        super(view);
    }

    public BrowseProductActivityModel getBrowseProductActivityModel() {
        return browseProductActivityModel;
    }

    @Override
    public BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly) {
        return browseProductModel;
    }

    @Override
    public NetworkParam.Product getProductParam() {
        return p;
    }

    @Override
    public List<Breadcrumb> getBreadCrumb() {
        List<Breadcrumb> breadcrumbList = new ArrayList<>();
        for (Breadcrumb breadcrumb : browseProductModel.result.breadcrumb) {
            breadcrumbList.add(breadcrumb);
        }
        return breadcrumbList;
    }

    @Override
    public void initData(@NonNull Context context) {
        Log.d(TAG, "isAfterRotate " + isAfterRotate + " init Data network params " + p.toString());
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
        if (!isAfterRotate || browseProductModel == null) {
            fetchFromNetwork(context);
        }
    }

    @Override
    public void fetchFromNetwork(Context context) {
        view.initDiscoveryTicker();
        p.breadcrumb = true;
        p.userId = SessionHandler.getLoginID(context);
        view.setLoadingProgress(true);
        switch (browseProductActivityModel.getSource()) {
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT:
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                p.source = browseProductActivityModel.getSource();
                if (SessionHandler.isV4Login(context)) {
                    p.unique_id = AuthUtil.md5(p.userId);
                } else {
                    p.unique_id = AuthUtil.md5(GCMHandler.getRegistrationId(context));
                }
                discoveryInteractor.getProducts(NetworkParam.generateNetworkParamProduct(p));
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT:
                p.unique_id = null;
                discoveryInteractor.getProducts(NetworkParam.generateNetworkParamProduct(p));
                break;
            case BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY:
                p.unique_id = null;
                discoveryInteractor.getProducts(NetworkParam.generateNetworkParamProduct(p));
                break;
        }
    }

    @Override
    public void fetchArguments(Bundle argument) {
        if (argument != null && !isAfterRotate) {
            index = argument.getInt(ProductFragment.INDEX, 0);
            browseProductActivityModel = Parcels.unwrap(argument.getParcelable(BrowseParentFragment.BROWSE_PRODUCT_ACTIVITY_MODEL));
            view.setSource(browseProductActivityModel.getSource());
            Log.d(TAG, "BROWSE_PRODUCT_ACTIVITY_MODEL " + browseProductActivityModel.toString());
            p = new NetworkParam.Product();
            switch (browseProductActivityModel.getSource()) {
                case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_CATALOG:
                case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT:
                case BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP:
                    break;
                case BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT:
                    p.source = "hot_product";
                    break;
                case BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY:
                    p.source = "directory";
                    break;
                default:
                    p.source = "other_product";
            }
            HotListBannerModel hotListBannerModel = browseProductActivityModel.getHotListBannerModel();
            if (hotListBannerModel != null) { // hot list
                this.hotListBannerModel = hotListBannerModel;
                p.sc = hotListBannerModel.query.sc;
                p.fshop = hotListBannerModel.query.fshop;
                p.ob = hotListBannerModel.query.ob;
                p.h = hotListBannerModel.query.hot_id;
                if(hotListBannerModel.query.q.equals("()")){
                    p.q = browseProductActivityModel.getQ();
                } else {
                    p.q = hotListBannerModel.query.q;
                }
                p.terms = hotListBannerModel.query.terms;
                p.negative = hotListBannerModel.query.negative_keyword;
                p.pmin = hotListBannerModel.query.pmin;
                p.pmax = hotListBannerModel.query.pmax;
                p.hashtag = true;
                p.shopId = hotListBannerModel.query.shop_id;
                if (browseProductActivityModel.getFilterOptions() != null) {
                    p.extraFilter = browseProductActivityModel.getFilterOptions();
                }
            } else {// directory page
                p.start = "0";
                p.unique_id = browseProductActivityModel.getUnique_id();
                p.sc = browseProductActivityModel.getDepartmentId();
                p.q = browseProductActivityModel.getQ();
                p.ob = browseProductActivityModel.getOb();
                p.obCatalog = browseProductActivityModel.getObCatalog();
                // from dynamic filter
                Map<String, String> filters = browseProductActivityModel.getFilterOptions();
                if (filters != null) {
                    String depId = filters.get(BrowseApi.SC);
                    p.sc = depId == null ? browseProductActivityModel.getDepartmentId() : depId;
                    p.extraFilter = filters;
                }
            }
        }
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
        discoveryInteractor = new DiscoveryInteractorImpl();
        discoveryInteractor.setDiscoveryListener(this);
        //[START] This is for demo only
//        p = NetworkParam.getDummyProduct();
        //[END] This is for demo only
    }

    @Override
    public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {
        view.setLoadingProgress(false);
    }

    @Override
    public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
        Log.e(TAG, "onFailed " + data.toString());
        isAfterRotate = false;
        view.setLoadingProgress(false);
        view.setNetworkStateError();
    }

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
        Log.d(TAG, "onSuccess type "+type);
        ArrayMap<String, String> visibleTab = new ArrayMap<>();
        switch (type) {
            case DiscoveryListener.DYNAMIC_ATTRIBUTE:
                DynamicFilterModel.DynamicFilterContainer dynamicFilterContainer = (DynamicFilterModel.DynamicFilterContainer) data.getModel2();
                view.setDynamicFilterAtrribute(dynamicFilterContainer.body().getData(), index);
                break;
            case DiscoveryListener.BROWSE_PRODUCT:
                browseProductModel = (BrowseProductModel) data.getModel2().body();
                browseProductModel.hotListBannerModel = hotListBannerModel;
                if (browseProductModel.result.redirect_url == null) {
                    // if has catalog, show three tabs
                    String source = browseProductActivityModel.getSource();
                    Log.d(TAG, "source "+source);
                    if (browseProductModel.result.hasCatalog != null && Integer.parseInt(browseProductModel.result.hasCatalog) == 1 && !source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)) {
                        visibleTab.put(BrowserSectionsPagerAdapter.PRODUK, view.VISIBLE_ON);
                        visibleTab.put(BrowserSectionsPagerAdapter.KATALOG, view.VISIBLE_ON);
                        if(browseProductActivityModel.getSource().startsWith("search")){
                            visibleTab.put(BrowserSectionsPagerAdapter.TOKO, view.VISIBLE_ON);
                        }
                        view.initSectionAdapter(visibleTab);
                        if (source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)) {
                            view.setupWithViewPager();
                        } else {
                            view.setupWithTabViewPager();
                        }
                        view.setCurrentTabs(browseProductActivityModel.getActiveTab());
                    } else if (source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)) {
                        visibleTab.put(BrowserSectionsPagerAdapter.PRODUK, view.VISIBLE_ON);
                        view.initSectionAdapter(visibleTab);
                        view.setupWithViewPager();
                    } else if (source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
                        visibleTab.put(BrowserSectionsPagerAdapter.PRODUK, view.VISIBLE_ON);
                        view.initSectionAdapter(visibleTab);
                        view.setupWithTabViewPager();
                        view.setupCategory(browseProductModel);
                    } else {
                        visibleTab.put(BrowserSectionsPagerAdapter.PRODUK, view.VISIBLE_ON);
                        visibleTab.put(BrowserSectionsPagerAdapter.TOKO, view.VISIBLE_ON);
                        view.initSectionAdapter(visibleTab);
                        view.setupWithTabViewPager();
                        if (source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_SHOP)) {
                            view.setCurrentTabs(1);
                        } else {
                            view.setCurrentTabs(0);
                        }
                    }
                    if(view.getActivityPresenter().checkHasFilterAttrIsNull(index)) {
                        discoveryInteractor.getDynamicAttribute(view.getContext(), BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT, browseProductActivityModel.getDepartmentId());
                    }
                    view.setLoadingProgress(false);
                } else {
                    view.redirectUrl(browseProductModel);
                }
                break;
            case DiscoveryListener.TOPADS:
                Log.d("MNORMANSYAH", "masuk sini gan!!");
                break;
        }
    }
}
