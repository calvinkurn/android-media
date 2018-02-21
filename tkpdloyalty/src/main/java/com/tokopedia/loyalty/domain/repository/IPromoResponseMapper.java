package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.promo.MenuPromoResponse;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoResponse;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public interface IPromoResponseMapper {

    List<PromoData> convertPromoDataList(List<PromoResponse> promoResponseList);

    List<PromoMenuData> convertPromoMenuDataList(List<MenuPromoResponse> menuPromoResponseList);
}
