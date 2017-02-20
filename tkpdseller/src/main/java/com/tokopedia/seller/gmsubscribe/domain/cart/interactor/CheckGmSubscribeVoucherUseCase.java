package com.tokopedia.seller.gmsubscribe.domain.cart.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.cart.exception.GmVoucherCheckException;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmVoucherCheckDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class CheckGmSubscribeVoucherUseCase extends UseCase<GmVoucherCheckDomainModel> {
    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    public static final int UNDEFINED_SELECTED = -1;
    public static final String VOUCHER_CODE = "VOUCHER_CODE";
    public static final String EMPTY_VOUCHER = "";
    private final GmSubscribeCartRepository gmSubscribeCartRepository;

    public CheckGmSubscribeVoucherUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeCartRepository gmSubscribeCartRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeCartRepository = gmSubscribeCartRepository;
    }

    public static RequestParams createRequestParams(int productId, String voucherCode) {
        RequestParams params = RequestParams.create();
        params.putInt(SELECTED_PRODUCT, productId);
        params.putString(VOUCHER_CODE, voucherCode);
        return params;
    }

    @Override
    public Observable<GmVoucherCheckDomainModel> createObservable(RequestParams requestParams) {
        int selectedProduct = requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED);
        String voucherCode = requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER);
        if (selectedProduct == UNDEFINED_SELECTED || voucherCode.equals(EMPTY_VOUCHER)) {
            throw new GmVoucherCheckException("Invalid Voucher Input");
        }
        return gmSubscribeCartRepository.checkVoucher(
                selectedProduct,
                voucherCode
        );
    }
}
