package com.tokopedia.seller.topads.dashboard.data.repository;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.dashboard.domain.model.ProductListDomain;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/20/17.
 */

public class TopAdsSearchProductRepositoryImpl implements TopAdsSearchProductRepository {

    private CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource;
    private Context context;

    public TopAdsSearchProductRepositoryImpl(Context context,
            CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource) {
        this.context = context;
        this.cloudTopAdsSearchProductDataSource = cloudTopAdsSearchProductDataSource;
    }

    @Override
    public Observable<ProductListDomain> searchProduct(Map<String, String> param) {
        param.put(TopAdsNetworkConstant.PARAM_SHOP_ID, SessionHandler.getShopID(context));
        return cloudTopAdsSearchProductDataSource.searchProduct(param);
    }
}
