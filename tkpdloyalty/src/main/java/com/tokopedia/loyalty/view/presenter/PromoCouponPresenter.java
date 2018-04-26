package com.tokopedia.loyalty.view.presenter;

import com.google.gson.JsonObject;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.exception.LoyaltyErrorException;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.interactor.IPromoCouponInteractor;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponPresenter implements IPromoCouponPresenter {

    private final IPromoCouponInteractor promoCouponInteractor;
    private final IPromoCouponView view;

    @Inject
    public PromoCouponPresenter(IPromoCouponView view, IPromoCouponInteractor promoCouponInteractor) {
        this.view = view;
        this.promoCouponInteractor = promoCouponInteractor;
    }

    @Override
    public void processGetCouponList(String platform) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        view.disableSwipeRefresh();
        //param.put("user_id", SessionHandler.getLoginID(view.getContext()));
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
    public void processGetEventCouponList(int categoryId, int productId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        view.disableSwipeRefresh();
        param.put("category_id", String.valueOf(categoryId));
        param.put("product_id", String.valueOf(productId));
        param.put("page", String.valueOf(1));
        param.put("page_size", String.valueOf(20));

        //TODO Revert Later
        promoCouponInteractor.getCouponList(
                AuthUtil.generateParamsNetwork(view.getContext(), param),
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
    public void submitEventVoucher(final CouponData couponData, JsonObject requestBody, boolean flag) {
        view.showProgressLoading();
        requestBody.addProperty("promocode", couponData.getCode());
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
                if (e instanceof LoyaltyErrorException || e instanceof ResponseErrorException) {
                    couponData.setErrorMessage(e.getMessage());
                    view.couponError();
                } else {
                    view.showSnackbarError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                }
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
                if ((failmsg != null && failmsg.length() > 0) || status.length() == 0) {
                    couponData.setErrorMessage(failmsg);
                    view.couponError();
                } else {
                    CouponViewModel couponViewModel = new CouponViewModel();
                    couponViewModel.setCode(promocode);
                    couponViewModel.setMessage(successMsg);
                    couponViewModel.setSuccess(true);
                    couponViewModel.setAmount("");
                    couponViewModel.setRawCashback(cashback);
                    couponViewModel.setRawDiscount(discount);
                    couponViewModel.setTitle("");
                    view.receiveDigitalResult(couponViewModel);
                }
            }
        });

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
