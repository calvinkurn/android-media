package com.tokopedia.transaction.purchase.detail.activity;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.utils.TransactionTrackingUtil;
import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;


/**
 * Created by nabillasabbaha on 12/14/17.
 */

public class CustomScannerBarcodeActivity extends CaptureActivity {

    TransactionTrackingUtil transactionTrackingUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getApplicationContext() instanceof ITransactionOrderDetailRouter) {
            transactionTrackingUtil = new TransactionTrackingUtil((ITransactionOrderDetailRouter)getApplicationContext());
            transactionTrackingUtil.sendTrackerImpressionAWB();
        }
    }

    @Override
    public void onBackPressed() {
        if(transactionTrackingUtil != null) {
            transactionTrackingUtil.sendTrackerOnBackScanAWB();
        }
        super.onBackPressed();
    }

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.layout_scanner_barcode);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
