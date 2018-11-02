package com.tokopedia.loyalty.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.usecase.FlightCheckVoucherUseCase;
import com.tokopedia.loyalty.domain.usecase.TrainCheckVoucherUseCase;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.interactor.IPromoCodeInteractor;
import com.tokopedia.loyalty.view.view.IPromoCodeView;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class PromoCodePresenter implements IPromoCodePresenter {

    private static final String PARAM_CARTS = "carts";
    private static final String PARAM_PROMO_CODE = "promo_code";
    private static final String PARAM_SUGGESTED = "suggested";
    private static final String PARAM_LANG = "lang";
    private static final String PAREM_ONE_CLICK_SHIPMENT = "is_one_click_shipment";

    private final IPromoCodeView view;
    private final IPromoCodeInteractor promoCodeInteractor;
    private FlightCheckVoucherUseCase flightCheckVoucherUseCase;
    private TrainCheckVoucherUseCase trainCheckVoucherUseCase;

    @Inject
    public PromoCodePresenter(IPromoCodeView view,
                              IPromoCodeInteractor interactor,
                              FlightCheckVoucherUseCase flightCheckVoucherUseCase,
                              TrainCheckVoucherUseCase trainCheckVoucherUseCase) {
        this.view = view;
        this.promoCodeInteractor = interactor;
        this.flightCheckVoucherUseCase = flightCheckVoucherUseCase;
        this.trainCheckVoucherUseCase = trainCheckVoucherUseCase;
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
    public void processCheckMarketPlaceCartListPromoCode(
            Activity activity, String voucherCode, String paramUpdateCartString, boolean isOneClickShipment
    ) {
        TKPDMapParam<String, String> paramUpdateCart = null;
        if (!TextUtils.isEmpty(paramUpdateCartString)) {
            paramUpdateCart = new TKPDMapParam<>();
            paramUpdateCart.put(PARAM_CARTS, paramUpdateCartString);
        }
        TKPDMapParam<String, String> paramCheckPromo = new TKPDMapParam<>();
        paramCheckPromo.put(PARAM_PROMO_CODE, voucherCode);
        paramCheckPromo.put(PARAM_SUGGESTED, "0");
        paramCheckPromo.put(PARAM_LANG, "id");
        paramCheckPromo.put(PAREM_ONE_CLICK_SHIPMENT, String.valueOf(isOneClickShipment));

        promoCodeInteractor.submitCheckPromoCodeMarketPlace(
                paramUpdateCart != null ? AuthUtil.generateParamsNetwork(activity, paramUpdateCart) : null,
                AuthUtil.generateParamsNetwork(activity, paramCheckPromo),
                new Subscriber<VoucherViewModel>() {
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
                    public void onNext(VoucherViewModel voucherViewModel) {
                        if (!voucherViewModel.isSuccess()) {
                            view.onPromoCodeError(voucherViewModel.getMessage());
                        } else {
                            view.hideProgressLoading();
                            view.checkVoucherSuccessfull(voucherViewModel);
                        }
                    }
                }

        );
    }

    @Override
    public void processCheckEventPromoCode(String voucherId, JsonObject requestBody, boolean flag) {
        view.showProgressLoading();
        requestBody.addProperty("promocode", voucherId);
        RequestParams requestParams = RequestParams.create().create();
        requestParams.putObject("checkoutdata", requestBody);
        requestParams.putBoolean("ispromocodecase", flag);
        ((LoyaltyModuleRouter) view.getContext().getApplicationContext()).verifyEventPromo(requestParams).subscribe(new Subscriber<com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException
                        || e instanceof com.tokopedia.abstraction.common.network.exception.ResponseErrorException) {
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

    @Override
    public void processCheckDealPromoCode(String voucherId, JsonObject requestBody, boolean flag) {
        view.showProgressLoading();
        requestBody.addProperty("promocode", voucherId);
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putObject("checkoutdata", requestBody);
        requestParams.putBoolean("ispromocodecase", flag);
        ((LoyaltyModuleRouter) view.getContext().getApplicationContext()).verifyDealPromo(requestParams)
                .subscribe(new Subscriber<com.tokopedia.abstraction.common.utils.TKPDMapParam<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgressLoading();
                        if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException || e instanceof com.tokopedia.abstraction.common.network.exception.ResponseErrorException) {
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
    public void processCheckTrainPromoCode(Activity activity, String trainReservationId,
                                           String trainReservationCode, String voucherCode) {
        view.showProgressLoading();
        trainCheckVoucherUseCase.execute(
                trainCheckVoucherUseCase.createVoucherRequest(trainReservationId, trainReservationCode, voucherCode),
                checkTrainVoucherSubscriber()
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
                } else if (e instanceof MessageErrorException) {
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

    @NonNull
    private Subscriber<VoucherViewModel> checkTrainVoucherSubscriber() {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.sendTrackingOnCheckTrainVoucherError(e.getMessage());
                e.printStackTrace();
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    view.onPromoCodeError(e.getMessage());
                } else if (e instanceof MessageErrorException) {
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
