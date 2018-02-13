package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.contract.TokoCashHistoryContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryPresenter extends BaseDaggerPresenter<TokoCashHistoryContract.View> implements TokoCashHistoryContract.Presenter {

    private final GetHistoryDataUseCase getHistoryDataUseCase;
    private int page = 1;

    @Inject
    public TokoCashHistoryPresenter(GetHistoryDataUseCase getHistoryDataUseCase) {
        this.getHistoryDataUseCase = getHistoryDataUseCase;
    }

    @Override
    public void getWaitingTransaction() {
        page = 1;
        getHistoryDataUseCase.execute(getView().getHistoryTokoCashParam(true, page),
                new Subscriber<TokoCashHistoryData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideWaitingTransaction();
                    }

                    @Override
                    public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                        if (tokoCashHistoryData.getItemHistoryList() != null)
                            getView().renderWaitingTransaction(tokoCashHistoryData);
                        else
                            getView().hideWaitingTransaction();
                    }
                });
    }

    @Override
    public void getInitHistoryTokoCash() {
        page = 1;
        getHistoryDataUseCase.execute(getView().getHistoryTokoCashParam(false, page),
                new Subscriber<TokoCashHistoryData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLoading();
                        if (e instanceof ResponseDataNullException) {
                            getView().renderEmptyPage("Empty data list");
                        } else {
                            errorFirstTimeNetworkHandler(e);
                        }
                    }

                    @Override
                    public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                        getView().hideLoading();
                        if (tokoCashHistoryData.getItemHistoryList().size() == 0 && !tokoCashHistoryData.isNext_uri()) {
                            getView().renderDataTokoCashHistory(tokoCashHistoryData, true);
                            getView().renderEmptyTokoCashHistory(tokoCashHistoryData.getHeaderHistory());
                        } else {
                            if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                                getView().renderDataTokoCashHistory(tokoCashHistoryData, true);
                                getView().setHasNextPage(tokoCashHistoryData.isNext_uri());
                                if (tokoCashHistoryData.isNext_uri()) page++;
                            } else {
                                getView().renderEmptyTokoCashHistory(tokoCashHistoryData.getHeaderHistory());
                            }
                        }
                    }
                });
    }

    @Override
    public void getHistoryLoadMore() {
        getHistoryDataUseCase.execute(getView().getHistoryTokoCashParam(false, page),
                new Subscriber<TokoCashHistoryData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        errorNetworkHandler(e);
                    }

                    @Override
                    public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                        if (tokoCashHistoryData.getItemHistoryList().size() > 0) {
                            getView().renderDataTokoCashHistory(tokoCashHistoryData, false);
                        }
                        getView().setHasNextPage(tokoCashHistoryData.isNext_uri());
                        if (tokoCashHistoryData.isNext_uri()) page++;
                    }
                });
    }

    private void errorNetworkHandler(Throwable e) {
//        if (e instanceof UnknownHostException || e instanceof ConnectException) {
//            getView().renderErrorMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
//        } else if (e instanceof SocketTimeoutException) {
//            getView().renderErrorMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
//        } else if (e instanceof ResponseDataNullException) {
//            getView().renderErrorMessage(e.getMessage());
//        } else if (e instanceof HttpErrorException) {
//            getView().renderErrorMessage(e.getMessage());
//        } else if (e instanceof ServerErrorException) {
//            ServerErrorHandlerUtil.handleError(e);
//        } else {
//            getView().renderErrorMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
//        }
    }

    private void errorFirstTimeNetworkHandler(Throwable e) {
//        if (e instanceof UnknownHostException || e instanceof ConnectException) {
//            getView().renderEmptyPage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
//        } else if (e instanceof SocketTimeoutException) {
//            getView().renderEmptyPage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
//        } else if (e instanceof ResponseDataNullException) {
//            getView().renderEmptyPage(e.getMessage());
//        } else if (e instanceof HttpErrorException) {
//            getView().renderEmptyPage(e.getMessage());
//        } else if (e instanceof ServerErrorException) {
//            ServerErrorHandlerUtil.handleError(e);
//        } else {
//            getView().renderEmptyPage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
//        }
    }

    @Override
    public void onDestroyPresenter() {
        if (getHistoryDataUseCase != null) getHistoryDataUseCase.unsubscribe();
    }
}