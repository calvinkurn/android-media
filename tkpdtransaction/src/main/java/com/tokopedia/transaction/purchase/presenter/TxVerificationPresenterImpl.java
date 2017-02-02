package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;

import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.UploadImageReVamp;
import com.tokopedia.transaction.purchase.activity.ConfirmPaymentActivity;
import com.tokopedia.transaction.purchase.activity.TxVerDetailActivity;
import com.tokopedia.transaction.purchase.fragment.TxVerificationFragment;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractor;
import com.tokopedia.transaction.purchase.interactor.TxOrderNetInteractorImpl;
import com.tokopedia.transaction.purchase.interactor.TxUploadInteractor;
import com.tokopedia.transaction.purchase.interactor.TxUploadInteractorImpl;
import com.tokopedia.transaction.purchase.listener.TxVerViewListener;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;
import com.tokopedia.transaction.purchase.model.response.txverification.TxVerListData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angga.Prasetiyo on 24/05/2016.
 */
public class TxVerificationPresenterImpl implements TxVerificationPresenter {
    private final TxVerViewListener viewListener;
    private final TxOrderNetInteractorImpl netInteractor;
    private final TxUploadInteractor txUploadInteractor;

    public TxVerificationPresenterImpl(TxVerViewListener verViewListener) {
        this.viewListener = verViewListener;
        this.netInteractor = new TxOrderNetInteractorImpl();
        this.txUploadInteractor = new TxUploadInteractorImpl();
    }

    @Override
    public void getPaymentVerification(Context context, int page, final int typeRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("per_page", "10");
        viewListener.showProcessGetData(typeRequest);
        netInteractor.getPaymentVerificationList(context, params,
                new TxOrderNetInteractor.OnGetPaymentVerificationList() {

                    @Override
                    public void onSuccess(TxVerListData data) {
                        PagingHandler.PagingHandlerModel paging = data.getPaging();
                        boolean hasNext = paging != null && PagingHandler.CheckHasNext(paging);
                        viewListener.renderDataList(data.getTxVerDataList(),
                                hasNext, typeRequest);
                    }

                    @Override
                    public void onError(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showFailedResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showFailedPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showFailedLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onNoConnection(String message) {
                        switch (typeRequest) {
                            case TxOrderNetInteractor.TypeRequest.INITIAL:
                                viewListener.showNoConnectionResetData(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.PULL_REFRESH:
                                viewListener.showNoConnectionPullRefresh(message);
                                break;
                            case TxOrderNetInteractor.TypeRequest.LOAD_MORE:
                                viewListener.showNoConnectionLoadMoreData(message);
                                break;
                        }
                    }

                    @Override
                    public void onEmptyData() {
                        viewListener.showEmptyData(typeRequest);
                    }
                });

    }

    @Override
    public void processEditPayment(Context context, TxVerData data) {
        viewListener.navigateToActivityRequest(ConfirmPaymentActivity.instanceEdit(context,
                data.getPaymentId()),
                TxVerDetailActivity.REQUEST_EDIT_PAYMENT);
        // viewListener.navigateToActivityRequest(intent, 1);
    }

    @Override
    public void uploadProofImage(final Context context, UploadImageReVamp uploadImageHandler,
                                 TxVerData data) {
        uploadImageHandler.addParam("payment_id", data.getPaymentId());
        uploadImageHandler.setOnUploadListener(new UploadImageReVamp.UploadImageListener() {
            @Override
            public void onSuccess(JSONObject result) {
                String picSrc = result.optString("pic_src");
                String picObj = result.optString("pic_obj");
                uploadImageFile(context, picObj, picSrc);
            }

            @Override
            public void onStart() {
                viewListener.showProgressLoading();
            }

            @Override
            public void onFailure() {
                viewListener.hideProgressLoading();
            }
        });
        uploadImageHandler.actionPickImage();
    }

    @Override
    public void processToTxVerificationDetail(Context context, TxVerData data) {
        viewListener.navigateToActivityRequest(TxVerDetailActivity.createInstance(context, data),
                TxVerificationFragment.REQUEST_VERIFICATION_DETAIL);
    }

    @Override
    public void uploadProofImageWSV4(Context context, String imagePath, TxVerData txVerData) {
        if (imagePath == null || imagePath.isEmpty()) {
            viewListener.showToastMessage(context.getString(
                    com.tokopedia.transaction.R.string.message_failed_pick_image)
            );
            return;
        }
        viewListener.showProgressLoading();
        txUploadInteractor.uploadImageProof(context, imagePath, txVerData,
                new TxUploadInteractor.OnImageProofUpload() {
                    @Override
                    public void onSuccess(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                        viewListener.resetData();
                    }

                    @Override
                    public void onFailed(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        netInteractor.unSubscribeObservable();
        txUploadInteractor.unSubscribeObservable();
    }

    private void uploadImageFile(final Context context, String picObj,
                                 String picSrc) {
        Map<String, String> params = new HashMap<>();
        params.put("pic_obj", picObj);
        params.put("pic_src", picSrc);
        netInteractor.uploadValidProofByPayment(context, params,
                new TxOrderNetInteractor.OnUploadProof() {
                    @Override
                    public void onSuccess(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                        viewListener.resetData();
                    }

                    @Override
                    public void onFailed(String message) {
                        viewListener.hideProgressLoading();
                        viewListener.showToastMessage(message);
                    }
                });

    }
}
