package com.tokopedia.discovery.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.discovery.BrowseShopModel;
import com.tokopedia.core.network.entity.topads.TopAds;
import com.tokopedia.core.network.entity.topads.TopAdsResponse;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.discovery.fragment.ProductFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.ErrorContainer;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.model.ProductModel;
import com.tokopedia.discovery.util.PagingHandlerUtil;
import com.tokopedia.discovery.view.FragmentBrowseProductView;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noiz354 on 3/24/16.
 */
public class FragmentDiscoveryPresenterImpl extends FragmentDiscoveryPresenter implements DiscoveryListener {

    String TAG = FragmentDiscoveryPresenterImpl.class.getSimpleName();
    DiscoveryInteractor discoveryInteractor;
    WeakReference<Context> context;
    private List<ProductItem> currTopAdsItem;
    private BrowseProductModel browseProductModel;
    int spanCount;
    private int topAdsPaging = 1;

    public FragmentDiscoveryPresenterImpl(FragmentBrowseProductView view) {
        super(view);
    }


    @Override
    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    private void checkTAG() {
        if (TAG == null || TAG.equals(""))
            throw new RuntimeException(getMessageTAG() + "need supply TAG !!!");

    }

    @Override
    public void loadMore(Context context) {
        checkTAG();
        this.context = new WeakReference<Context>(context);

        switch (TAG) {
            case ProductFragment.TAG:
                int startIndexForQuery = view.getStartIndexForQuery(TAG);
                if (context != null && context instanceof DiscoveryActivityPresenter) {

                    NetworkParam.Product productParam = ((DiscoveryActivityPresenter) context).getProductParam();

                    Log.d(TAG, "Product Params " + productParam.toString());
                    if (productParam == null)
                        return;

                    productParam.start = Integer.toString(startIndexForQuery);
                    productParam.breadcrumb = false;
                    discoveryInteractor.getProducts(NetworkParam.generateNetworkParamProduct(productParam));
                }

                break;
            default:
                Log.i(TAG, getMessageTAG() + " not implemented yet for this TAG");
                break;
        }
    }

    @Override
    public void getTopAds(int page, String TAG, Context context) {
        checkTAG();
        this.context = new WeakReference<Context>(context);
        switch (TAG) {
            case ProductFragment.TAG:
                int startIndexForQuery = view.getStartIndexForQuery(TAG);
                if (context != null && context instanceof DiscoveryActivityPresenter) {

                    NetworkParam.Product productParam = ((DiscoveryActivityPresenter) context).getProductParam();

                    Log.d(TAG, getMessageTAG() + productParam);
                    if (productParam == null)
                        return;

                    NetworkParam.TopAds topAds = new NetworkParam.TopAds();
                    topAds.page = (topAdsPaging * 2) - 1;
                    topAds.q = productParam.q;
                    topAds.depId = productParam.sc;
                    topAds.h = productParam.h;
                    if(productParam.extraFilter != null){
                        topAds.extraFilter = productParam.extraFilter;
                    }
                    if(context != null && context instanceof DiscoveryActivityPresenter){
                        topAds.src = ((DiscoveryActivityPresenter) context).getBrowseProductActivityModel().getAdSrc();
                    } else {
                        topAds.src = TopAdsApi.SRC_BROWSE_PRODUCT;
                    }

                    Log.d(TAG, "getTopAds page "+page);
                    if(spanCount < 3) {// spanCount 1 and 2
                        if(currTopAdsItem == null || currTopAdsItem.isEmpty()){
                            HashMap<String, String> topAdsParam = NetworkParam.generateTopAds(context, topAds);
                            discoveryInteractor.getTopAds(topAdsParam);
//                        fetchTopAds(page);
                        } else {
                            List<ProductItem> passProduct = new ArrayList<>();
                            int counter = 2;
                            while(counter != 0 && !currTopAdsItem.isEmpty()) {
                                if (currTopAdsItem.get(0) != null) {
                                    ProductItem productItem = currTopAdsItem.remove(0);
                                    passProduct.add(productItem);
                                    counter --;
                                }
                            }
                            view.addTopAds(passProduct, page, TAG);
                        }
                    }else{
                        HashMap<String, String> topAdsParam = NetworkParam.generateTopAds(context, topAds);
                        discoveryInteractor.getTopAds(topAdsParam);
                    }
                }
                break;
            default:
                Log.i(TAG, getMessageTAG() + " not implemented yet for this TAG");
                break;
        }

    }

