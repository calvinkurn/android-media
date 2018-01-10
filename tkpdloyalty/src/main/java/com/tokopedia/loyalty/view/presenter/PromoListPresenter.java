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
    private int page = 1;

    @Inject
    public PromoListPresenter(IPromoInteractor promoInteractor, IPromoListView view) {
        this.promoInteractor = promoInteractor;
        this.view = view;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void processGetPromoList(String subCategories) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("categories", subCategories);
        param.put("categories_exclude", "30");
        param.put("page", String.valueOf(page));
        this.promoInteractor.getPromoList(param, new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }

            @Override
            public void onNext(List<PromoData> promoData) {
                if (promoData.size() > 0) {
                    if (promoData.size() == 10) {
                        page++;
                        view.renderNextPage(true);
                    } else {
                        view.renderNextPage(false);
                    }
                    view.renderPromoDataList(promoData, true);
                } else {
                    view.renderErrorGetPromoDataList("Empty Data");
                }
            }
        });
    }

    @Override
    public void processGetPromoListLoadMore(String subCategories) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("categories", subCategories);
        param.put("categories_exclude", "30");
        param.put("page", String.valueOf(page));
        this.promoInteractor.getPromoList(param, new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }

            @Override
            public void onNext(List<PromoData> promoData) {
                if (promoData.size() > 0) {
                    if (promoData.size() == 10) {
                        page++;
                        view.renderNextPage(true);
                    } else {
                        page = 1;
                        view.renderNextPage(false);
                    }
                } else {
                    page = 1;
                    view.renderNextPage(false);
                }
                view.renderPromoDataList(promoData, false);
            }
        });
    }

    private void handleError(Throwable e) {
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
}
