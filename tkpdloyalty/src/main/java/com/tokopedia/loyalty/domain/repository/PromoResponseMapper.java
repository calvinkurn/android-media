package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.loyalty.domain.entity.response.promo.Children;
import com.tokopedia.loyalty.domain.entity.response.promo.MenuPromoResponse;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoResponse;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.data.PromoSubMenuData;

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
            PromoMenuData promoMenuData = new PromoMenuData();
            promoMenuData.setTitle(menuPromoResponse.getTitle());
            promoMenuData.setMenuId(String.valueOf(menuPromoResponse.getIdMenu()));
            promoMenuData.setIconActive(menuPromoResponse.getIcon());
            promoMenuData.setIconNormal(menuPromoResponse.getIconOff());

            List<PromoSubMenuData> promoSubMenuDataList = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            for (Children children : menuPromoResponse.getChildrenList()) {
                PromoSubMenuData promoSubMenuData = new PromoSubMenuData();
                promoSubMenuData.setId(String.valueOf(children.getSubCategory().getTermId()));
                promoSubMenuData.setTitle(children.getSubCategory().getName());
                promoSubMenuDataList.add(promoSubMenuData);
                stringBuilder.append(children.getSubCategory().getTermId()).append(",");
            }
            String allCategoryIds = stringBuilder.toString();
            promoMenuData.setAllSubCategoryId(allCategoryIds);
            promoMenuData.setPromoSubMenuDataList(promoSubMenuDataList);
            promoMenuDataList.add(promoMenuData);
        }
        return promoMenuDataList;
    }
}
