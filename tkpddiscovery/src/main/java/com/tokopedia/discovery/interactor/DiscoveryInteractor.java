package com.tokopedia.discovery.interactor;

import android.content.Context;

import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.network.entity.categoriesHades.Data;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
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

    void getProductWithCategory(HashMap<String, String> data, String categoryId, int level);

    void getCatalogs(HashMap<String, String> data);

    void getShops(HashMap<String, String> data);

    void getDynamicAttribute(Context context, String source, String depId);

    void setDiscoveryListener(DiscoveryListener discoveryListener);

    void getHotListBanner(HashMap<String, String> data);

    void storeCacheCategoryHeader(int level, Data categoriesHadesModel);

    Data getCategoryHeaderCache(int level);

    Observable<Map<String, Boolean>> checkProductsInWishlist(String userId, List<ProductItem> productItemList);

    void getOSBanner(String keyword);
}
