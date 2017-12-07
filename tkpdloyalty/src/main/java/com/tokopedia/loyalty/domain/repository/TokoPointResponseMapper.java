package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.core.drawer2.data.viewmodel.TopPointDrawerData;
import com.tokopedia.loyalty.domain.entity.response.Coupon;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.ValidateRedeemCouponResponse;
import com.tokopedia.loyalty.view.data.CouponData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoPointResponseMapper implements ITokoPointResponseMapper {

    @Inject
    public TokoPointResponseMapper() {
    }

    @Override
    public String sampleMapper(TokoPointResponse tokoplusResponse) {
        return tokoplusResponse.getStrResponse();
    }

    @Override
    public List<CouponData> convertCouponListData(CouponListDataResponse couponListDataResponse) {
        List<CouponData> couponDataList = new ArrayList<>();
        for (Coupon coupon : couponListDataResponse.getCoupons()) {
            couponDataList.add(
                    new CouponData.Builder()
                            .id(coupon.getId())
                            .promoId(coupon.getPromoId())
                            .code(coupon.getCode())
                            .title(coupon.getTitle())
                            .subTitle(coupon.getSubTitle())
                            .description(coupon.getDescription())
                            .expired(coupon.getExpired())
                            .imageUrl(coupon.getImageUrl())
                            .imageUrlMobile(coupon.getImageUrlMobile())
                            .build()
            );
        }
        return couponDataList;
    }

    @Override
    public TopPointDrawerData convertTokoplusPointDrawer(TokoPointDrawerDataResponse tokoplusPointDrawerData) {
        TopPointDrawerData.Catalog catalog = new TopPointDrawerData.Catalog();
        TopPointDrawerData.PopUpNotif popUpNotif = new TopPointDrawerData.PopUpNotif();
        TopPointDrawerData.UserTier userTier = new TopPointDrawerData.UserTier();
        TopPointDrawerData tokoPointDrawerData = new TopPointDrawerData();
        tokoPointDrawerData.setHasNotif(tokoplusPointDrawerData.getHasNotif());
        tokoPointDrawerData.setOffFlag(tokoplusPointDrawerData.getOffFlag());
        if (tokoplusPointDrawerData.getUserTier() != null) {
            userTier.setRewardPoints(tokoplusPointDrawerData.getUserTier().getRewardPoints());
            userTier.setTierId(tokoplusPointDrawerData.getUserTier().getTierId());
            userTier.setTierName(tokoplusPointDrawerData.getUserTier().getTierName());
            userTier.setTierImageUrl(tokoplusPointDrawerData.getUserTier().getTierImageUrl());
            tokoPointDrawerData.setUserTier(userTier);
        } else {
            tokoPointDrawerData.setUserTier(null);
        }

        if (tokoplusPointDrawerData.getPopUpNotif() != null) {
            popUpNotif.setAppLink(tokoplusPointDrawerData.getPopUpNotif().getAppLink());
            popUpNotif.setButtonText(tokoplusPointDrawerData.getPopUpNotif().getButtonText());
            popUpNotif.setButtonUrl(tokoplusPointDrawerData.getPopUpNotif().getButtonUrl());
            popUpNotif.setImageUrl(tokoplusPointDrawerData.getPopUpNotif().getImageUrl());
            popUpNotif.setNotes(tokoplusPointDrawerData.getPopUpNotif().getNotes());
            popUpNotif.setText(tokoplusPointDrawerData.getPopUpNotif().getText());
            popUpNotif.setTitle(tokoplusPointDrawerData.getPopUpNotif().getTitle());
            if (tokoplusPointDrawerData.getPopUpNotif().getCatalog() != null) {
                catalog.setPoints(tokoplusPointDrawerData.getPopUpNotif().getCatalog().getPoints());
                catalog.setSubTitle(tokoplusPointDrawerData.getPopUpNotif().getCatalog().getSubTitle());
                catalog.setThumbnailUrl(tokoplusPointDrawerData.getPopUpNotif().getCatalog().getThumbnailUrl());
                catalog.setThumbnailUrlMobile(tokoplusPointDrawerData.getPopUpNotif().getCatalog().getThumbnailUrlMobile());
                catalog.setTitle(tokoplusPointDrawerData.getPopUpNotif().getCatalog().getTitle());
                popUpNotif.setCatalog(catalog);
            } else {
                popUpNotif.setCatalog(null);
            }
            tokoPointDrawerData.setPopUpNotif(popUpNotif);
        } else {
            tokoPointDrawerData.setPopUpNotif(null);
        }
        return tokoPointDrawerData;
    }

    @Override
    public String getSuccessValidateRedeemMessage(ValidateRedeemCouponResponse response) {
        return response.getMessageSuccess();
    }
}
