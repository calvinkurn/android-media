package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CouponListResult;
import com.tokopedia.transaction.checkout.domain.ICartRepository;
import com.tokopedia.transaction.checkout.domain.IVoucherCouponMapper;
import com.tokopedia.transaction.checkout.domain.response.couponlist.CouponDataResponse;
import com.tokopedia.transaction.checkout.view.data.voucher.CouponListData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class GetCouponListCartMarketPlaceUseCase extends UseCase<CouponListResult> {
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PAGE_SIZE = "page_size";
    private final ICartRepository cartRepository;
    private final IVoucherCouponMapper voucherCouponMapper;

    @Inject
    public GetCouponListCartMarketPlaceUseCase(ICartRepository cartRepository,
                                               IVoucherCouponMapper voucherCouponMapper) {
        this.cartRepository = cartRepository;
        this.voucherCouponMapper = voucherCouponMapper;
    }

    @Override
    public Observable<CouponListResult> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_PAGE, requestParams.getString(PARAM_PAGE, ""));
        param.put(PARAM_PAGE_SIZE, requestParams.getString(PARAM_PAGE_SIZE, ""));

        return cartRepository.getCouponList(AuthUtil.generateParamsNetwork(
                MainApplication.getAppContext(), param
        )).map(new Func1<CouponDataResponse, CouponListData>() {
            @Override
            public CouponListData call(CouponDataResponse couponDataResponse) {
                return voucherCouponMapper.convertCouponListData(couponDataResponse);
            }
        }).map(new Func1<CouponListData, CouponListResult>() {
            @Override
            public CouponListResult call(CouponListData couponListData) {
                return voucherCouponMapper.convertCouponListResult(couponListData);
            }
        });
    }
}
