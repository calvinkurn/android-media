package com.tokopedia.ride.bookingride.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.repository.TokoCashRepositoryImpl;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.exception.InterruptConfirmationHttpException;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetPaymentMethodListCacheUseCase;
import com.tokopedia.ride.bookingride.domain.GetPaymentMethodListUseCase;
import com.tokopedia.ride.common.configuration.PaymentMode;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/22/17.
 */

public class ConfirmBookingPresenter extends BaseDaggerPresenter<ConfirmBookingContract.View>
        implements ConfirmBookingContract.Presenter {

    private final String BEARER_TOKEN = "Bearer ";

    private GetFareEstimateUseCase getFareEstimateUseCase;
    private GetPaymentMethodListUseCase getPaymentMethodListUseCase;
    private GetPaymentMethodListCacheUseCase getPaymentMethodListCacheUseCase;
    private String tokoCashBalance;

    @Inject
    public ConfirmBookingPresenter(GetFareEstimateUseCase getFareEstimateUseCase, GetPaymentMethodListUseCase getPaymentMethodListUseCase, GetPaymentMethodListCacheUseCase getPaymentMethodListCacheUseCase) {
        this.getFareEstimateUseCase = getFareEstimateUseCase;
        this.getPaymentMethodListUseCase = getPaymentMethodListUseCase;
        this.getPaymentMethodListCacheUseCase = getPaymentMethodListCacheUseCase;
    }

    @Override
    public void initialize() {
        getView().renderInitialView();

        actionGetFareAndEstimate(getView().getParam());
    }

    @Override
    public void actionGetFareAndEstimate(RequestParams showProgress) {
        getView().showProgress();
        getView().hidePromoLayout();
        getView().disableConfirmBtn();

        RequestParams requestParams = getView().getParam();
        getFareEstimateUseCase.execute(requestParams, new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideProgress();

                    if (e instanceof InterruptConfirmationHttpException) {
                        if (((InterruptConfirmationHttpException) e).getType().equalsIgnoreCase(InterruptConfirmationHttpException.TOS_CONFIRMATION_INTERRUPT)) {
                            getView().openInterruptConfirmationWebView(((InterruptConfirmationHttpException) e).getHref());
                            getView().showErrorTosConfirmation(((InterruptConfirmationHttpException) e).getHref());
                        } else if (((InterruptConfirmationHttpException) e).getType().equalsIgnoreCase(InterruptConfirmationHttpException.TOS_TOKOPEDIA_INTERRUPT)) {
                            getView().showErrorTosConfirmationDialog(
                                    e.getMessage(),
                                    ((InterruptConfirmationHttpException) e).getHref(),
                                    ((InterruptConfirmationHttpException) e).getKey(),
                                    ((InterruptConfirmationHttpException) e).getId()
                            );
                            getView().openInterruptConfirmationDialog(
                                    ((InterruptConfirmationHttpException) e).getHref(),
                                    ((InterruptConfirmationHttpException) e).getKey(),
                                    ((InterruptConfirmationHttpException) e).getId()
                            );
                        } else {
                            String message = e.getMessage();
                            getView().showToastMessage(message);
                            getView().goToProductList();
                        }
                    } else {
                        if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            getView().showToastMessage(getView().getActivity().getString(R.string.error_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            getView().showToastMessage(getView().getActivity().getString(R.string.error_timeout));
                        } else if (e instanceof UnProcessableHttpException) {
                            String message = TextUtils.isEmpty(e.getMessage()) ?
                                    getView().getActivity().getResources().getString(R.string.error_internet_not_connected) :
                                    e.getMessage();
                            getView().showToastMessage(message);
                        } else {
                            getView().showToastMessage(getView().getActivity().getString(R.string.error_default));
                        }
                        getView().goToProductList();
                    }
                }
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (isViewAttached()) {
                    getView().hideProgress();
                    getView().enableConfirmButton();
                    getView().showPromoLayout();

                    float surgeMultiplier = 0;
                    String display = "";
                    String surgeConfirmationHref = null;
                    if (fareEstimate.getEstimate() != null) {
                        surgeMultiplier = fareEstimate.getEstimate().getSurgeMultiplier();
                        display = fareEstimate.getEstimate().getDisplay();
                        surgeConfirmationHref = fareEstimate.getEstimate().getSurgeConfirmationHref();
                    } else {
                        display = fareEstimate.getFare().getDisplay();
                    }

                    getView().renderFareEstimate(fareEstimate.getFare().getFareId(), display, fareEstimate.getFare().getValue(), surgeMultiplier, surgeConfirmationHref, fareEstimate.getCode(), fareEstimate.getMessageSuccess());
                }

            }
        });
    }

    /**
     * Get payment method list
     */
    @Override
    public void getPaymentMethodList() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPaymentMethodListUseCase.PARAM_PAYMENT_METHOD, PaymentMode.CC);
        getPaymentMethodListUseCase.execute(requestParams, new Subscriber<PaymentMethodList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().showErrorMessage(e.getMessage());
            }

            @Override
            public void onNext(PaymentMethodList paymentMethodList) {
                //find the active payment method
                PaymentMethod selectedPaymentMethod = null;
                if (paymentMethodList != null && paymentMethodList.getPaymentMethods() != null) {

                    for (PaymentMethod paymentMethod : paymentMethodList.getPaymentMethods()) {
                        if (paymentMethod.getActive() == true) {
                            selectedPaymentMethod = paymentMethod;
                            break;
                        }
                    }
                }

                if (selectedPaymentMethod != null) {
                    getView().showPaymentMethod(selectedPaymentMethod.getLabel(), selectedPaymentMethod.getCardTypeImage());
                    if (selectedPaymentMethod.getMode().equalsIgnoreCase(PaymentMode.WALLET)) {
                        fetchTokoCashBalance();
                    } else {
                        getView().hideTokoCashBalance();
                    }
                } else {
                    getView().hidePaymentMethod();
                }
            }
        });
    }

    /**
     * This function fetches the tokocash balance and update on UI
     */
    private void fetchTokoCashBalance() {
        if (!isViewAttached() || getView().getActivity() == null) {
            return;
        }

        //first try to fetch from cache, if found then return
        try {
            GlobalCacheManager cacheManager = new GlobalCacheManager();
            String cache = cacheManager.getValueString(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);

            if (cache != null) {
                TokoCashModel tokoCashModel = CacheUtil.convertStringToModel(cache, new TypeToken<TokoCashModel>() {
                }.getType());

                if (tokoCashModel != null
                        && tokoCashModel.isSuccess()
                        && tokoCashModel.getTokoCashData() != null
                        && tokoCashModel.getTokoCashData().getLink() == 1) {
                    CommonUtils.dumper("ConfirmBookingPresenter tokocash balance == " + tokoCashModel.getTokoCashData().getBalance());

                    tokoCashBalance = "(" + tokoCashModel.getTokoCashData().getBalance() + ")";

                    //show tokocash balance
                    getView().showTokoCashBalance(tokoCashBalance);
                    return;
                }
            }
        } catch (Exception ex) {
        }


        SessionHandler sessionHandler = new SessionHandler(getView().getActivity());

        Bundle bundle = new Bundle();
        String authKey = sessionHandler.getAccessToken(getView().getActivity());
        authKey = BEARER_TOKEN + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountsService = new AccountsService(bundle);
        GlobalCacheManager walletCache = new GlobalCacheManager();

        TokoCashSourceFactory tokoCashSourceFactory = new TokoCashSourceFactory(
                getView().getActivity(),
                accountsService,
                new TokoCashMapper(),
                walletCache);


        TokoCashRepository tokoCashRepository = new TokoCashRepositoryImpl(tokoCashSourceFactory);
        TokoCashUseCase tokoCashUseCase = new TokoCashUseCase(
                new JobExecutor(),
                new UIThread(),
                tokoCashRepository
        );


        tokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<TokoCashModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("ConfirmBookingPresenter :: inside tokocash subscriber error");
            }

            @Override
            public void onNext(TokoCashModel tokoCashModel) {
                if (tokoCashModel != null
                        && tokoCashModel.isSuccess()
                        && tokoCashModel.getTokoCashData() != null
                        && tokoCashModel.getTokoCashData().getLink() == 1) {
                    CommonUtils.dumper("ConfirmBookingPresenter :: tokocash balance == " + tokoCashModel.getTokoCashData().getBalance());

                    tokoCashBalance = "(" + tokoCashModel.getTokoCashData().getBalance() + ")";

                    //show tokocash balance
                    if (isViewAttached()) {
                        getView().showTokoCashBalance(tokoCashBalance);
                    }
                }
            }
        });
    }

    /**
     * Get payment method list from cache
     */
    @Override
    public void getPaymentMethodListFromCache() {
        getPaymentMethodListCacheUseCase.execute(RequestParams.EMPTY, new Subscriber<PaymentMethodList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hidePaymentMethod();
            }

            @Override
            public void onNext(PaymentMethodList paymentMethodList) {
                //find the active payment method
                PaymentMethod selectedPaymentMethod = null;
                if (paymentMethodList != null && paymentMethodList.getPaymentMethods() != null) {

                    for (PaymentMethod paymentMethod : paymentMethodList.getPaymentMethods()) {
                        if (paymentMethod.getActive() == true) {
                            selectedPaymentMethod = paymentMethod;
                            break;
                        }
                    }
                }

                if (selectedPaymentMethod != null) {
                    getView().showPaymentMethod(selectedPaymentMethod.getLabel(), selectedPaymentMethod.getCardTypeImage());
                } else {
                    getView().hidePaymentMethod();
                }
            }
        });
    }

    @Override
    public void detachView() {
        getFareEstimateUseCase.unsubscribe();
        getPaymentMethodListUseCase.unsubscribe();
        getPaymentMethodListCacheUseCase.unsubscribe();
        super.detachView();
    }
}
