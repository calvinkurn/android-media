package com.tokopedia.discovery.interactor;

import android.content.Context;

import com.tokopedia.core.network.entity.categoriesHades.CategoryHadesModel;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.interfaces.DiscoveryListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by noiz354 on 3/17/16.
 */
public interface DiscoveryInteractor {
    void getProducts(HashMap<String, String> data);

    void getCatalogs(HashMap<String, String> data);

    void getShops(HashMap<String, String> data);

    void getDynamicAttribute(Context context, String source, String depId);

    void setDiscoveryListener(DiscoveryListener discoveryListener);

    void getHotListBanner(HashMap<String, String> data);

    void getCategoryHeader(String categoryId, int level);

    void storeCacheCategoryHeader(int level, CategoryHadesModel categoriesHadesModel);

    CategoryHadesModel getCategoryHeaderCache(int level);

    void getTopAds(HashMap<String, String> data);

    void loadSearchSuggestion(String querySearch, String unique_id, int count);

    void deleteSearchHistory(String unique_id, String keyword, boolean clear_all);

    void getOSBanner(String keyword);

    Observable<Map<String, Boolean>> checkProductsInWishlist(String userId, List<ProductItem> productItemList);
}
