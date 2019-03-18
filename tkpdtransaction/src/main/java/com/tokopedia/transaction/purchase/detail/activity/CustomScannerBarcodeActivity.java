package com.tokopedia.transaction.purchase.detail.activity;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.logisticanalytics.SalesShippingAnalytics;
import com.tokopedia.logisticanalytics.listener.IBarcodeScannerReceiptShippingAnalyticListener;
import com.tokopedia.transaction.R;


/**
 * Created by nabillasabbaha on 12/14/17.
 */

public class CustomScannerBarcodeActivity extends CaptureActivity implements IBarcodeScannerReceiptShippingAnalyticListener {

    private SalesShippingAnalytics salesShippingAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplicationContext() instanceof AbstractionRouter) {
            salesShippingAnalytics = new SalesShippingAnalytics();
        }
        sendAnalyticsOnImpressionBarcodeScanner();

    }

    @Override
    public void onBackPressed() {
        sendAnalyticsOnCloseBarcodeScanner();
        super.onBackPressed();
    }

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.layout_scanner_barcode);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }

    @Override
    public void sendAnalyticsOnImpressionBarcodeScanner() {
        salesShippingAnalytics.eventViewShippingSalesShippingImpressionScanAwbPage();
    }

    @Override
    public void sendAnalyticsOnCloseBarcodeScanner() {
        salesShippingAnalytics.eventClickShippingSalesShippingClickExitScanAwb();
    }
}
