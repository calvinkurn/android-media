package com.tokopedia.seller.gmsubscribe.domain.cart.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.UseCase;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.cart.GMSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class CheckGMSubscribeVoucherUseCase extends UseCase<GMVoucherCheckDomainModel> {
    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    public static final int UNDEFINED_SELECTED = -1;
    public static final String VOUCHER_CODE = "VOUCHER_CODE";
    public static final String EMPTY_VOUCHER = "";
    private final GMSubscribeCartRepository gmSubscribeCartRepository;

    public CheckGMSubscribeVoucherUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GMSubscribeCartRepository gmSubscribeCartRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeCartRepository = gmSubscribeCartRepository;
    }

    @Override
    public Observable<GMVoucherCheckDomainModel> createObservable(RequestParams requestParams) {
        return gmSubscribeCartRepository.checkVoucher(
                requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED),
                requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER)
        );
    }

    public static RequestParams createRequestParams(int productId, String voucherCode) {
        RequestParams params = RequestParams.create();
        params.putInt(SELECTED_PRODUCT, productId);
        params.putString(VOUCHER_CODE, voucherCode);
        return params;
    }
}
