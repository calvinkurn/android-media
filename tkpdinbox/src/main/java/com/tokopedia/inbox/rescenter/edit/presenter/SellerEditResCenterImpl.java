package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.edit.listener.SellerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/29/16.
 */
public class SellerEditResCenterImpl implements SellerEditResCenterPresenter {

    private final SellerEditResCenterListener listener;
    private final RetrofitInteractor retrofit;

    public SellerEditResCenterImpl(SellerEditResCenterListener listener) {
        this.listener  = listener;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void setOnLaunching(@NonNull Context context) {
        listener.setLoading(false);
        listener.setMainView(true);

        getEditResCenterFormData(
                listener.getBaseContext(),
                listener.getDetailData(),
                listener.getDetailData().getDetail().getResolutionLast().getLastFlagReceived()
        );
    }

    private void getEditResCenterFormData(Context context,
                                          DetailResCenterData detailData,
                                          Integer lastFlagReceived) {
        retrofit.getEditResolutionForm(context,
                NetworkParam.paramEditResCenter(detailData, lastFlagReceived),
                new RetrofitInteractor.GetEditResolutionFormListener() {
                    @Override
                    public void onStart() {
                        listener.setLoading(true);
                        listener.setMainView(false);
                    }

                    @Override
                    public void onSuccess(EditResCenterFormData formData) {
                        listener.setLoading(false);
                        listener.setMainView(true);
                        listener.setPassData(storeFormData(formData));
                        renderView(formData);
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener rcListener) {
                        listener.setLoading(false);
                        listener.setMainView(false);
                        listener.setTimeOutView(rcListener);
                    }

                    @Override
                    public void onError(String message) {
                        listener.setLoading(false);
                        listener.setMainView(false);
                        listener.setErrorView(message);
                    }

                    @Override
                    public void onFailAuth() {
                        listener.setLoading(false);
                        listener.setMainView(false);
                        listener.setTimeOutView(null);
                    }
                });
    }

    private ActionParameterPassData storeFormData(EditResCenterFormData formData) {
        ActionParameterPassData passData = listener.getPassData();
        passData.setFormData(formData);
        return passData;
    }

    @Override
    public void renderView(EditResCenterFormData formData) {
        listener.renderInvoice(formData);
        listener.renderShop(formData);
        listener.renderSummary(formData);
        listener.renderSolution(formData);
    }

    @Override
    public void setOnButtonNextClick(@NonNull Context context) {
        if(isValid(context)) {
            collectData();
            postEditResolution(context);
        }
    }

    private void collectData() {
        ActionParameterPassData passData = listener.getPassData();
        passData.setAttachmentData(listener.getAttachmentData());
        passData.setSolutionChoosen(listener.getEditSolutionSellerView().getSolutionChoosen());
        passData.setRefund(listener.getEditSolutionSellerView().getRefundBox().getText().toString());
        passData.setReplyMsg(listener.getEditSolutionSellerView().getMessageBox().getText().toString());
        listener.setPassData(passData);
    }

    private boolean isValid(Context context) {
        EditResCenterFormData.SolutionData solutionDataChoosen = listener.getEditSolutionSellerView().getSolutionChoosen();

        if (listener.getEditSolutionSellerView().getSolutionChoosen() == null) {
            listener.showErrorMessage(context.getString(R.string.error_choose_solution));
            return false;
        }

        if (solutionDataChoosen.getRefundType() != 0 &&
                (listener.getEditSolutionSellerView().getRefundBox().getText().toString().isEmpty())) {
            listener.getEditSolutionSellerView().getRefundBox().setError(context.getString(R.string.error_field_required));
            return false;
        }

        if (solutionDataChoosen.getRefundType() != 0
                && (Integer.parseInt(listener.getEditSolutionSellerView().getRefundBox().getText().toString()) > solutionDataChoosen.getMaxRefund())) {
            listener.getEditSolutionSellerView().getRefundBox().setError(context.getString(R.string.error_max_refund_rescenter).replace("XYS", solutionDataChoosen.getMaxRefundIdr()));
            return false;
        }

        if (listener.getEditSolutionSellerView().getMessageBox().getText().toString().isEmpty()) {
            listener.getEditSolutionSellerView().getMessageBox().setError(context.getString(R.string.error_field_required));
            return false;
        }

        if (listener.getEditSolutionSellerView().getMessageBox().getText().toString().length() < 10) {
            listener.getEditSolutionSellerView().getMessageBox().setError(context.getString(R.string.error_min_10));
            return false;
        }

        return true;
    }

    private void postEditResolution(Context context) {
        retrofit.postEditSellerResolution(context,
                listener.getPassData(),
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

    @Override
    public void unsubscribe() {
        retrofit.unsubscribe();
    }
}