    @Override
    public void getTopAds(int page, String TAG, Context context, int spanCount) {
        this.spanCount = spanCount;
        getTopAds(page, TAG, context);
    }

    @Override
    public void sendGTMNoResult(Context context) {
        BrowseProductModel browseProductModel = ((DiscoveryActivityPresenter) context).getDataForBrowseProduct(!isAfterRotate);
        if (browseProductModel != null) {
            if (browseProductModel.result.products == null || browseProductModel.result.products.length == 0) {
                NetworkParam.Product params = ((DiscoveryActivityPresenter) context).getProductParam();
                if (params != null) {
                    UnifyTracking.eventDiscoveryNoResult(params.q);
                }
            }
        }
    }

    @Override
    public void getCategoryHeader(String categoryId) {

    }

    @Override
    public String getMessageTAG() {
        return FragmentDiscoveryPresenterImpl.class.getSimpleName();
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return getMessageTAG() + " : ";
    }

    @Override
    public void initData(@NonNull Context context) {
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);

        switch (TAG) {
            case ProductFragment.TAG:
                // below indicate for the first time
                if (!isAfterRotate) {
                    view.setupRecyclerView();
                    if (context != null && context instanceof DiscoveryActivityPresenter) {
                        browseProductModel = ((DiscoveryActivityPresenter) context).getDataForBrowseProduct(!isAfterRotate);

                        Log.d(TAG, getMessageTAG() + browseProductModel);
                        if (browseProductModel == null || browseProductModel.result.products == null)
                            return;

                        Pair<List<ProductItem>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseProductModel(browseProductModel);
                        HotListBannerModel hotListBannerModel = browseProductModel.hotListBannerModel;
                        if (hotListBannerModel != null) {
                            view.addHotListHeader(new ProductAdapter.HotListBannerModel(hotListBannerModel, browseProductModel.result.hashtag));
                            processBrowseProductLoadMore(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                        } else {
                            processBrowseProduct(browseProductModel.header.getTotalData(),listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                        }
                        sendGTMNoResult(context);
                    }
                }
                break;
            default: {

            }
            break;
        }
    }

    public BrowseProductModel getBrowseProductModel() {
        return browseProductModel;
    }

    private Pair<List<ProductItem>, PagingHandler.PagingHandlerModel> parseBrowseProductModel(BrowseProductModel browseProductModel) {
        List<ProductItem> productItems = BrowseProductModel.Products.toProductItemList(browseProductModel.result.products);

        PagingHandler.PagingHandlerModel pagingHandlerModel = getPagingHandlerModel(browseProductModel.result.paging.getUriNext(), browseProductModel.result.paging.getUriPrevious());

        return new Pair<>(productItems, pagingHandlerModel);
    }

    @NonNull
    public static PagingHandler.PagingHandlerModel getPagingHandlerModel(String uriNext, String uriPrevious) {
        String start = "-1";
        try {
            String temp = PagingHandlerUtil.getCertainKeyword(uriNext, "start");
            if (temp != null) {
                start = temp;
            }
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
            Log.e("STUART", "error parse" + e.getMessage());
        }
        PagingHandler.PagingHandlerModel pagingHandlerModel = new PagingHandler.PagingHandlerModel();
        pagingHandlerModel.setStartIndex(Integer.parseInt(start));
        pagingHandlerModel.setUriNext(uriNext);
        pagingHandlerModel.setUriPrevious(uriPrevious);
        return pagingHandlerModel;
    }

    public void processBrowseProduct(Long totalProduct, List<ProductItem> productItems, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        view.onCallProductServiceResult2(totalProduct, productItems, pagingHandlerModel);
    }

    public void processBrowseProductLoadMore(List<ProductItem> productItems, PagingHandler.PagingHandlerModel pagingHandlerModel) {
        view.onCallProductServiceLoadMore(productItems, pagingHandlerModel);
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        view.restorePaging(argument);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        view.savePaging(argument);
    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotate) {
            view.setupAdapter();
        }
        discoveryInteractor = new DiscoveryInteractorImpl();
        discoveryInteractor.setDiscoveryListener(this);
    }

