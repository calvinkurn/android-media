package com.tokopedia.discovery.model;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.router.discovery.BrowseProductRouter;

import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.core.network.apiservices.topads.api.TopAdsApi.SRC_BROWSE_PRODUCT;
import static com.tokopedia.core.network.apiservices.topads.api.TopAdsApi.SRC_DIRECTORY;
import static com.tokopedia.core.network.apiservices.topads.api.TopAdsApi.SRC_HOTLIST;

/**
 * Created by noiz354 on 3/23/16.
 */
public class NetworkParam {
    private static final String SEARCH_PRODUCT = "VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT";

    public static HashMap<String, String> generateHotListBannerParams(String key) {
        HashMap<String, String> result = new HashMap<>();
        result.put("key", key);
        return result;
    }

    public static class Product {
        public String device = "android";
        public String start;
        public String rows = "12";
        public String sc;
        public String floc;
        public String ob = "23";// Best match
        public String obCatalog = "23";// Best match
        public String pmin;
        public String pmax;
        public String condition;
        public String fshop;
        public boolean wholesale;
        public String q;
        public String h;
        public String id;
        public String negative;
        public String highlight;
        public String terms = "true";
        public String fq;
        public String id1;
        public String shopId;
        public String userId;
        public boolean breadcrumb = false;// only for first page
        public boolean preorder = false;
        public String imageSize = "200";
        public boolean imageSquare = true;
        public boolean hashtag = false;
        public String unique_id;
        public String shipping;
        public boolean returnable;
        public String source;
        public String keyword;
        public Map<String, String> extraFilter;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static HashMap<String, String> generateNetworkParamProduct(Product product){
        HashMap<String, String> data = new HashMap<>();
        if(product.extraFilter != null)
            data.putAll(product.extraFilter);
        data.put(BrowseApi.DEVICE, product.device);
        data.put(BrowseApi.START, product.start);
        data.put(BrowseApi.ROWS, product.rows);
        data.put(BrowseApi.SC, product.sc);
        data.put(BrowseApi.Q, product.q);
        data.put(BrowseApi.OB, product.ob);
        data.put(BrowseApi.H, product.h);
        data.put(BrowseApi.ID, product.id);// didn't use, previously for topads
        data.put(BrowseApi.NEGATIVE, product.negative);
        data.put(BrowseApi.HIGHLIGHT, product.highlight);// didn't use, previously for topads
        data.put(BrowseApi.TERMS, product.terms);
        data.put(BrowseApi.FQ, product.fq);
        data.put(BrowseApi.ID1, product.id1);
        data.put(BrowseApi.SHOP_ID, product.shopId);
        data.put(BrowseApi.USER_ID, product.userId);
        if (!product.source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
            data.put(BrowseApi.BREADCRUMB, Boolean.toString(product.breadcrumb));
        }
        data.put(BrowseApi.IMAGE_SIZE, product.imageSize);
        data.put(BrowseApi.IMAGE_SQUARE, Boolean.toString(product.imageSquare));
        data.put(BrowseApi.HASHTAG, Boolean.toString(product.hashtag));
        data.put(BrowseApi.UNIQUE_ID, product.unique_id);
        data.put(BrowseApi.SOURCE, product.source);
        if (product.source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT)) {
            data.put(BrowseApi.DEFAULT_SC, product.sc);
            data.remove(BrowseApi.SC);
        }
        if (product.source.equals(BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY)) {
            data.remove(BrowseApi.H);
            data.remove(BrowseApi.Q);
            data.remove(BrowseApi.HASHTAG);
        }
        return data;
    }

    public static HashMap<String, String> generateDummyProduct() {
        Product p = getDummyProduct();
        return generateNetworkParamProduct(p);
    }

    @NonNull
    public static Product getDummyProduct() {
        Product p = new Product();
        p.start = "12";
        p.wholesale = true;
        p.q = "samsung";
        p.breadcrumb = true;
        p.preorder = true;
        return p;
    }

    public static Shop generateDummyShop() {
        Shop shop = new Shop();
        shop.floc = "";
        shop.q = "cincin";
        shop.fshop = "0";
        shop.start = 0;

        return shop;
    }

