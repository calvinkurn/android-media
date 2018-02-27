package com.tokopedia.loyalty.view.presenter;

import android.content.Context;

import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.interactor.IPromoCodeInteractor;
import com.tokopedia.loyalty.view.view.IPromoCodeView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class PromoCodePresenter implements IPromoCodePresenter {
    private final IPromoCodeView view;
    private final IPromoCodeInteractor promoCodeInteractor;

    @Inject
    public PromoCodePresenter(IPromoCodeView view, IPromoCodeInteractor interactor) {
        this.view = view;
        this.promoCodeInteractor = interactor;
    }

    @Override
    public void processCheckPromoCode(Context context, String voucherCode) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("voucher_code", voucherCode);
        promoCodeInteractor.submitVoucher(voucherCode,
                AuthUtil.generateParamsNetwork(context, param),
                makeVoucherViewModel());
    }

    @Override
    public void processCheckDigitalPromoCode(
            Context context,
            String voucherCode,
            String categoryId) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("voucher_code", voucherCode);
        param.put("category_id", categoryId);
        promoCodeInteractor.submitDigitalVoucher(voucherCode,
                AuthUtil.generateParamsNetwork(context, param),
                makeDigitalVoucherViewModel());
    }

    @Override
    public void processCheckMarketPlaceCartListPromoCode(Context context, String voucherCode) {

    }

    @Override
    public void processCheckMarketPlaceCartShipmentPromoCode(Context context, String voucherCode) {

    }

    private Subscriber<VoucherViewModel> makeVoucherViewModel() {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if(e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    view.onPromoCodeError(e.getMessage());
                } else view.onGetGeneralError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(VoucherViewModel voucherViewModel) {
                view.hideProgressLoading();
                view.checkVoucherSuccessfull(voucherViewModel);
            }
        };
    }

    private Subscriber<VoucherViewModel> makeDigitalVoucherViewModel() {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if(e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException) {
                    view.onPromoCodeError(e.getMessage());
                } else view.onGetGeneralError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(VoucherViewModel voucherViewModel) {
                view.hideProgressLoading();
                view.checkDigitalVoucherSucessful(voucherViewModel);
            }
        };
    }

}
