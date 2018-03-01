package com.tokopedia.transaction.checkout.domain.mapper;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CouponListResult;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.couponlist.Coupon;
import com.tokopedia.transaction.checkout.data.entity.response.couponlist.CouponDataResponse;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.CouponListData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class VoucherCouponMapper implements IVoucherCouponMapper {

    private final IMapperUtil mapperUtil;

    @Inject
    public VoucherCouponMapper(IMapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CouponListData convertCouponListData(CouponDataResponse couponDataResponse) {
        CouponListData couponListData = new CouponListData();

        List<CouponListData.Coupon> couponList = new ArrayList<>();
        for (Coupon coupon : couponDataResponse.getCoupons()) {
            couponList.add(new CouponListData.Coupon.Builder()
                    .code(coupon.getCode())
                    .description(coupon.getDescription())
                    .expired(coupon.getExpired())
                    .icon(coupon.getIcon())
                    .id(coupon.getId())
                    .imageUrl(coupon.getImageUrl())
                    .imageUrlMobile(coupon.getImageUrlMobile())
                    .promoId(coupon.getPromoId())
                    .subTitle(coupon.getSubTitle())
                    .title(coupon.getTitle())
                    .build());
        }
        couponListData.setCoupons(couponList);
        return couponListData;
    }

    @Override
    public PromoCodeCartListData convertPromoCodeCartListData(
            CheckPromoCodeCartListDataResponse dataResponse
    ) {
        PromoCodeCartListData promoCodeCartListData = new PromoCodeCartListData();
        promoCodeCartListData.setError(!mapperUtil.isEmpty(dataResponse.getError()));
        promoCodeCartListData.setErrorMessage(dataResponse.getError());

        if (!mapperUtil.isEmpty(dataResponse.getDataVoucher())) {
            PromoCodeCartListData.DataVoucher dataVoucher = new PromoCodeCartListData.DataVoucher();
            dataVoucher.setCashbackAmount(dataResponse.getDataVoucher().getCashbackAmount());
            dataVoucher.setCashbackTopCashAmount(dataResponse.getDataVoucher().getCashbackTopCashAmount());
            dataVoucher.setCashbackVoucherAmount(dataResponse.getDataVoucher().getCashbackVoucherAmount());
            dataVoucher.setCashbackVoucherDescription(dataResponse.getDataVoucher().getCashbackVoucherDescription());
            dataVoucher.setCode(dataResponse.getDataVoucher().getCode());
            dataVoucher.setDiscountAmount(dataResponse.getDataVoucher().getDiscountAmount());
            dataVoucher.setExtraAmount(dataResponse.getDataVoucher().getExtraAmount());
            dataVoucher.setGatewayId(dataResponse.getDataVoucher().getGatewayId());
            dataVoucher.setMessageSuccess(dataResponse.getDataVoucher().getMessageSuccess());
            dataVoucher.setPromoCodeId(dataResponse.getDataVoucher().getPromoCodeId());
            dataVoucher.setSaldoAmount(dataResponse.getDataVoucher().getSaldoAmount());
            dataVoucher.setToken(dataResponse.getDataVoucher().getToken());

            promoCodeCartListData.setDataVoucher(dataVoucher);
        }
        return promoCodeCartListData;
    }

    @Override
    public PromoCodeCartShipmentData convertPromoCodeCartShipmentData(CheckPromoCodeFinalDataResponse dataResponse) {
        PromoCodeCartShipmentData promoCodeCartShipmentData = new PromoCodeCartShipmentData();
        promoCodeCartShipmentData.setError(!mapperUtil.isEmpty(dataResponse.getError()));
        promoCodeCartShipmentData.setErrorMessage(dataResponse.getError());
        if (!mapperUtil.isEmpty(dataResponse.getDataVoucher())) {
            PromoCodeCartShipmentData.DataVoucher dataVoucher = new PromoCodeCartShipmentData.DataVoucher();
            dataVoucher.setVoucherAmount(dataResponse.getDataVoucher().getVoucherAmount());
            dataVoucher.setVoucherAmountIdr(dataResponse.getDataVoucher().getVoucherAmountIdr());
            dataVoucher.setVoucherNoOtherPromotion(dataResponse.getDataVoucher().getVoucherNoOtherPromotion());
            dataVoucher.setVoucherStatus(dataResponse.getDataVoucher().getVoucherStatus());
            dataVoucher.setVoucherPromoDesc(dataResponse.getDataVoucher().getVoucherPromoDesc());
            promoCodeCartShipmentData.setDataVoucher(dataVoucher);
        }
        return promoCodeCartShipmentData;
    }

    @Override
    public CouponListResult convertCouponListResult(CouponListData couponListData) {
        CouponListResult couponListResult = new CouponListResult();
        List<CouponListResult.Coupon> couponList = new ArrayList<>();
        for (CouponListData.Coupon coupon : couponListData.getCoupons()) {
            couponList.add(
                    new CouponListResult.Coupon.Builder()
                            .code(coupon.getCode())
                            .description(coupon.getDescription())
                            .expired(coupon.getExpired())
                            .icon(coupon.getIcon())
                            .id(coupon.getId())
                            .imageUrl(coupon.getImageUrl())
                            .imageUrlMobile(coupon.getImageUrl())
                            .promoId(coupon.getPromoId())
                            .subTitle(coupon.getSubTitle())
                            .title(coupon.getTitle())
                            .build()
            );
        }
        couponListResult.setCoupons(couponList);
        return couponListResult;
    }

    @Override
    public CheckPromoCodeCartListResult convertCheckPromoCodeCartListResult(
            PromoCodeCartListData promoCodeCartListData
    ) {
        CheckPromoCodeCartListResult checkPromoCodeCartListResult = new CheckPromoCodeCartListResult();
        checkPromoCodeCartListResult.setError(promoCodeCartListData.isError());
        checkPromoCodeCartListResult.setErrorMessage(promoCodeCartListData.getErrorMessage());
        if (!mapperUtil.isEmpty(promoCodeCartListData.getDataVoucher())) {
            CheckPromoCodeCartListResult.DataVoucher dataVoucher = new CheckPromoCodeCartListResult.DataVoucher();
            dataVoucher.setCashbackTopCashAmount(promoCodeCartListData.getDataVoucher().getCashbackTopCashAmount());
            dataVoucher.setCashbackAmount(promoCodeCartListData.getDataVoucher().getCashbackAmount());
            dataVoucher.setCashbackVoucherAmount(promoCodeCartListData.getDataVoucher().getCashbackVoucherAmount());
            dataVoucher.setCashbackVoucherDescription(promoCodeCartListData.getDataVoucher().getCashbackVoucherDescription());
            dataVoucher.setCode(promoCodeCartListData.getDataVoucher().getCode());
            dataVoucher.setDiscountAmount(promoCodeCartListData.getDataVoucher().getDiscountAmount());
            dataVoucher.setExtraAmount(promoCodeCartListData.getDataVoucher().getExtraAmount());
            dataVoucher.setGatewayId(promoCodeCartListData.getDataVoucher().getGatewayId());
            dataVoucher.setMessageSuccess(promoCodeCartListData.getDataVoucher().getMessageSuccess());
            dataVoucher.setPromoCodeId(promoCodeCartListData.getDataVoucher().getPromoCodeId());
            dataVoucher.setSaldoAmount(promoCodeCartListData.getDataVoucher().getSaldoAmount());
            dataVoucher.setToken(promoCodeCartListData.getDataVoucher().getToken());
            checkPromoCodeCartListResult.setDataVoucher(dataVoucher);
        }
        return checkPromoCodeCartListResult;
    }

    @Override
    public CheckPromoCodeCartShipmentResult convertCheckPromoCodeCartShipmentResult(
            PromoCodeCartShipmentData promoCodeCartShipmentData
    ) {
        CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult = new CheckPromoCodeCartShipmentResult();
        checkPromoCodeCartShipmentResult.setError(promoCodeCartShipmentData.isError());
        checkPromoCodeCartShipmentResult.setErrorMessage(promoCodeCartShipmentData.getErrorMessage());
        if(!mapperUtil.isEmpty(promoCodeCartShipmentData.getDataVoucher())){
            CheckPromoCodeCartShipmentResult.DataVoucher dataVoucher =
                    new CheckPromoCodeCartShipmentResult.DataVoucher();
            dataVoucher.setVoucherAmount(promoCodeCartShipmentData.getDataVoucher().getVoucherAmount());
            dataVoucher.setVoucherAmountIdr(promoCodeCartShipmentData.getDataVoucher().getVoucherAmountIdr());
            dataVoucher.setVoucherNoOtherPromotion(promoCodeCartShipmentData.getDataVoucher().getVoucherNoOtherPromotion());
            dataVoucher.setVoucherPromoDesc(promoCodeCartShipmentData.getDataVoucher().getVoucherPromoDesc());
            dataVoucher.setVoucherStatus(promoCodeCartShipmentData.getDataVoucher().getVoucherStatus());
            checkPromoCodeCartShipmentResult.setDataVoucher(dataVoucher);
        }
        return checkPromoCodeCartShipmentResult;
    }
}
