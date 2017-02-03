package com.tokopedia.seller.gmsubscribe.domain.cart.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.UseCase;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.cart.GMSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class CheckoutGMSubscribeUseCase extends UseCase<GMCheckoutDomainModel>{
    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    public static final String SELECTED_AUTOSUBSCRIBE_PRODUCT = "SELECTED_AUTOSUBSCRIBE_PRODUCT";
    public static final int UNDEFINED_SELECTED = -1;
    public static final String VOUCHER_CODE = "VOUCHER_CODE";
    public static final String EMPTY_VOUCHER = "";
    protected final GMSubscribeCartRepository gmSubscribeCartRepository;

    public CheckoutGMSubscribeUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GMSubscribeCartRepository gmSubscribeCartRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeCartRepository = gmSubscribeCartRepository;
    }

    @Override
    public Observable<GMCheckoutDomainModel> createObservable(RequestParams requestParams) {
        return gmSubscribeCartRepository.checkoutGMSubscribe(
                requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED),
                requestParams.getInt(SELECTED_AUTOSUBSCRIBE_PRODUCT, UNDEFINED_SELECTED),
                requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER)
        );
    }

    public static RequestParams generateParams(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        RequestParams params = RequestParams.create();
        params.putInt(SELECTED_PRODUCT, selectedProduct);
        params.putInt(SELECTED_AUTOSUBSCRIBE_PRODUCT, autoExtendSelectedProduct);
        params.putString(VOUCHER_CODE, voucherCode);
        return params;
    }
}
