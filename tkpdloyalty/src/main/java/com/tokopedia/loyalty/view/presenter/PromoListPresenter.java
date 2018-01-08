package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoListView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListPresenter implements IPromoListPresenter {
    private final IPromoInteractor promoInteractor;
    private final IPromoListView view;

    @Inject
    public PromoListPresenter(IPromoInteractor promoInteractor, IPromoListView view) {
        this.promoInteractor = promoInteractor;
        this.view = view;
    }


    @Override
    public void processGetPromoList(String subCategories) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("categories", subCategories);
        this.promoInteractor.getPromoList(param, new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                             /* Ini kalau ga ada internet */
                    view.renderErrorNoConnectionGetPromoDataList(
                            ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL
                    );
                } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                            /* Ini kalau timeout */
                    view.renderErrorTimeoutConnectionGetPromoDataListt(
                            ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                    );
                } else if (e instanceof HttpErrorException) {
                            /* Ini Http error, misal 403, 500, 404,
                            code http errornya bisa diambil
                             e.getErrorCode */
                    view.renderErrorHttpGetPromoDataList(e.getMessage());
                } else {
                             /* Ini diluar dari segalanya hahahaha */
                    view.renderErrorHttpGetPromoDataList(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(List<PromoData> promoData) {
                view.renderPromoDataList(promoData);
            }
        });
    }
}
