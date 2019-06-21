package com.tokopedia.transaction.purchase.utils;

import com.tokopedia.track.TrackApp;
import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;


@Deprecated
public class TransactionTrackingUtil {
    private ITransactionOrderDetailRouter router;

    public TransactionTrackingUtil(ITransactionOrderDetailRouter router) {
        this.router = router;
    }


    public void sendTrackerOnScanBarcodeClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_CLICK_TOMBOL_SCAN_AWB,
                "");
    }

    public void sendTrackerOnResultScanBarcode() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TransactionTrackingConstant.EVENT_VIEW_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_VIEW_SCAN_AWB,
                TransactionTrackingConstant.EVENT_LABEL_SUCCESS);
    }

    public void sendTrackerOnSuccessConfirmShipping() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_CLICK_TOMBOL_SELESAI,
                TransactionTrackingConstant.EVENT_LABEL_SUCCESS);
    }

    public void sendTrackerOnFailedConfirmShipping() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_CLICK_TOMBOL_SELESAI,
                TransactionTrackingConstant.EVENT_LABEL_FAILED);

    }

    public void sendTrackerImpressionAWB() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_IMPRESSION_SCAN_AWB_PAGE,
                "");
    }

    public void sendTrackerOnBackScanAWB() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_EXIT_SCAN_AWB,
                "");
    }
}
