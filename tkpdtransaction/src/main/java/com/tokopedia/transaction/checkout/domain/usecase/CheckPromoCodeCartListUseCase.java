package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transaction.checkout.data.repository.ICartRepository;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.domain.mapper.IVoucherCouponMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class CheckPromoCodeCartListUseCase extends UseCase<CheckPromoCodeCartListResult> {

    private final ICartRepository cartRepository;
    private final IVoucherCouponMapper voucherCouponMapper;

    @Inject
    public CheckPromoCodeCartListUseCase(ICartRepository cartRepository,
                                         IVoucherCouponMapper voucherCouponMapper) {
        this.cartRepository = cartRepository;
        this.voucherCouponMapper = voucherCouponMapper;
    }

    public static final String PARAM_PROMO_CODE = "promo_code";

    @Override
    public Observable<CheckPromoCodeCartListResult> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_PROMO_CODE, requestParams.getString(PARAM_PROMO_CODE, ""));
        param.put("lang", "id");
        param.put("suggested", "0");
        return cartRepository.checkPromoCodeCartList(AuthUtil.generateParamsNetwork(
                MainApplication.getAppContext(), param)
        ).map(
                new Func1<CheckPromoCodeCartListDataResponse, PromoCodeCartListData>() {
                    @Override
                    public PromoCodeCartListData call(
                            CheckPromoCodeCartListDataResponse checkPromoCodeCartListDataResponse
                    ) {
                        return voucherCouponMapper.convertPromoCodeCartListData(
                                checkPromoCodeCartListDataResponse
                        );
                    }
                }
        ).map(
                new Func1<PromoCodeCartListData, CheckPromoCodeCartListResult>() {
                    @Override
                    public CheckPromoCodeCartListResult call(PromoCodeCartListData promoCodeCartListData) {
                        return voucherCouponMapper.convertCheckPromoCodeCartListResult(promoCodeCartListData);
                    }
                });
    }
}
