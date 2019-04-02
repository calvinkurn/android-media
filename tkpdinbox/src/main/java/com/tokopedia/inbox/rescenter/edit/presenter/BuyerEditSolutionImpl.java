package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;

import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.edit.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditSolutionListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;

import java.util.List;
import java.util.Map;

/**
 * Created on 8/28/16.
 */
public class BuyerEditSolutionImpl implements BuyerEditSolutionPresenter {

    private final BuyerEditSolutionListener listener;
    private final RetrofitInteractor retrofit;

    public BuyerEditSolutionImpl(BuyerEditSolutionListener listener) {
        this.listener = listener;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void setOnSubmitClick(Context context) {
        if (isValid(context)) {
            postEditResolution(context);
        }
    }

    private void postEditResolution(Context context) {
        retrofit.postEditResolution(context,
                listener.getActionParameterPassData(),
                new RetrofitInteractor.ResultEditResolutionListener() {
                    @Override
                    public void onStart() {
                        listener.showLoading(true);
                        listener.showMainView(false);
                    }

                    @Override
                    public void onSuccess() {
                        listener.setActivityResult();
                        listener.finish();
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener clickedListener) {
                        listener.showLoading(false);
                        listener.showMainView(true);
                        listener.showTimeOut(clickedListener);
                    }

                    @Override
                    public void onError(String message) {
                        listener.showLoading(false);
                        listener.showMainView(true);
                        listener.showErrorMessage(message);
                    }

                    @Override
                    public void onFailAuth() {
                        listener.showLoading(false);
                        listener.showMainView(true);
                        listener.showTimeOut(null);
                    }
                });
    }

    private boolean isValid(Context context) {
        EditResCenterFormData.SolutionData solutionDataChoosen = listener.getSolutionSectionView().getSolutionChoosen();

        if (listener.getSolutionSectionView().getSolutionChoosen() == null) {
            listener.showErrorMessage(context.getString(R.string.error_choose_solution));
            return false;
        }

        if (solutionDataChoosen.getRefundType() != 0 &&
                (listener.getSolutionSectionView().getRefundBox().getText().toString().isEmpty())) {
            listener.getSolutionSectionView().getRefundBox().setError(context.getString(R.string.error_field_required));
            listener.getSolutionSectionView().getRefundBox().requestFocus();
            return false;
        }

        if (solutionDataChoosen.getRefundType() != 0
                && (Integer.parseInt(listener.getSolutionSectionView().getRefundBox().getText().toString()) > solutionDataChoosen.getMaxRefund())) {
            listener.getSolutionSectionView().getRefundBox().setError(context.getString(R.string.error_max_refund_rescenter).replace("XYS", solutionDataChoosen.getMaxRefundIdr()));
            listener.getSolutionSectionView().getRefundBox().requestFocus();
            return false;
        }

        if (listener.getMessageView().getMessageBox().getText().toString().isEmpty()) {
            listener.getMessageView().getMessageBox().setError(context.getString(R.string.error_field_required));
            listener.getMessageView().getMessageBox().requestFocus();
            return false;
        }

        if (listener.getMessageView().getMessageBox().getText().toString().length() < 10) {
            listener.getMessageView().getMessageBox().setError(context.getString(R.string.error_min_10));
            listener.getMessageView().getMessageBox().requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onFirstTimeLaunched(Context context, final ActionParameterPassData passData) {
        listener.showLoading(true);
        listener.showMainView(false);
        retrofit.getSolution(context,
                generateParams(passData),
                new RetrofitInteractor.FormSolutionListener() {
                    @Override
                    public void onSuccess(List<EditResCenterFormData.SolutionData> solutionDataList) {
                        listener.storeSolutionDataList(solutionDataList);
                        listener.renderSolutionSpinner();
                        listener.showAttachmentView(passData.getTroubleCategoryChoosen().getAttachment() == 1);
                        listener.showLoading(false);
                        listener.showMainView(true);
                    }

                    @Override
                    public void onTimeout(NetworkErrorHelper.RetryClickedListener clickedListener) {
                        listener.showLoading(false);
                        listener.showTimeOutFull(clickedListener);
                    }

                    @Override
                    public void onError(String error) {
                        listener.showLoading(false);
                        listener.showErrorMessageFull(error);
                    }

                    @Override
                    public void onNullData() {
                        listener.showLoading(false);
                        listener.showTimeOutFull(null);
                    }
                });
    }

    private Map<String, String> generateParams(ActionParameterPassData passData) {
        if (passData.getProductTroubleChoosenList() == null || passData.getProductTroubleChoosenList().isEmpty()) {
            return NetworkParam.getSolutionNonProductRelatedParam(passData);
        } else {
            return NetworkParam.getSolutionParam(passData);
        }
    }

    @Override
    public void unsubscribe() {
        retrofit.unsubscribe();
    }
}
