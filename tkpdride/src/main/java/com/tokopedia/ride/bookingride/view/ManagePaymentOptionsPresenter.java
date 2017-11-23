package com.tokopedia.ride.bookingride.view;

import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.adapter.Visitable;
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
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetPaymentMethodListCacheUseCase;
import com.tokopedia.ride.bookingride.domain.GetPaymentMethodListUseCase;
import com.tokopedia.ride.bookingride.domain.RequestApiUseCase;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.common.ride.data.PaymentMethodListCacheImpl;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Vishal on 25th Oct, 2017.
 */

public class ManagePaymentOptionsPresenter extends BaseDaggerPresenter<ManagePaymentOptionsContract.View>
        implements ManagePaymentOptionsContract.Presenter {

    private static final String BEARER_TOKEN = "Bearer ";

    private RequestApiUseCase saveUrlUseCase;
    private GetPaymentMethodListUseCase getPaymentMethodListUseCase;
    private GetPaymentMethodListCacheUseCase getPaymentMethodListCacheUseCase;
    private PaymentMethodList paymentMethodList;
    private String tokoCashBalance;
    private ArrayList<Visitable> visitables;

    @Inject
    public ManagePaymentOptionsPresenter(GetPaymentMethodListUseCase getPaymentMethodListUseCase, GetPaymentMethodListCacheUseCase getPaymentMethodListCacheUseCase, RequestApiUseCase deleteCreditCardUseCase) {
        this.getPaymentMethodListUseCase = getPaymentMethodListUseCase;
        this.getPaymentMethodListCacheUseCase = getPaymentMethodListCacheUseCase;
        this.saveUrlUseCase = deleteCreditCardUseCase;
    }

    @Override
    public void fetchPaymentMethodList() {
        getPaymentMethodsFromCache();
        getPaymentMethodsFromCloud();
        fetchTokoCashBalance();
    }

    @Override
    public void getPaymentMethodsFromCloud() {
        getView().showProgress();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPaymentMethodListUseCase.PARAM_PAYMENT_METHOD, "cc");
        getPaymentMethodListUseCase.execute(requestParams, new Subscriber<PaymentMethodList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("ManagePaymentOptionsPresenter :: getPaymentMethodsFromCloud onError");
                e.printStackTrace();
                getView().hideProgress();
                getView().showErrorMessage(e.getMessage());
            }

            @Override
            public void onNext(PaymentMethodList paymentMethodList) {
                CommonUtils.dumper("ManagePaymentOptionsPresenter :: getPaymentMethodsFromCloud onNext");
                renderPaymentMethodList(paymentMethodList);
            }
        });
    }

    private void getPaymentMethodsFromCache() {
        getPaymentMethodListCacheUseCase.execute(RequestParams.EMPTY, new Subscriber<PaymentMethodList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(PaymentMethodList paymentMethodList) {
                renderPaymentMethodList(paymentMethodList);
            }
        });
    }

    private void renderPaymentMethodList(PaymentMethodList paymentMethodList) {
        ManagePaymentOptionsPresenter.this.paymentMethodList = paymentMethodList;
        visitables = new ArrayList<>();


        if (paymentMethodList != null && paymentMethodList.getPaymentMethods() != null) {

            for (PaymentMethod paymentMethod : paymentMethodList.getPaymentMethods()) {
                PaymentMethodViewModel visitable = new PaymentMethodViewModel();
                visitable.setActive(paymentMethod.getActive());
                visitable.setName(paymentMethod.getLabel());
                visitable.setType(paymentMethod.getMode());
                visitable.setImageUrl(paymentMethod.getImage());
                visitable.setExpiryMonth(paymentMethod.getExpiryMonth());
                visitable.setExpiryYear(paymentMethod.getExpiryYear());
                visitable.setDeleteUrl(paymentMethod.getDeleteUrl());
                visitable.setDeleteBody(paymentMethod.getRemoveBody());
                visitable.setSaveurl(paymentMethod.getSaveUrl());
                visitable.setSaveBody(paymentMethod.getSaveBody());
                visitable.setCardType(paymentMethod.getCardType());
                visitable.setSaveWebView(paymentMethod.isSaveWebView());
                visitable.setBankImage(paymentMethod.getBankImage());

                if (tokoCashBalance != null && paymentMethod.getMode() != null && paymentMethod.getMode().equalsIgnoreCase(PaymentMethodViewModel.MODE_WALLET)) {
                    visitable.setTokoCashBalance(tokoCashBalance);
                }

                visitables.add(visitable);
            }
        }

        if (getView() != null) {
            getView().hideProgress();
            getView().renderPaymentMethodList(visitables);
        }
    }

    @Override
    public void addCreditCard() {
        if (paymentMethodList != null && paymentMethodList.getAddPayment() != null) {

            getView().opeScroogePage(paymentMethodList.getAddPayment().getSaveUrl(), true, paymentMethodList.getAddPayment().getSaveBody());
        }
    }

    @Override
    public void deletePaymentMethodCache() {
        PaymentMethodListCacheImpl cache = new PaymentMethodListCacheImpl();
        cache.evictAll();
    }

    @Override
    public void selectPaymentOption(final PaymentMethodViewModel paymentMethodViewModel) {
        if (paymentMethodViewModel.isSaveWebView()) {
            getView().showAutoDebitDialog(paymentMethodViewModel);
            //getView().opeScroogePage(paymentMethodViewModel.getSaveurl(), true, paymentMethodViewModel.getSaveBody());
        } else {
            getView().showProgressBar();
            saveUrlUseCase.setUrl(paymentMethodViewModel.getSaveurl());
            RequestParams params = RequestParams.create();

            //create params from bundle
            Bundle bundle = paymentMethodViewModel.getSaveBody();
            Set<String> set = bundle.keySet();
            for (String key : set) {
                params.putString(key, bundle.getString(key));
            }

            saveUrlUseCase.execute(params, new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();

                    if (isViewAttached()) {
                        getView().hideProgressBar();
                        String error;
                        if (e instanceof UnknownHostException) {
                            error = getView().getActivity().getString(R.string.error_no_connection);
                        } else if (e instanceof SocketTimeoutException) {
                            error = getView().getActivity().getString(R.string.error_timeout);
                        } else {
                            error = getView().getActivity().getString(R.string.error_default);
                        }
                        getView().showErrorMessage(e.getMessage());
                    }
                }

                @Override
                public void onNext(String s) {
                    if (isViewAttached() && getView().getActivity() != null) {
                        getView().hideProgressBar();
                        getView().closeActivity(paymentMethodViewModel);
                    }
                }
            });
        }
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
                    CommonUtils.dumper("tokocash balance == " + tokoCashModel.getTokoCashData().getBalance());

                    tokoCashBalance = tokoCashModel.getTokoCashData().getBalance();

                    //show tokocash balance
                    showTokoCashBalance(tokoCashBalance);
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
                CommonUtils.dumper("ManagePaymentOptionsPresenter :: inside tokocash subscriber error");
            }

            @Override
            public void onNext(TokoCashModel tokoCashModel) {
                if (tokoCashModel != null
                        && tokoCashModel.isSuccess()
                        && tokoCashModel.getTokoCashData() != null
                        && tokoCashModel.getTokoCashData().getLink() == 1) {
                    CommonUtils.dumper("ManagePaymentOptionsPresenter :: tokocash balance == " + tokoCashModel.getTokoCashData().getBalance());

                    tokoCashBalance = tokoCashModel.getTokoCashData().getBalance();

                    //show tokocash balance
                    showTokoCashBalance(tokoCashBalance);
                }
            }
        });
    }

    /**
     * This function updates the list and render the List again
     *
     * @param tokoCashBalance
     */
    private void showTokoCashBalance(String tokoCashBalance) {
        if (isViewAttached() && visitables != null) {

            for (Visitable visitable : visitables) {
                if (visitable instanceof PaymentMethodViewModel) {
                    PaymentMethodViewModel model = (PaymentMethodViewModel) visitable;

                    if (model != null && model.getType() != null && model.getType().equalsIgnoreCase(PaymentMethodViewModel.MODE_WALLET)) {
                        model.setTokoCashBalance(tokoCashBalance);

                        getView().renderPaymentMethodList(visitables);
                        return;
                    }
                }
            }
        }
    }


}