    @Override
    public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

    }

    @Override
    public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
        Log.e(TAG, "onFailed " + data.toString() + " type " + type);
        switch (type) {
            case DiscoveryListener.TOPADS:
                break;
            default:
                view.ariseRetry(type, ((ErrorContainer) data.getModel2()).body().getMessage());
                break;
        }
    }

    //berantakan banget, tiap case dibikin fungsi masing masing biar lebih rapi lah -rico-

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
        switch (type) {
            case DiscoveryListener.BROWSE_PRODUCT:
                Context context = this.context.get();
                if (context != null && context instanceof DiscoveryActivityPresenter) {
                    BrowseProductModel.BrowseProductContainer temp = (BrowseProductModel.BrowseProductContainer) data.getModel2();
                    browseProductModel = temp.body();
                    Pair<List<ProductItem>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseProductModel(browseProductModel);
                    processBrowseProductLoadMore(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                }
                break;
            case DiscoveryListener.BROWSE_SHOP:
                BrowseShopModel.BrowseShopContainer browseShopContainer = (BrowseShopModel.BrowseShopContainer) data.getModel2();
                BrowseShopModel browseShopModel = browseShopContainer.body();

                Log.d(TAG, getMessageTAG() + browseShopModel);
                if (browseShopModel == null)
                    return;

                List<ProductModel> result = new ArrayList<>();
                for (BrowseShopModel.Shops temp : browseShopModel.result.shops) {
                    ProductModel productModel = new ProductModel();
                    productModel.productImage = temp.shopImage;
                    productModel.productName = temp.shopName;
                    productModel.productPrice = temp.shopId;
                    productModel.shopName = temp.shopName;

                    // add to collection
                    result.add(productModel);
                }

                break;
            case DiscoveryListener.BROWSE_CATALOG:

                break;
            case DiscoveryListener.TOPADS:
                Log.i(TAG, getMessageTAG() + " fetch top ads " + data.getModel1());
                ObjContainer model2 = data.getModel2();
                topAdsPaging ++;
                if(model2 instanceof TopAdsResponse.TopAdsContainer){

                    TopAdsResponse topAdsResponse = (TopAdsResponse) model2.body();
                    int page = view.getPage(ProductFragment.TAG) - 1;

                    if (spanCount < 3) {// spanCount 1 & 2

                        currTopAdsItem = new ArrayList<>();
                        List<TopAdsResponse.Data> topAdsData = topAdsResponse.data;
                        for (int i = 0; i < topAdsData.size(); i++) {
                            TopAdsResponse.Data dataTopAds = topAdsData.get(i);
                            ProductItem topads = convertToProductItem(dataTopAds);
                            topads.setTopAds(TopAds.from(dataTopAds));
                            currTopAdsItem.add(topads);
                        }

                        List<ProductItem> passProduct = new ArrayList<>();

                        int counter = 2;
                        while(counter != 0 && !currTopAdsItem.isEmpty()) {
                            passProduct.add(currTopAdsItem.remove(0));
                            counter --;
                        }

                        view.addTopAds(passProduct, page, TAG);
                    } else {
                        List<ProductItem> topAds = new ArrayList<>();
                        int count = 0, max = 4;
                        A:
                        for (TopAdsResponse.Data topAdsData :
                                topAdsResponse.data) {
                            if (count < max) {
                                ProductItem topads = convertToProductItem(topAdsData);
                                topads.setTopAds(TopAds.from(topAdsData));
                                topAds.add(topads);
                            } else {
                                break A;
                            }
                            count++;
                        }
                        view.addTopAds(topAds, page, TAG);
                    }

                }
                break;
            default:

                break;
        }
    }

    private ProductItem convertToProductItem(TopAdsResponse.Data data) {
        ProductItem product = new ProductItem();
        product.setId(data.product.id);
        product.setPrice(data.product.priceFormat);
        product.setName(data.product.name);
        product.setShopId(Integer.parseInt(data.shop.id));
        product.setImgUri(data.product.image.mUrl);
        product.setShop(data.shop.name);
        product.setIsGold(data.shop.goldShop ? "1" : "0");
        product.setLuckyShop(data.shop.luckyShop);
        product.setWholesale(data.product.wholesalePrice.size() > 0 ? "1" : "0");
        product.setPreorder((data.product.preorder) ? "1" : "0");
        product.setIsTopAds(true);
        product.setShopLocation(data.shop.location);
        product.setBadges(data.shop.badges);
        product.setLabels(data.product.labels);
        TopAds ads = new TopAds();
        ads.setId(data.id);
        ads.setAdRefKey(data.adRefKey);
        ads.setProductClickUrl(data.productClickUrl);
        ads.setRedirect(data.redirect);
        ads.setShopClickUrl(data.shopClickUrl);
        ads.setStickerId(data.stickerId);
        ads.setStickerImage(data.stickerId);
        product.setTopAds(ads);
        return product;
    }
}
