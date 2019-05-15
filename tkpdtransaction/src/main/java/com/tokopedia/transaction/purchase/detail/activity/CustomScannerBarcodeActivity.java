package com.tokopedia.transaction.purchase.detail.activity;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.logisticdata.data.analytics.SalesShippingAnalytics;
import com.tokopedia.logisticdata.data.analytics.listener.IBarcodeScannerReceiptShippingAnalyticListener;
import com.tokopedia.transaction.R;


/**
 * Created by nabillasabbaha on 12/14/17.
 */

public class CustomScannerBarcodeActivity extends CaptureActivity implements IBarcodeScannerReceiptShippingAnalyticListener {

    private SalesShippingAnalytics salesShippingAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        salesShippingAnalytics = new SalesShippingAnalytics();
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
