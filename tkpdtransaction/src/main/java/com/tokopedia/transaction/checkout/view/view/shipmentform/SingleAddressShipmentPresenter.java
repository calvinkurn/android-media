package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.domain.usecase.GetAllAddressUseCase;
import com.tokopedia.transaction.checkout.domain.usecase.GetDefaultAddressUseCase;
import com.tokopedia.transaction.checkout.util.PeopleAddressAuthUtil;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.presenter.CartMvpPresenter;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class SingleAddressShipmentPresenter
        extends CartMvpPresenter<ICartSingleAddressView<CartSingleAddressData>> {

    private static final String TAG = SingleAddressShipmentPresenter.class.getSimpleName();

    private static final String DEFAULT_KEYWORD = "";
    private static final int DEFAULT_ORDER = 1;

    private final GetDefaultAddressUseCase mGetDefaultAddressUseCase;
    private final PagingHandler mPagingHandler;

    @Inject
    public SingleAddressShipmentPresenter(GetDefaultAddressUseCase getDefaultAddressUseCase,
                                          PagingHandler pagingHandler) {
        mGetDefaultAddressUseCase = getDefaultAddressUseCase;
        mPagingHandler = pagingHandler;
    }

    @Override
    public void attachView(ICartSingleAddressView<CartSingleAddressData> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    /**
     *
     * @param context
     * @param cartSingleAddressData
     */
    public void getCartShipmentData(Context context, final CartSingleAddressData cartSingleAddressData) {
        getMvpView().show(cartSingleAddressData);
        mGetDefaultAddressUseCase.execute(
                PeopleAddressAuthUtil.getPeopleAddressRequestParams(context, DEFAULT_ORDER, DEFAULT_KEYWORD, mPagingHandler.getPage()),
                new Subscriber<List<RecipientAddressModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<RecipientAddressModel> recipientAddressModels) {
                        cartSingleAddressData.setRecipientAddressModel(recipientAddressModels.get(0));
                        getMvpView().show(cartSingleAddressData);
                    }
                });
    }

}
