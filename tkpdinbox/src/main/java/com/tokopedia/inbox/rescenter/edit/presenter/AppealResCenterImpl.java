package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.edit.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.edit.listener.AppealResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.AppealResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/31/16.
 */
public class AppealResCenterImpl implements AppealResCenterPresenter {

    private final AppealResCenterListener listener;
    private final RetrofitInteractor retrofit;

    public AppealResCenterImpl(AppealResCenterListener listener) {
        this.listener = listener;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void setOnLaunching(@NonNull Context context) {
        listener.setLoading(false);
        listener.setMainView(true);

        getAppealResCenterFormData(
                listener.getBaseContext(),
                listener.getPassData()
        );
    }

    private void getAppealResCenterFormData(Context context,
                                            ActionParameterPassData passData) {
        retrofit.getAppealResolutionForm(context,
                NetworkParam.paramAppealResCenter(passData),
                new RetrofitInteractor.GetAppealResolutionFormListener() {
                    @Override
                    public void onStart() {
                        listener.setLoading(true);
                        listener.setMainView(false);
                    }

                    @Override
                    public void onSuccess(AppealResCenterFormData formData) {
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

    private ActionParameterPassData storeFormData(AppealResCenterFormData formData) {
        ActionParameterPassData passData = listener.getPassData();
        passData.setAppealFormData(formData);
        return passData;
    }

    @Override
    public void renderView(AppealResCenterFormData formData) {
        listener.getSolutionView().renderData(formData);
        listener.renderShop(formData);
        listener.renderInvoice(formData);
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
        passData.setSolutionChoosen(listener.getSolutionView().getSolutionChoosen());
        passData.setRefund(listener.getSolutionView().getRefundBox().getText().toString());
        passData.setReplyMsg(listener.getSolutionView().getMessageBox().getText().toString());
        listener.setPassData(passData);
    }

    private boolean isValid(Context context) {
        EditResCenterFormData.SolutionData solutionDataChoosen = listener.getSolutionView().getSolutionChoosen();

        if (listener.getSolutionView().getSolutionChoosen() == null) {
            listener.showErrorMessage(context.getString(R.string.error_choose_solution));
            return false;
        }

        if (solutionDataChoosen.getRefundType() != 0 &&
                (listener.getSolutionView().getRefundBox().getText().toString().isEmpty())) {
            listener.getSolutionView().getRefundBox().setError(context.getString(R.string.error_field_required));
            return false;
        }

        if (solutionDataChoosen.getRefundType() != 0
                && (Integer.parseInt(listener.getSolutionView().getRefundBox().getText().toString()) > solutionDataChoosen.getMaxRefund())) {
            listener.getSolutionView().getRefundBox().setError(context.getString(R.string.error_max_refund_rescenter).replace("XYS", solutionDataChoosen.getMaxRefundIdr()));
            return false;
        }

        if (listener.getSolutionView().getMessageBox().getText().toString().isEmpty()) {
            listener.getSolutionView().getMessageBox().setError(context.getString(R.string.error_field_required));
            return false;
        }

        if (listener.getSolutionView().getMessageBox().getText().toString().length() < 10) {
            listener.getSolutionView().getMessageBox().setError(context.getString(R.string.error_min_10));
            return false;
        }

        return true;
    }

    private void postEditResolution(Context context) {
        retrofit.postAppealResolution(context,
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
