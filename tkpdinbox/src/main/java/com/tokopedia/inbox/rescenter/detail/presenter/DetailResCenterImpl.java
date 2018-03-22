package com.tokopedia.inbox.rescenter.detail.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.inbox.rescenter.detail.dialog.UploadImageDialog;
import com.tokopedia.inbox.rescenter.detail.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.detail.fragment.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detail.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.detail.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.detail.listener.DetailResCenterView;
import com.tokopedia.inbox.rescenter.detail.listener.ResCenterView;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActivityParamenterPassData;
import com.tokopedia.inbox.rescenter.detail.service.DetailResCenterService;
import com.tokopedia.inbox.rescenter.detail.service.DetailResCenterServiceConstant;
import com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

import java.util.List;

/**
 * Created on 2/9/16.
 */
public class DetailResCenterImpl implements DetailResCenterPresenter {

    private static final String TAG = DetailResCenterImpl.class.getSimpleName();
    private static final int REQUEST_APPEAL_RESOLUTION = 5;
    private static final int REQUEST_INPUT_SHIPPING = 6;
    private static final int REQUEST_EDIT_SHIPPING = 7;
    private static final String CURRENT_DATA = "CURRENT_DATA";
    private static final String CURRENT_DATA_PASS = "CURRENT_DATA_PASS";

    private final DetailResCenterView view;
    private final RetrofitInteractor retrofitInteractor;

    private UploadImageDialog uploadImageDialog;
    private ResCenterView mListener;
    private GlobalCacheManager globalCacheManager;
    private LocalCacheManager.MessageConversation cache;

    public DetailResCenterImpl(DetailResCenterFragment fragment) {
        this.view = fragment;
        this.retrofitInteractor = new RetrofitInteractorImpl();
        this.globalCacheManager = new GlobalCacheManager();
        cache = LocalCacheManager.MessageConversation.Builder(view.getResolutionID()).getCache();
        uploadImageDialog = new UploadImageDialog(fragment, view.getResolutionID());
    }

    @Override
    public void setInteractionListener(ResCenterView listener) {
        mListener = listener;
    }

    @Override
    public void onFirstTimeLaunched(@NonNull Fragment fragment, @NonNull ActivityParamenterPassData passData) {
        view.setProgressLoading(true);
        requestResCenterDetail(fragment.getActivity(), passData);
    }

    @Override
    public void onButtonSendClick(@NonNull Context context, String param) {
        if (!param.trim().isEmpty()) {
            cache.setMessage(param).save();
            view.setErrorComment(null);
            processReply();
        } else {
            view.setErrorComment(context.getString(R.string.error_field_required));
        }
    }

