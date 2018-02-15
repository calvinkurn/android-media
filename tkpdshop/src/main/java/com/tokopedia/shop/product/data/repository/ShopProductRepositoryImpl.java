package com.tokopedia.shop.product.data.repository;

import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductRepositoryImpl implements ShopProductRepository {

    private final ShopProductCloudDataSource shopProductCloudDataSource;

    @Inject
    public ShopProductRepositoryImpl(ShopProductCloudDataSource shopProductCloudDataSource) {
        this.shopProductCloudDataSource = shopProductCloudDataSource;
    }

    @Override
    public Observable<ShopNoteDetail> getShopProductList(HashMap<String, Object> hashMap, boolean shopClosed) {
        return null;
    }
}
