package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.loyalty.domain.entity.response.Coupon;
import com.tokopedia.loyalty.domain.entity.response.CouponListDataResponse;
import com.tokopedia.loyalty.domain.entity.response.DigitalVoucherData;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;
import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;
import com.tokopedia.loyalty.domain.entity.response.ValidateRedeemCouponResponse;
import com.tokopedia.loyalty.domain.entity.response.VoucherResponse;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

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
                            .icon(coupon.getIcon())
                            .build()
            );
        }
        return couponDataList;
    }

    @Override
    public TokoPointDrawerData convertTokoplusPointDrawer(TokoPointDrawerDataResponse tokoplusPointDrawerData) {
        TokoPointDrawerData.Catalog catalog = new TokoPointDrawerData.Catalog();
        TokoPointDrawerData.PopUpNotif popUpNotif = new TokoPointDrawerData.PopUpNotif();
        TokoPointDrawerData.UserTier userTier = new TokoPointDrawerData.UserTier();
        TokoPointDrawerData tokoPointDrawerData = new TokoPointDrawerData();
        tokoPointDrawerData.setHasNotif(tokoplusPointDrawerData.getHasNotif());
        tokoPointDrawerData.setOffFlag(tokoplusPointDrawerData.getOffFlag());
        tokoPointDrawerData.setMainPageUrl(tokoplusPointDrawerData.getMainPageUrl());
        tokoPointDrawerData.setMainPageTitle(tokoplusPointDrawerData.getMainPageTitle());
        if (tokoplusPointDrawerData.getUserTier() != null) {
            userTier.setRewardPoints(tokoplusPointDrawerData.getUserTier().getRewardPoints());
            userTier.setRewardPointsStr(tokoplusPointDrawerData.getUserTier().getRewardPointsStr());
            userTier.setTierId(tokoplusPointDrawerData.getUserTier().getTierId());
            userTier.setTierName(tokoplusPointDrawerData.getUserTier().getTierName());
            userTier.setTierNameDesc(tokoplusPointDrawerData.getUserTier().getTierNameDesc());
            userTier.setTierImageUrl(tokoplusPointDrawerData.getUserTier().getTierImageUrl());
            userTier.setMainPageUrl(tokoplusPointDrawerData.getMainPageUrl());
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
    public VoucherViewModel voucherViewModel(VoucherResponse voucherResponse, String voucherCode) {
        VoucherViewModel viewModel = new VoucherViewModel();
        viewModel.setAmount(voucherResponse.getVoucher().getVoucherAmountIdr());
        viewModel.setMessage(voucherResponse.getVoucher().getVoucherPromoDesc());
        viewModel.setCode(voucherCode);
        return viewModel;
    }

    @Override
    public CouponViewModel couponViewModel(VoucherResponse voucherResponse, String voucherCode, String couponTitle) {
        CouponViewModel viewModel = new CouponViewModel();
        viewModel.setAmount(voucherResponse.getVoucher().getVoucherAmountIdr());
        viewModel.setMessage(voucherResponse.getVoucher().getVoucherPromoDesc());
        viewModel.setCode(voucherCode);
        viewModel.setTitle(couponTitle);
        return viewModel;
    }

    @Override
    public VoucherViewModel digtialVoucherViewModel(DigitalVoucherData voucherResponse,
                                                    String voucherCode) {
        VoucherViewModel viewModel = new VoucherViewModel();
        viewModel.setAmount(voucherResponse.getAttributes().getDiscountAmount());
        viewModel.setMessage(voucherResponse.getAttributes().getMessage());
        viewModel.setCode(voucherCode);
        viewModel.setRawCashback(voucherResponse.getAttributes().getCashbackAmountPlain());
        viewModel.setRawDiscount(voucherResponse.getAttributes().getDiscountAmountPlain());
        return viewModel;
    }

    @Override
    public CouponViewModel digitalCouponViewModel(DigitalVoucherData voucherResponse,
                                                  String voucherCode, String couponTitle) {
        CouponViewModel viewModel = new CouponViewModel();
        viewModel.setAmount(voucherResponse.getAttributes().getDiscountAmount());
        viewModel.setMessage(voucherResponse.getAttributes().getMessage());
        viewModel.setCode(voucherCode);
        viewModel.setTitle(couponTitle);
        viewModel.setRawCashback(voucherResponse.getAttributes().getCashbackAmountPlain());
        viewModel.setRawDiscount(voucherResponse.getAttributes().getDiscountAmountPlain());
        return viewModel;
    }

    @Override
    public String getSuccessValidateRedeemMessage(ValidateRedeemCouponResponse response) {
        return response.getMessageSuccess();
    }
}
