package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import com.tokopedia.inbox.rescenter.detail.dialog.ConfirmationDialog;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingDialogViewModel;

/**
 * Created by hangnadi on 3/8/17.
 */

public interface DetailResCenterFragmentView {
    String getResolutionID();

    int getResolutionStatus();

    void setResolutionID(String resolutionID);

    DetailViewModel getViewData();

    void setViewData(DetailViewModel model);

    void showLoadingDialog(boolean show);

    void showTimeOutMessage();

    void showSnackBar(String messageError);

    void showLoading(boolean isShow);

    boolean isSeller();

    void doOnInitFailed();

    void showConfirmationDialog(String messageDialog, ConfirmationDialog.Listener listener);

    void setOnActionCancelResolutionClick();

    void setOnActionFinishResolutionClick();

    void setOnActionAcceptSolutionClick();

    void setOnActionRecomplaintClick();

    void setOnActionInputAddressClick();

    void setOnActionAcceptAdminSolutionClick();

    void setOnActionEditSolutionClick(boolean isFromButton);

    void setOnActionMoreProductClick();

    void setOnActionDiscussClick();

    void setOnActionMoreHistoryClick();

    void setOnActionTrackAwbClick(String shipmentID, String shipmentRef);

    void setOnActionAwbHistoryClick();

    void setOnActionAddressHistoryClick();

    void setOnActionEditAddressClick();

    void setOnFreeReturnClicked();

    void setOnActionProductClick(String productID, String productName);

    void setOnActionPeopleDetailClick(String buyerID);

    void setOnActionShopDetailClick(String shopID);

    void setOnActionInvoiceClick(String invoice, String url);

    void setOnActionHelpClick();

    void setOnActionHelpTimeViewClick();

    void setOnActionAppealClick();

    void setOnActionInputAwbNumberClick(boolean isFromButton);

    void setOnDiscussionButtonPosition(boolean isButtonAvailable);

    void actionReturnToList();

    void doOnInitTimeOut();

    void doOnInitSuccess();

    void doOnTrackingTimeOut();

    void doOnTrackingSuccess(TrackingDialogViewModel trackingDialogViewModel);

    void doOnTrackingError(String messageError);

    void doOnTrackingFailed();

    void doOnActionSuccess(String action);

    void doOnActionError(String messageError);

    void doOnActionError();

    void doOnActionTimeOut();

    void hideTimeTicker();

    void doImpressionTrackingButton(String buttonType);
}
