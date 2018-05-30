package com.tokopedia.loyalty.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartListResult;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.loyalty.domain.usecase.FlightCheckVoucherUseCase;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;
import com.tokopedia.loyalty.router.ITkpdLoyaltyModuleRouter;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
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
    private FlightCheckVoucherUseCase flightCheckVoucherUseCase;

    @Inject
    public PromoCodePresenter(IPromoCodeView view,
                              IPromoCodeInteractor interactor,
                              FlightCheckVoucherUseCase flightCheckVoucherUseCase) {
        this.view = view;
        this.promoCodeInteractor = interactor;
        this.flightCheckVoucherUseCase = flightCheckVoucherUseCase;
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
                            if (checkPromoCodeCartListResult.isError()) {
                                view.onPromoCodeError(checkPromoCodeCartListResult.getErrorMessage());
                            } else {
                                VoucherViewModel viewModel = new VoucherViewModel();
                                viewModel.setAmount(checkPromoCodeCartListResult.getDataVoucher().getDiscountAmount());
                                viewModel.setMessage(checkPromoCodeCartListResult.getDataVoucher().getMessageSuccess());
                                viewModel.setCode(checkPromoCodeCartListResult.getDataVoucher().getCode());
                                view.hideProgressLoading();
                                view.checkVoucherSuccessfull(viewModel);
                            }

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
                            if (checkPromoCodeCartShipmentResult.isError()) {
                                view.onPromoCodeError(checkPromoCodeCartShipmentResult.getErrorMessage());
                            } else {
                                VoucherViewModel viewModel = new VoucherViewModel();
                                viewModel.setAmount(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherAmountIdr());
                                viewModel.setMessage(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherPromoDesc());
                                viewModel.setCode(voucherCode);
                                view.hideProgressLoading();
                                view.checkVoucherSuccessfull(viewModel);
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void processCheckEventPromoCode(String voucherId, JsonObject requestBody, boolean flag) {
        view.showProgressLoading();
        requestBody.addProperty("promocode", voucherId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject("checkoutdata", requestBody);
        requestParams.putBoolean("ispromocodecase", flag);
        ((LoyaltyModuleRouter) view.getContext().getApplicationContext()).verifyEventPromo(requestParams).subscribe(new Subscriber<com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object>>() {
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
                    UnifyTracking.eventDigitalEventTracking("voucher failed - " + promocode, failmsg);
                } else {
                    couponViewModel.setMessage(successMsg);
                    couponViewModel.setSuccess(true);
                    couponViewModel.setAmount("");
                    couponViewModel.setRawCashback(cashback);
                    couponViewModel.setRawDiscount(discount);
                    UnifyTracking.eventDigitalEventTracking("voucher success - " + promocode, successMsg);
                    view.checkDigitalVoucherSucessful(couponViewModel);
                }
            }
        });

    }

    @Override
    public void processCheckFlightPromoCode(Activity activity, String voucherCode, String cartId) {
        view.showProgressLoading();
        flightCheckVoucherUseCase.execute(
                flightCheckVoucherUseCase.createVoucherRequest(cartId, voucherCode),
                checkFlightVoucherSubscriber()
        );
    }

    @NonNull
    private Subscriber<VoucherViewModel> checkFlightVoucherSubscriber() {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    view.onPromoCodeError(e.getMessage());
                }else if (e instanceof MessageErrorException) {
                    view.onGetGeneralError(e.getMessage());
                } else view.onGetGeneralError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(VoucherViewModel voucherViewModel) {
                view.hideProgressLoading();
                view.checkDigitalVoucherSucessful(voucherViewModel);
            }
        };
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
