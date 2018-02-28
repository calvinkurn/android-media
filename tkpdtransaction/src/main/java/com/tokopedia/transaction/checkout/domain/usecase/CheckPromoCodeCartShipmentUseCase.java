package com.tokopedia.transaction.checkout.domain.usecase;

import com.google.gson.Gson;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transaction.checkout.data.repository.ICartRepository;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartShipmentData;
import com.tokopedia.transaction.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class CheckPromoCodeCartShipmentUseCase extends UseCase<CheckPromoCodeCartShipmentResult> {
    public static final String PARAM_CARTS = "carts";
    private final ICartRepository cartRepository;
    private final IVoucherCouponMapper voucherCouponMapper;

    @Inject
    public CheckPromoCodeCartShipmentUseCase(ICartRepository cartRepository,
                                             IVoucherCouponMapper voucherCouponMapper) {
        this.cartRepository = cartRepository;
        this.voucherCouponMapper = voucherCouponMapper;
    }

    @Override
    public Observable<CheckPromoCodeCartShipmentResult> createObservable(RequestParams requestParams) {
        CheckPromoCodeCartShipmentRequest request =
                (CheckPromoCodeCartShipmentRequest) requestParams.getObject(PARAM_CARTS);
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_CARTS, new Gson().toJson(request));
        param.put("lang", "id");
        param.put("suggested", "0");
        return cartRepository.checkPromoCodeCartShipment(AuthUtil.generateParamsNetwork(
                MainApplication.getAppContext(), param
        )).map(
                new Func1<CheckPromoCodeFinalDataResponse, PromoCodeCartShipmentData>() {
                    @Override
                    public PromoCodeCartShipmentData call(
                            CheckPromoCodeFinalDataResponse checkPromoCodeFinalDataResponse
                    ) {
                        return voucherCouponMapper.convertPromoCodeCartShipmentData(
                                checkPromoCodeFinalDataResponse
                        );
                    }
                }
        ).map(
                new Func1<PromoCodeCartShipmentData, CheckPromoCodeCartShipmentResult>() {
                    @Override
                    public CheckPromoCodeCartShipmentResult call(PromoCodeCartShipmentData promoCodeCartShipmentData) {
                        return voucherCouponMapper.convertCheckPromoCodeCartShipmentResult(promoCodeCartShipmentData);
                    }
                }
        );
    }
}
