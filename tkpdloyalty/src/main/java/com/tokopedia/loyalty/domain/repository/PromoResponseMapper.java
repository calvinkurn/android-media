package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.promo.MenuPromoResponse;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoResponse;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoResponseMapper implements IPromoResponseMapper {

    @Inject
    public PromoResponseMapper() {
    }

    @Override
    public List<PromoData> convertPromoDataList(List<PromoResponse> promoResponseList) {
        List<PromoData> promoDataList = new ArrayList<>();
        for (PromoResponse promoResponse : promoResponseList) {
            promoDataList.add(new PromoData());
        }
        return promoDataList;
    }

    @Override
    public List<PromoMenuData> convertPromoMenuDataList(List<MenuPromoResponse> menuPromoResponseList) {
        List<PromoMenuData> promoMenuDataList = new ArrayList<>();
        for (MenuPromoResponse menuPromoResponse : menuPromoResponseList) {
            promoMenuDataList.add(new PromoMenuData());
        }
        return promoMenuDataList;
    }
}
