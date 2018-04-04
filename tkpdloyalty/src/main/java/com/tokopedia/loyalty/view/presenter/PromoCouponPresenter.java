package com.tokopedia.loyalty.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.usecase.FlightCheckVoucherUseCase;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.interactor.IPromoCouponInteractor;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.loyalty.view.activity.LoyaltyActivity.FLIGHT_STRING;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponPresenter implements IPromoCouponPresenter {

    private final IPromoCouponInteractor promoCouponInteractor;
    private final IPromoCouponView view;
    private FlightCheckVoucherUseCase flightCheckVoucherUseCase;

    @Inject
    public PromoCouponPresenter(IPromoCouponView view, IPromoCouponInteractor promoCouponInteractor, FlightCheckVoucherUseCase flightCheckVoucherUseCase) {
        this.view = view;
        this.promoCouponInteractor = promoCouponInteractor;
        this.flightCheckVoucherUseCase = flightCheckVoucherUseCase;
    }

    @Override
    public void processGetCouponList(String platform) {
        view.disableSwipeRefresh();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        //param.put("user_id", SessionHandler.getLoginID(view.getContext()));
        if (platform.equalsIgnoreCase(FLIGHT_STRING)){
            platform = LoyaltyActivity.DIGITAL_STRING;
            param.put("category_id", view.getCategoryId());
        };
        param.put("type", platform);

        //TODO Revert Later
        promoCouponInteractor.getCouponList(
                AuthUtil.generateParamsNetwork(view.getContext(), param),
                //AuthUtil.generateDummyParamsNetwork(view.getContext(), param),
                new Subscriber<List<CouponData>>() {
                    @Override
                    public void onCompleted() {
                        view.enableSwipeRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof TokoPointResponseErrorException) {
                            view.renderErrorGetCouponList(e.getMessage());
                        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            view.renderErrorNoConnectionGetCouponList(
                                    ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_SHORT
                            );
                        } else if (e instanceof SocketTimeoutException) {
                            view.renderErrorTimeoutConnectionGetCouponList(
                                    ErrorNetMessage.MESSAGE_ERROR_TIMEOUT_SHORT
                            );
                        } else if (e instanceof HttpErrorException) {
                            view.renderErrorHttpGetCouponList(
                                    e.getMessage()
                            );
                        } else {
                            view.renderErrorGetCouponList(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                        }
                    }

                    @Override
                    public void onNext(List<CouponData> couponData) {
                        if (couponData.size() < 1) {
                            view.couponDataNoResult();
                        } else {
                            view.renderCouponListDataResult(couponData);
                        }
                    }
                });
    }

    @Override
    public void processPostCouponValidateRedeem() {

    }

    @Override
    public void processPostCouponRedeem() {

    }

    @Override
    public void processGetPointRecentHistory() {

    }

    @Override
    public void processGetCatalogList() {

    }

    @Override
    public void processGetCatalogDetail() {

    }

    @Override
    public void processGetCatalogFilterCategory() {

    }

    @Override
    public void submitVoucher(final CouponData couponData) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(VOUCHER_CODE, couponData.getCode());
        promoCouponInteractor.submitVoucher(
                couponData.getTitle(),
                couponData.getCode(),
                AuthUtil.generateParamsNetwork(view.getContext(), param
                ), makeCouponSubscriber(couponData));

    }

    @Override
    public void submitDigitalVoucher(CouponData couponData, String categoryId) {
        view.showProgressLoading();
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(VOUCHER_CODE, couponData.getCode());
        param.put(CATEGORY_ID, categoryId);
        promoCouponInteractor.submitDigitalVoucher(
                couponData.getTitle(),
                couponData.getCode(),
                AuthUtil.generateParamsNetwork(view.getContext(), param
                ), makeDigitalCouponSubscriber(couponData));
    }

    @Override
    public void submitFlightVoucher(final CouponData data, String cartId) {
        view.showProgressLoading();
        flightCheckVoucherUseCase.execute(flightCheckVoucherUseCase.createCouponRequest(cartId, data.getCode()),
                checkFlightCouponSubscriber(data));
    }

    @NonNull
    private Subscriber<VoucherViewModel> checkFlightCouponSubscriber(final CouponData data) {
        return new Subscriber<VoucherViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    data.setErrorMessage(e.getMessage());
                    view.couponError();
                } else if (e instanceof MessageErrorException) {
                    data.setErrorMessage(e.getMessage());
                    view.showSnackbarError(e.getMessage());
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(VoucherViewModel voucherViewModel) {
                CouponViewModel viewModel = new CouponViewModel();
                viewModel.setAmount(voucherViewModel.getAmount());
                viewModel.setCode(voucherViewModel.getCode());
                viewModel.setRawCashback(voucherViewModel.getRawCashback());
                viewModel.setRawDiscount(voucherViewModel.getRawDiscount());
                viewModel.setMessage(voucherViewModel.getMessage());
                viewModel.setTitle(voucherViewModel.getCode());
                viewModel.setSuccess(true);
                view.receiveResult(viewModel);
                view.hideProgressLoading();
            }
        };
    }

    private Subscriber<CouponViewModel> makeCouponSubscriber(final CouponData couponData) {
        return new Subscriber<CouponViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CouponViewModel couponViewModel) {
                view.receiveResult(couponViewModel);
                view.hideProgressLoading();
            }
        };
    }

    private Subscriber<CouponViewModel> makeDigitalCouponSubscriber(final CouponData couponData) {
        return new Subscriber<CouponViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
                if (e instanceof TokoPointResponseErrorException || e instanceof ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
            }

            @Override
            public void onNext(CouponViewModel couponViewModel) {
                view.receiveDigitalResult(couponViewModel);
                view.hideProgressLoading();
            }
        };
    }

    @Override
    public void detachView() {
        promoCouponInteractor.unsubscribe();
    }

}
