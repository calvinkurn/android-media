package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.view.data.CouponData;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public interface ITokoPointResponseMapper {

    String sampleMapper(TokoPointResponse tokoplusResponse);

    List<CouponData> convertCouponListData(CouponListDataResponse couponListDataResponse);

    TokoPointDrawerData convertTokoplusPointDrawer(TokoPointDrawerDataResponse tokoplusPointDrawerData);
}
