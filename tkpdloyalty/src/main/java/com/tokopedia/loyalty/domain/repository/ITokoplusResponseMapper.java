package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoplusResponse;
import com.tokopedia.loyalty.view.data.CouponData;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface ITokoplusResponseMapper {

    String sampleMapper(TokoplusResponse tokoplusResponse);

    List<CouponData> convertCouponListData(CouponListDataResponse couponListDataResponse);
}
