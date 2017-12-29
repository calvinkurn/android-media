package com.tokopedia.tokocash.qrpayment.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.tokopedia.tokocash.R;

/**
 * Created by nabillasabbaha on 12/29/17.
 */

public class CustomScannerTokoCashActivity extends BaseScannerQRActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getInflateViewId() {
        return R.layout.layout_scanner_qr;
    }

    @Override
    protected int getIdDecoratedBarcodeView() {
        return R.id.zxing_barcode_scanner;
    }

    @Override
    protected int getIdswitchTorch() {
        return R.id.switch_flashlight;
    }

    @Override
    protected int getIdScannerLaser() {
        return R.id.scanner_laser;
    }

    @Override
    protected int getColorUpScannerLaser() {
        return R.drawable.digital_gradient_green_up;
    }

    @Override
    protected int getColorDownScannerLaser() {
        return R.drawable.digital_gradient_green_down;
    }

    @Override
    protected void initView() {
        toolbar.setTitle("Scan QR Code");
    }

    @Override
    protected void findResult(BarcodeResult barcodeResult) {
        if (!barcodeResult.getText().equals("http://www.tokopedia.com")) {
            Toast.makeText(getApplicationContext(), "Wrong QR Code", Toast.LENGTH_LONG).show();
        } else {
            decoratedBarcodeView.setStatusText(barcodeResult.getText());
            startActivity(new Intent(getApplicationContext(), SuccessPaymentQRActivity.class));
            finish();
        }
    }
}
