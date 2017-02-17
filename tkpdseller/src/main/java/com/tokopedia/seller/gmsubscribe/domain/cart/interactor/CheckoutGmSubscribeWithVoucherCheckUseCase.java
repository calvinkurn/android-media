package com.tokopedia.seller.gmsubscribe.domain.cart.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.cart.exception.GmCheckoutCheckException;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmVoucherCheckDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class CheckoutGmSubscribeWithVoucherCheckUseCase extends CheckoutGmSubscribeUseCase {

    public CheckoutGmSubscribeWithVoucherCheckUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeCartRepository gmSubscribeCartRepository) {
        super(threadExecutor, postExecutionThread, gmSubscribeCartRepository);
    }

    @Override
    public Observable<GmCheckoutDomainModel> createObservable(RequestParams requestParams) {
        return gmSubscribeCartRepository.checkVoucher(
                requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED),
                requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER)
        ).flatMap(new ContinueCheckout(requestParams));
    }

    private class ContinueCheckout implements Func1<GmVoucherCheckDomainModel, Observable<GmCheckoutDomainModel>> {
        private final RequestParams requestParams;

        public ContinueCheckout(RequestParams requestParams) {
            this.requestParams = requestParams;
        }

        @Override
        public Observable<GmCheckoutDomainModel> call(GmVoucherCheckDomainModel gmVoucherCheckDomainModel) {
            int selectedProduct = requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED);
            int autoExtendSelectedProduct = requestParams.getInt(SELECTED_AUTOSUBSCRIBE_PRODUCT, UNDEFINED_SELECTED);
            String voucherCode = requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER);
            if (selectedProduct == UNDEFINED_SELECTED || voucherCode.equals(EMPTY_VOUCHER)) {
                throw new GmCheckoutCheckException("Undefined selected selected product or voucher code");
            }
            return gmSubscribeCartRepository.checkoutGMSubscribe(
                    selectedProduct,
                    autoExtendSelectedProduct,
                    voucherCode
            );
        }
    }
}
