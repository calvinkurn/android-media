package com.tokopedia.transaction.purchase.presenter;

import android.app.Activity;

import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.exception.HttpErrorException;
import com.tokopedia.transaction.exception.ResponseErrorException;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.transaction.purchase.interactor.TxUploadInteractor;
import com.tokopedia.transaction.purchase.interactor.TxUploadInteractorImpl;
import com.tokopedia.transaction.purchase.listener.TxVerDetailViewListener;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;
import com.tokopedia.transaction.purchase.model.response.txverinvoice.TxVerInvoiceData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author Angga.Prasetiyo on 13/06/2016.
 */
public class TxVerDetailPresenterImpl implements TxVerDetailPresenter {
    private final TxVerDetailViewListener viewListener;
    private final TxOrderNetInteractor netInteractor;
    private TxUploadInteractor txUploadInteractor;

    public TxVerDetailPresenterImpl(TxVerDetailViewListener viewListener) {
        this.viewListener = viewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
        this.txUploadInteractor = new TxUploadInteractorImpl();
    }

    @Override
    public void getTxInvoiceData(String paymentId) {
        TKPDMapParam<String, String> paramNetwork = new TKPDMapParam<>();
        paramNetwork.put("payment_id", paymentId);
        netInteractor.getInvoiceData(
                viewListener.getGeneratedAuthParamNetwork(paramNetwork),
                new SubscriberGetTXInvoiceData()
        );
    }

    @Override
    public int getTypePaymentMethod(TxVerData data) {
        if (data.getBankName().contains("Klik") && data.getBankName().contains("BCA")) {
            return 1;
        } else {
            return data.getButton().getButtonEditPayment() == 0 &&
                    data.getButton().getButtonUploadProof() == 0 &&
                    data.getButton().getButtonViewProof() == 0 ? 2 : 3;
        }
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
    }

    @Override
    public void uploadProofImageWSV4(Activity activity, String imagePath, TxVerData txVerData) {
        if (imagePath == null || imagePath.isEmpty()) {
            viewListener.showToastMessage(activity.getString(
                    com.tokopedia.transaction.R.string.message_failed_pick_image)
            );
            return;
        }
        viewListener.showProgressLoading();
        txUploadInteractor.uploadImageProof(activity, imagePath, txVerData,
                new TxUploadInteractor.OnImageProofUpload() {
                    @Override
                    public void onSuccess(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }

                    @Override
                    public void onFailed(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }
                });
    }

    private class SubscriberGetTXInvoiceData extends Subscriber<TxVerInvoiceData> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof SocketTimeoutException) {
                viewListener.renderErrorGetInvoiceData(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
            } else if (e instanceof UnknownHostException) {
                viewListener.renderErrorGetInvoiceData(
                        ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                );
            } else if (e.getCause() instanceof ResponseErrorException) {
                viewListener.renderErrorGetInvoiceData(e.getCause().getMessage());
            } else if (e.getCause() instanceof HttpErrorException) {
                viewListener.renderErrorGetInvoiceData(e.getCause().getMessage());
            } else {
                viewListener.renderErrorGetInvoiceData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        }

        @Override
        public void onNext(TxVerInvoiceData txVerInvoiceData) {
            viewListener.renderInvoiceList(txVerInvoiceData.getTxOrderDetail().getDetail());
        }
    }
}
