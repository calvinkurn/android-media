package com.tokopedia.loyalty.view.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
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
    public void processCheckEventPromoCode(String voucherId, JsonObject requestBody, boolean flag) {
        view.showProgressLoading();
        requestBody.addProperty("promocode", voucherId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject("checkoutdata", requestBody);
        requestParams.putBoolean("ispromocodecase", flag);
        ((TkpdCoreRouter) view.getContext().getApplicationContext()).verifyEventPromo(requestParams).subscribe(new Subscriber<com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException) {
                    view.onPromoCodeError(e.getMessage());
                } else view.onGetGeneralError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object> resultMap) {
                view.hideProgressLoading();
                String promocode = (String) resultMap.get("promocode");
                int discount = (int) resultMap.get("promocode_discount");
                int cashback = (int) resultMap.get("promocode_cashback");
                String failmsg = (String) resultMap.get("promocode_failure_message");
                String successMsg = (String) resultMap.get("promocode_success_message");
                String status = (String) resultMap.get("promocode_status");

                VoucherViewModel couponViewModel = new VoucherViewModel();
                couponViewModel.setCode(promocode);
                if ((failmsg != null && failmsg.length() > 0) || status.length() == 0) {
                    couponViewModel.setSuccess(false);
                    couponViewModel.setMessage(failmsg);
                    couponViewModel.setAmount("");
                    couponViewModel.setRawCashback(0);
                    couponViewModel.setRawDiscount(0);
                    view.onPromoCodeError(failmsg);
                } else {
                    couponViewModel.setMessage(successMsg);
                    couponViewModel.setSuccess(true);
                    couponViewModel.setAmount("");
                    couponViewModel.setRawCashback(cashback);
                    couponViewModel.setRawDiscount(discount);
                    view.checkDigitalVoucherSucessful(couponViewModel);
                }
            }
        });

    }

    private Subscriber<VoucherViewModel> makeVoucherViewModel() {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
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
                if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException) {
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
