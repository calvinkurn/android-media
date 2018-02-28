package com.tokopedia.loyalty.view.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;
import com.tokopedia.loyalty.router.ITkpdLoyaltyModuleRouter;
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
    public void processCheckMarketPlaceCartListPromoCode(Activity activity, final String voucherCode) {
        if (activity.getApplication() instanceof ITkpdLoyaltyModuleRouter) {
            promoCodeInteractor.submitVoucherMarketPlaceCartList(
                    ((ITkpdLoyaltyModuleRouter) activity.getApplication())
                            .tkpdLoyaltyGetCheckPromoCodeCartListResultObservable(voucherCode),
                    new Subscriber<CheckPromoCodeCartListResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            view.hideProgressLoading();
                            if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException) {
                                view.onPromoCodeError(e.getMessage());
                            } else view.onGetGeneralError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onNext(CheckPromoCodeCartListResult checkPromoCodeCartListResult) {
                            VoucherViewModel viewModel = new VoucherViewModel();
                            viewModel.setAmount(checkPromoCodeCartListResult.getDataVoucher().getDiscountAmount());
                            viewModel.setMessage(checkPromoCodeCartListResult.getDataVoucher().getMessageSuccess());
                            viewModel.setCode(checkPromoCodeCartListResult.getDataVoucher().getCode());
                            view.hideProgressLoading();
                            view.checkVoucherSuccessfull(viewModel);
                        }
                    }
            );
        }
    }

    @Override
    public void processCheckMarketPlaceCartShipmentPromoCode(
            Activity activity, final String voucherCode, String paramCartShipment
    ) {
        CheckPromoCodeCartShipmentRequest data =
                new Gson().fromJson(paramCartShipment, CheckPromoCodeCartShipmentRequest.class);
        data.setPromoCode(voucherCode);
        if (activity.getApplication() instanceof ITkpdLoyaltyModuleRouter) {
            promoCodeInteractor.submitVoucherMarketPlaceCartShipment(
                    ((ITkpdLoyaltyModuleRouter) activity.getApplication())
                            .tkpdLoyaltyGetCheckPromoCodeCartShipmentResultObservable(data),
                    new Subscriber<CheckPromoCodeCartShipmentResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            view.hideProgressLoading();
                            if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException) {
                                view.onPromoCodeError(e.getMessage());
                            } else view.onGetGeneralError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }

                        @Override
                        public void onNext(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
                            VoucherViewModel viewModel = new VoucherViewModel();
                            viewModel.setAmount(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherAmountIdr());
                            viewModel.setMessage(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherPromoDesc());
                            viewModel.setCode(voucherCode);
                            view.hideProgressLoading();
                            view.checkVoucherSuccessfull(viewModel);
                        }
                    }
            );
        }
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
