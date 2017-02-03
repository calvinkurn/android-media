package com.tokopedia.seller.gmsubscribe.domain.cart.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.UseCase;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.cart.GMSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class CheckoutGMSubscribeWithVoucherCheckUseCase extends CheckoutGMSubscribeUseCase {

    public CheckoutGMSubscribeWithVoucherCheckUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GMSubscribeCartRepository gmSubscribeCartRepository) {
        super(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
    }

    @Override
    public Observable<GMCheckoutDomainModel> createObservable(RequestParams requestParams) {
        return gmSubscribeCartRepository.checkVoucher(
                requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED),
                requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER)
        ).flatMap(new ContinueCheckout(requestParams));
    }

    private class ContinueCheckout implements Func1<GMVoucherCheckDomainModel, Observable<GMCheckoutDomainModel>> {
        private final RequestParams requestParams;

        public ContinueCheckout(RequestParams requestParams) {
            this.requestParams = requestParams;
        }

        @Override
        public Observable<GMCheckoutDomainModel> call(GMVoucherCheckDomainModel gmVoucherCheckDomainModel) {
            return gmSubscribeCartRepository.checkoutGMSubscribe(
                    requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED),
                    requestParams.getInt(SELECTED_AUTOSUBSCRIBE_PRODUCT, UNDEFINED_SELECTED),
                    requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER)
            );
        }
    }
}
