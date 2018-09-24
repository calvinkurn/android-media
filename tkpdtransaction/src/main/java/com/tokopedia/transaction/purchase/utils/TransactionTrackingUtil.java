package com.tokopedia.transaction.purchase.utils;

import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;

public class TransactionTrackingUtil {
    private ITransactionOrderDetailRouter router;

    public TransactionTrackingUtil(ITransactionOrderDetailRouter router) {
        this.router = router;
    }


    public void sendTrackerOnScanBarcodeClick() {
        router.sendEventTracking(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_CLICK_TOMBOL_SCAN_AWB,
                "");
    }

    public void sendTrackerOnResultScanBarcode() {
        router.sendEventTracking(TransactionTrackingConstant.EVENT_VIEW_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_VIEW_SCAN_AWB,
                TransactionTrackingConstant.EVENT_LABEL_SUCCESS);
    }

    public void sendTrackerOnSuccessConfirmShipping() {
        router.sendEventTracking(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_CLICK_TOMBOL_SELESAI,
                TransactionTrackingConstant.EVENT_LABEL_SUCCESS);
    }

    public void sendTrackerOnFailedConfirmShipping() {
        router.sendEventTracking(TransactionTrackingConstant.EVENT_CLICK_SHIPPING,
                TransactionTrackingConstant.EVENT_CATEGORY_SALES_SHIPPING,
                TransactionTrackingConstant.EVENT_ACTION_CLICK_TOMBOL_SELESAI,
                TransactionTrackingConstant.EVENT_LABEL_FAILED);

    }
}
