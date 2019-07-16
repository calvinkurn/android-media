package com.tokopedia.seller.seller.info.data.repository;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoRepositoryImpl implements SellerInfoRepository {
    SellerInfoDataSource sellerInfoDataSource;

    public SellerInfoRepositoryImpl(SellerInfoDataSource sellerInfoDataSource) {
        this.sellerInfoDataSource = sellerInfoDataSource;
    }

    @Override
    public Observable<ResponseSellerInfoModelA> getSellerInfoList(RequestParams requestParams){
        return sellerInfoDataSource.getSellerInfoList(requestParams);
    }
}
