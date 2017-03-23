package com.tokopedia.discovery.interactor;

import android.content.Context;

import com.tokopedia.core.network.entity.categoriesHades.CategoriesHadesModel;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.discovery.interfaces.DiscoveryListener;

import java.util.HashMap;

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
    void storeCacheCategoryHeader(int level, CategoriesHadesModel categoriesHadesModel);
    void getTopAds(HashMap<String, String> data);
    void loadSearchSuggestion(String querySearch, String unique_id, int count);
    void deleteSearchHistory(String unique_id, String keyword, boolean clear_all);
}
