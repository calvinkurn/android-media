package com.tokopedia.seller.seller.info.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel;

import rx.Observable;

/**
 * Created by normansyahputa on 12/5/17.
 */

public interface SellerInfoRepository {
    Observable<ResponseSellerInfoModel> getSellerInfoList(RequestParams requestParams);
}
