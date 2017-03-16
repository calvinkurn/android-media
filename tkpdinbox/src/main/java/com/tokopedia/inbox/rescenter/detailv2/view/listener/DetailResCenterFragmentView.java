package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import com.tokopedia.inbox.rescenter.detail.dialog.ConfirmationDialog;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;

/**
 * Created by hangnadi on 3/8/17.
 */

public interface DetailResCenterFragmentView {
    String getResolutionID();

    void setResolutionID(String resolutionID);

    DetailViewModel getViewData();

    void setViewData(DetailViewModel model);

    void setOnInitResCenterDetailComplete();

    void showLoading(boolean isShow);

    boolean isSeller();

    void showConfirmationDialog(String messageDialog, ConfirmationDialog.Listener listener);

    void setOnActionAcceptProductClick();

    void setOnActionAcceptSolutionClick();

    void setOnActionEditSolutionClick();

    void setOnActionMoreProductClick();

    void setOnActionDiscussClick();

    void setOnActionMoreHistoryClick();

    void setOnActionTrackAwbClick();

    void setOnActionAwbHistoryClick();

    void setOnActionAddressHistoryClick();

    void setOnActionEditAddressClick();

    void setOnActionProductClick(String productID);

    void setOnActionPeopleDetailClick(String buyerID);

    void setOnActionShopDetailClick(String shopID);

    void setOnActionInvoiceClick(String invoice, String url);

    void setOnActionHelpClick();

    void setOnActionAppealClick();
}
