package com.tokopedia.seller.seller.info.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel;
import com.tokopedia.seller.seller.info.data.source.SellerInfoApi;
import com.tokopedia.seller.shopscore.data.common.GetData;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoDataSource {
    private SellerInfoApi sellerInfoApi;

    @Inject
    public SellerInfoDataSource(SellerInfoApi sellerInfoApi) {
        this.sellerInfoApi = sellerInfoApi;
    }

    public Observable<ResponseSellerInfoModel> getSellerInfoList(RequestParams requestParams){
        return sellerInfoApi.listSellerInfo(requestParams.getParamsAllValueInString())
                .map(new GetData<ResponseSellerInfoModel>());
    }
}