    public static HashMap<String, String> generateShopQuery(Shop shop) {
        HashMap<String, String> data = new HashMap<>();
        data.put(BrowseApi.FLOC, shop.floc);
        data.put(BrowseApi.Q, shop.q);
        data.put(BrowseApi.FSHOP, shop.fshop);
        data.put(BrowseApi.ROWS, Integer.toString(shop.rows));
        data.put(BrowseApi.START, Integer.toString(shop.start));
        data.put(BrowseApi.DEVICE, shop.device);
        if (shop.extraFilter != null)
            data.putAll(shop.extraFilter);
        return data;
    }

    public static HashMap<String, String> generateShopQuery() {
        return generateShopQuery(generateDummyShop());
    }

    public static class Shop {
        public String floc;
        public String q;
        public String fshop;
        public int rows = 12;
        public int start;
        public String device = "android";
        public Map<String, String> extraFilter;
    }

    public static class TopAds {
        public int page;
        public String q;
        public String depId;
        public int item = 4;// size of item
        public String xDevice = "android";
        public String h;// dont know what it is
        public String src;
        public String pmin;
        public String pmax;
        public String fshop;
        public String floc;
        public boolean wholesale;
        public String shipping;
        public boolean preorder;
        public String condition;
        public boolean returnable;
        public Map<String, String> extraFilter;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static HashMap<String, String> generateTopAds(Context context, TopAds topAds) {
        HashMap<String, String> param = new HashMap<>();
        param.put(TopAdsApi.PAGE, Integer.toString(topAds.page));
        param.put(TopAdsApi.ITEM, Integer.toString(topAds.item));
        param.put(TopAdsApi.SRC, topAds.src);
        switch (topAds.src) {
            case SRC_HOTLIST:
                if (topAds.h != null && !topAds.h.equals("")) {
                    param.put(TopAdsApi.H, topAds.h);
                } else {
                    param.put(TopAdsApi.DEP_ID, topAds.depId);
                }
                break;
            case SRC_BROWSE_PRODUCT:
                param.put(TopAdsApi.Q, topAds.q);
                param.put(TopAdsApi.DEP_ID, topAds.depId);
                break;
            case SRC_DIRECTORY:
                param.put(TopAdsApi.DEP_ID, topAds.depId);
                break;
        }
        if (topAds.extraFilter != null) {
            param.putAll(topAds.extraFilter);
        }

        return param;
    }

    public static Catalog generateCatalog() {
        Catalog catalog = new Catalog();
        catalog.q = "samsung";
        catalog.start = 0;
        return catalog;
    }

    public static HashMap<String, String> generateCatalogQuery(Catalog catalog) {
        HashMap<String, String> data = new HashMap<>();
        data.put(BrowseApi.Q, catalog.q);
        data.put(BrowseApi.ROWS, Integer.toString(catalog.rows));
        data.put(BrowseApi.START, Integer.toString(catalog.start));
        data.put(BrowseApi.DEVICE, catalog.device);
        data.put(BrowseApi.SC, catalog.sc);
        data.put(BrowseApi.ID, catalog.id);
        data.put(BrowseApi.OB, catalog.ob);
        data.put(BrowseApi.PMIN, catalog.pmin);
        data.put(BrowseApi.PMAX, catalog.pmax);
        data.put(BrowseApi.TERMS, catalog.terms);
        data.put(BrowseApi.BREADCRUMB, Boolean.toString(catalog.breadcrumb));
        data.put(BrowseApi.IMAGE_SIZE, catalog.imageSize);
        data.put(BrowseApi.IMAGE_SQUARE, Boolean.toString(catalog.imageSquare));
        if (catalog.extraFilter != null)
            data.putAll(catalog.extraFilter);
        return data;
    }

    public static HashMap<String, String> generateCatalogQuery() {
        return generateCatalogQuery(generateCatalog());
    }

    public static class Catalog {
        public String device = "android";
        public int rows = 12;
        public int start;
        public String q;

        public String sc = "0";
        public String id;
        public String ob = "23";
        public String pmin;
        public String pmax;
        public String terms = "true";// klo terms itu, filter kaya brand, screen dll. kepake d directory product
        public boolean breadcrumb = false;
        public String imageSize = "200";
        public boolean imageSquare = true;
        public Map<String, String> extraFilter;
    }
}
