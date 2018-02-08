package com.tokopedia.gm.subscribe.domain.cart.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.subscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.gm.subscribe.domain.cart.exception.GmCheckoutCheckException;
import com.tokopedia.gm.subscribe.domain.cart.model.GmCheckoutDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class CheckoutGmSubscribeUseCase extends UseCase<GmCheckoutDomainModel> {
    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    public static final String SELECTED_AUTOSUBSCRIBE_PRODUCT = "SELECTED_AUTOSUBSCRIBE_PRODUCT";
    public static final int UNDEFINED_SELECTED = -1;
    public static final String VOUCHER_CODE = "VOUCHER_CODE";
    public static final String EMPTY_VOUCHER = "";
    protected final GmSubscribeCartRepository gmSubscribeCartRepository;

    @Inject
    public CheckoutGmSubscribeUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeCartRepository gmSubscribeCartRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeCartRepository = gmSubscribeCartRepository;
    }

    public static RequestParams generateParams(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        RequestParams params = RequestParams.create();
        params.putInt(SELECTED_PRODUCT, selectedProduct);
        params.putInt(SELECTED_AUTOSUBSCRIBE_PRODUCT, autoExtendSelectedProduct);
        params.putString(VOUCHER_CODE, voucherCode);
        return params;
    }

    @Override
    public Observable<GmCheckoutDomainModel> createObservable(RequestParams requestParams) {
        int selectedProduct = requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED);
        int autoExtendSelectedProduct = requestParams.getInt(SELECTED_AUTOSUBSCRIBE_PRODUCT, UNDEFINED_SELECTED);
        String voucherCode = requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER);
        if (selectedProduct == UNDEFINED_SELECTED) {
            throw new GmCheckoutCheckException("Invalid Selected Product");
        }
        return gmSubscribeCartRepository.checkoutGMSubscribe(
                selectedProduct,
                autoExtendSelectedProduct,
                voucherCode
        );
    }
}