    @Override
    public void onButtonAttachmentClick(Context context) {
        uploadImageDialog.showDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultUploadImage(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_APPEAL_RESOLUTION:
                    view.refreshPage();
                    break;
                case REQUEST_EDIT_SHIPPING:
                    view.refreshPage();
                    break;
                case REQUEST_INPUT_SHIPPING:
                    view.refreshPage();
                    break;
                default:
                    break;
            }
        }
    }

    private void onActivityResultUploadImage(int requestCode, int resultCode, Intent data) {
        uploadImageDialog.onResult(requestCode, resultCode, data,
                new UploadImageDialog.UploadImageDialogListener() {
                    @Override
                    public void onSuccess(List<AttachmentResCenterVersion2DB> data) {
                        view.showAttachment(data);
                        view.setAttachmentArea(true);
                    }

                    @Override
                    public void onFailed() {

                    }
                });
    }

    @Override
    public void processReply() {
        view.setProgressLoading(true);
        mListener.replyConversation(view.getResolutionID());
    }

    @Override
    public void requestResCenterDetail(@NonNull final Context context,
                                       @NonNull final ActivityParamenterPassData activityParamenterPassData) {
        retrofitInteractor.getResCenterDetail(context,
                NetworkParam.paramResCenterDetail(activityParamenterPassData),
                new RetrofitInteractor.ResCenterDetailListener() {

                    @Override
                    public void onSuccess(@NonNull DetailResCenterData data) {
                        storeStateCache(data);
                        view.loadDetailResCenterData(data);
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setFailSaveRespond();
                        view.showTimeOutView(listener);
                    }

                    @Override
                    public void onFailAuth() {
                        view.setFailSaveRespond();
                        view.showErrorView(null);
                    }

                    @Override
                    public void onThrowable(Throwable e) {
                        view.setFailSaveRespond();
                        view.showTimeOutView(null);
                    }

                    @Override
                    public void onError(String message) {
                        view.setFailSaveRespond();
                        view.showErrorView(message);
                    }

                    @Override
                    public void onNullData() {
                        view.setFailSaveRespond();
                        view.showErrorView(null);
                    }
                });
    }

    private void storeStateCache(DetailResCenterData data) {
        LocalCacheManager.StateDetailResCenter.Builder(view.getResolutionID())
                .setByCustomer(data.getDetail().getResolutionBy().getByCustomer())
                .setBySeller(data.getDetail().getResolutionBy().getBySeller())
                .setLastFlagReceived(data.getDetail().getResolutionLast().getLastFlagReceived())
                .setLastTroubleType(data.getDetail().getResolutionLast().getLastTroubleType())
                .setLastTroubleString(data.getDetail().getResolutionLast().getLastTroubleTypeString())
                .setLastSolutionType(data.getDetail().getResolutionLast().getLastSolution())
                .setLastSolutionString(data.getDetail().getResolutionLast().getLastSolutionString())
                .setOrderPriceFmt(data.getDetail().getResolutionOrder().getOrderOpenAmountIdr())
                .setOrderPriceRaw(data.getDetail().getResolutionOrder().getOrderOpenAmount())
                .setShippingPriceFmt(data.getDetail().getResolutionOrder().getOrderShippingPriceIdr())
                .setShippingPriceRaw(data.getDetail().getResolutionOrder().getOrderShippingPrice())
                .save();
    }

    @Override
    public void requestTrackDelivery(@NonNull final Context context,
                                     @NonNull String url) {
        view.showLoadingDialog(true);
        retrofitInteractor.trackShipping(context,
                AuthUtil.generateParams(context, NetworkParam.paramTrackingDelivery(url)),
                new RetrofitInteractor.TrackShippingListener() {
                    @Override
                    public void onSuccess(ResCenterTrackShipping resCenterTrackShipping) {
                        Log.d(TAG, CacheUtil.convertModelToString(resCenterTrackShipping, new TypeToken<ResCenterTrackShipping>() {
                        }.getType()));
                        view.showLoadingDialog(false);
                        if (resCenterTrackShipping.getTrackShipping() != null) {
                            view.showTrackingDialog(resCenterTrackShipping.getTrackShipping());
                        } else {
                            view.showInvalidTrackingDialog();
                        }
                    }

                    @Override
                    public void onTimeOut(String message, NetworkErrorHelper.RetryClickedListener listener) {
                        view.showLoadingDialog(false);
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                        view.showLoadingDialog(false);
                    }

                    @Override
                    public void onThrowable(Throwable e) {
                        view.showLoadingDialog(false);
                    }

                    @Override
                    public void onError(String message) {
                        Log.d(TAG, message);
                        view.showLoadingDialog(false);
                        view.showToastMessage(message);
                    }

                    @Override
                    public void onNullData() {
                        view.showLoadingDialog(false);
                        view.showTimeOutMessage();
                    }
                });

    }

    @Override
    public void onEditShippingClickListener(@NonNull Context context, @NonNull String url) {
        String conversationID = Uri.parse(url).getQueryParameter("conv_id");
        String shippingID = Uri.parse(url).getQueryParameter("ship_id");
        String shippingRefNum = Uri.parse(url).getQueryParameter("ship_ref");
        view.startActivityForResult(
                InputShippingActivity.createEditPageIntentFromDetail(context,
                        view.getResolutionID(),
                        conversationID,
                        shippingID,
                        shippingRefNum
                ),
                REQUEST_EDIT_SHIPPING
        );
    }

    @Override
    public void onNewShippingClickListener(Context context) {
        view.startActivityForResult(InputShippingActivity.createNewPageIntentFromDetail(context, view.getResolutionID()), REQUEST_INPUT_SHIPPING);
    }

    @Override
    public void actionAcceptSolution() {
        view.setProgressLoading(true);
        mListener.actionAcceptSolution(view.getResolutionID());
    }

    @Override
    public void actionAcceptAdminSolution() {
        view.setProgressLoading(true);
        mListener.actionAcceptAdminSolution(view.getResolutionID());
    }

    @Override
    public void actionFinishReturSolution() {
        view.setProgressLoading(true);
        mListener.actionFinishReturSolution(view.getResolutionID());
    }

    @Override
    public void actionCancelResolution() {
        view.setProgressLoading(true);
        mListener.actionCancelResolution(view.getResolutionID());
    }

    @Override
    public void actionReportResolution() {
        view.setProgressLoading(true);
        mListener.actionReportResolution(view.getResolutionID());
    }

    @Override
    public void onReceiveServiceResult(int resultCode, Bundle resultData) {
        int typePostData = resultData.getInt(DetailResCenterService.EXTRA_PARAM_ACTION_TYPE, 0);
        if (resultCode == DetailResCenterServiceConstant.STATUS_FINISHED) {
            switch (typePostData) {
                case DetailResCenterService.ACTION_CHANGE_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_REPLY_CONVERSATION:
                    UnifyTracking.eventResolutionSendSuccess();
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_ACCEPT_ADMIN_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_ACCEPT_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_FINISH_RETUR_SOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_CANCEL_RESOLUTION:
                    view.refreshPage();
                    break;
                case DetailResCenterService.ACTION_REPORT_RESOLUTION:
                    view.refreshPage();
                    break;
                default:
                    throw new UnsupportedOperationException("unknown operation");
            }
        } else {
            String errorMessage = resultData.getString(DetailResCenterServiceConstant.EXTRA_PARAM_NETWORK_ERROR_MESSAGE);
            view.setProgressLoading(false);
            switch (resultData.getInt(DetailResCenterServiceConstant.EXTRA_PARAM_NETWORK_ERROR_TYPE)) {
                case DetailResCenterServiceConstant.STATUS_TIME_OUT :
                    view.showTimeOutMessage();
                    break;
                default:
                    view.showToastMessage(errorMessage);
                    break;
            }

            switch (typePostData) {
                case DetailResCenterService.ACTION_REPLY_CONVERSATION:
                    UnifyTracking.eventResolutionSendError();
                    break;
            }
        }
    }

    @Override
    public void refreshPage(Context context, ActivityParamenterPassData activityParamenterPassData) {
        view.setProgressLoading(true);
        view.setAttachmentArea(false);
        view.setReplyAreaEmpty();
        requestResCenterDetail(context, activityParamenterPassData);
    }

    @Override
    public void saveState(Bundle state, ActivityParamenterPassData dataPass, DetailResCenterData data) {
        state.putParcelable(CURRENT_DATA_PASS, dataPass);
        state.putParcelable(CURRENT_DATA, data);
    }

    @Override
    public void restoreState(Bundle savedState) {
        DetailResCenterData data = savedState.getParcelable(CURRENT_DATA);
        view.loadDetailResCenterData(data);
    }

    @Override
    public void onDestroyView() {
        deleteAttachment();
        retrofitInteractor.unsubscribe();
    }

    private void deleteAttachment() {
        LocalCacheManager.ImageAttachment.Builder(view.getResolutionID()).clearAll();
    }

    @Override
    public void actionInputAddress(Context context, String addressID) {
        retrofitInteractor.postAddress(context,
                NetworkParam.getParamInputAddress(addressID, view.getResolutionID()),
                new RetrofitInteractor.OnPostAddressListener() {
                    @Override
                    public void onStart() {
                        view.setProgressLoading(true);
                    }

                    @Override
                    public void onSuccess() {
                        view.refreshPage();
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setProgressLoading(false);
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                        view.setProgressLoading(false);
                    }

                    @Override
                    public void onError(String message) {
                        view.setProgressLoading(false);
                        view.showToastMessage(message);
                    }
                });
    }

    @Override
    public void actionInputAddressMigrateVersion(Context context, String addressID) {
        retrofitInteractor.postAddress(context,
                NetworkParam.getParamInputAddressMigrateVersion(addressID, view.getResolutionID()),
                new RetrofitInteractor.OnPostAddressListener() {
                    @Override
                    public void onStart() {
                        view.setProgressLoading(true);
                    }

                    @Override
                    public void onSuccess() {
                        view.refreshPage();
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setProgressLoading(false);
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                        view.setProgressLoading(false);
                    }

                    @Override
                    public void onError(String message) {
                        view.setProgressLoading(false);
                        view.showToastMessage(message);
                    }
                });
    }

    @Override
    public void actionInputAddressAcceptAdminSolution(Context context, String addressID) {
        retrofitInteractor.postAddress(context,
                NetworkParam.getParamInputAddressAcceptAdminSolution(addressID, view.getResolutionID()),
                new RetrofitInteractor.OnPostAddressListener() {
                    @Override
                    public void onStart() {
                        view.setProgressLoading(true);
                    }

                    @Override
                    public void onSuccess() {
                        view.refreshPage();
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setProgressLoading(false);
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                        view.setProgressLoading(false);
                    }

                    @Override
                    public void onError(String message) {
                        view.setProgressLoading(false);
                        view.showToastMessage(message);
                    }
                });
    }

    @Override
    public void actionEditAddress(Context context, String addressID, String ahrefEditAddressURL) {
        retrofitInteractor.editAddress(context,
                NetworkParam.getParamInputEditAddress(addressID, ahrefEditAddressURL, view.getResolutionID()),
                new RetrofitInteractor.OnPostAddressListener() {
                    @Override
                    public void onStart() {
                        view.setProgressLoading(true);
                    }

                    @Override
                    public void onSuccess() {
                        view.refreshPage();
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener listener) {
                        view.setProgressLoading(false);
                        view.showTimeOutMessage();
                    }

                    @Override
                    public void onFailAuth() {
                        view.setProgressLoading(false);
                    }

                    @Override
                    public void onError(String message) {
                        view.setProgressLoading(false);
                        view.showToastMessage(message);
                    }
                });
    }

    @Override
    public void actionImagePicker() {
        uploadImageDialog.openImagePicker();
    }

    @Override
    public void actionCamera() {
        uploadImageDialog.openCamera();
    }
}
