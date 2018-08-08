package com.tokopedia.inbox.rescenter.detail.listener;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.detail.dialog.ConfirmationDialog;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;

import java.util.List;

/**
 * Created on 2/9/16.
 */
public interface DetailResCenterView {

    void onReceiveServiceResult(int resultCode, Bundle resultData);

    void showConfirmationDialog(int messageDialog, ConfirmationDialog.Listener listener);

    void setProgressLoading(boolean param);

    void showToastMessage(String message);

    void loadDetailResCenterData(DetailResCenterData data);

    void setErrorComment(String param);

    void setAttachmentArea(boolean param);

    void showLoadingDialog(boolean param);

    void showTrackingDialog(ResCenterTrackShipping.TrackShipping trackShipping);

    void showInvalidTrackingDialog();

    void finishActivity();

    void refreshPage();

    void showAttachment(List<AttachmentResCenterVersion2DB> data);

    void setFailSaveRespond();

    void showTimeOutMessage();

    void showLoading(boolean isVisible);

    void showMainView(boolean isVisible);

    void showTimeOutView(NetworkErrorHelper.RetryClickedListener clickedListener);

    void showErrorView(String message);

    void setOnSendClickListener(String text);

    void setOnAttachmentClickListener();

    void actionAcceptAdmin(String paramID);

    void actionFinishRetur(String paramID);

    void actionAcceptResolution(String paramID);

    void actionReportResolution(String paramID);

    void actionCancelResolution(String paramID);

    void openInputShippingRef();

    void openTrackShippingRef(String url);

    void openAppealSolution(String paramID);

    void openEditSolution(String paramID);

    void openInputAddress();

    void openInputAddressForAcceptAdmin();

    void openInputAddressMigrateVersion();

    void openEditAddress(String url);

    void setReplyAreaEmpty();

    void openEditShippingRef(String url);

    void openAttachment(String url);

    void openShop();

    void openInvoice();

    void openPeople(String url);

    void setErrorWvLogin();

    String getResolutionID();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    void openVideoPlayer(String url);
}
