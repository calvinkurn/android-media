package com.tokopedia.transaction.purchase.detail.activity;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.transaction.R;


/**
 * Created by nabillasabbaha on 12/14/17.
 */

public class CustomScannerBarcodeActivity extends CaptureActivity {

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.layout_scanner_barcode);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
