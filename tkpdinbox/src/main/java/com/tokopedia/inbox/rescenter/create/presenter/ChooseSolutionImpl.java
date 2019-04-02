package com.tokopedia.inbox.rescenter.create.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.create.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.create.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.create.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.create.listener.ChooseSolutionListener;
import com.tokopedia.inbox.rescenter.create.listener.CreateResCenterListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;
import com.tokopedia.inbox.rescenter.create.service.CreateResCenterService;

import java.util.List;
import java.util.Map;

/**
 * Created on 6/16/16.
 */
public class ChooseSolutionImpl implements ChooseSolutionPresenter {

    @SuppressWarnings("unused")
    private static final String TAG = ChooseSolutionImpl.class.getSimpleName();
    private final ChooseSolutionListener listener;
    private final RetrofitInteractorImpl retrofit;

    public ChooseSolutionImpl(ChooseSolutionListener listener) {
        this.listener = listener;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void onFirstTimeLaunched(@NonNull Context context, final ActionParameterPassData passData) {
        listener.showLoading(true);
        listener.showMainView(false);
        retrofit.getSolution(context,
                generateParams(passData),
                new RetrofitInteractor.FormSolutionListener() {
                    @Override
                    public void onSuccess(List<CreateResCenterFormData.SolutionData> solutionDataList) {
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

                    }
                });
    }

    private Map<String, String> generateParams(ActionParameterPassData passData) {
        if (passData.getTroubleCategoryChoosen().getProductRelated() == 0) {
            return NetworkParam.getSolutionNonProductRelatedParam(passData);
        } else {
            return NetworkParam.getSolutionParam(passData);
        }
    }

    @Override
    public void setOnSubmitClick(@NonNull Context context) {
        listener.setRefundError(null);
        if (isValid(context)) {
            listener.showLoading(true);
            listener.showMainView(false);
            createResolutionCenter(context, listener.collectInputData());
        }
    }

    private boolean isValid(Context context) {
        if (listener.collectInputData().getSolutionChoosen() == null) {
            listener.showErrorMessage(context.getString(R.string.error_choose_solution));
            return false;
        }

        if (listener.collectInputData().getSolutionChoosen().getRefundType() != 0
                && (listener.collectInputData().getRefund() == null || listener.collectInputData().getRefund().isEmpty())) {
            listener.setRefundError(context.getString(R.string.error_field_required));
            return false;
        }

        if (listener.collectInputData().getSolutionChoosen().getRefundType() != 0
                && (Integer.parseInt(listener.collectInputData().getRefund()) > listener.collectInputData().getSolutionChoosen().getMaxRefund())) {
            listener.setRefundError(context.getString(R.string.error_max_refund_rescenter).replace("XYS", listener.collectInputData().getSolutionChoosen().getMaxRefundIdr()));
            return false;
        }

        return true;
    }

    private void createResolutionCenter(Context context, ActionParameterPassData passData) {
        CreateResCenterListener listener = (CreateResCenterListener) context;
        listener.startCreateResCenterService(passData);
    }

    @Override
    public void processResultCreateResCenter(int resultCode, Bundle resultData) {
        if (resultCode == CreateResCenterService.RESULT_SUCCESS) {
            listener.successFinish();
        } else if (resultCode == CreateResCenterService.RESULT_ERROR) {
            switch (resultData.getInt(CreateResCenterService.EXTRA_PARAM_STATUS)) {
                case CreateResCenterService.STATUS_TIME_OUT:
                    listener.showLoading(false);
                    listener.showTimeOutFull(null);
                    break;
                case CreateResCenterService.STATUS_ERROR:
                    listener.showLoading(false);
                    listener.showMainView(true);
                    listener.showErrorMessage(resultData.getString(CreateResCenterService.EXTRA_PARAM_ERROR_MESSAGE));
                    break;
                default:
                    throw new UnsupportedOperationException("unsupported status error" +
                            resultData.getInt(CreateResCenterService.EXTRA_PARAM_STATUS));
            }
        } else {
            throw new UnsupportedOperationException("result code not supported " + resultCode);
        }
    }

    @Override
    public void unSubscribe() {
        retrofit.unSubscribe();
    }

    @Override
    public void onAbortCreateResolution() {
        clearAttachment();
        ((Activity) listener.getActivity()).finish();
    }

    private void clearAttachment() {
        for (AttachmentResCenterVersion2DB data : listener.getAttachmentData()) {
            data.delete();
        }
    }
}
