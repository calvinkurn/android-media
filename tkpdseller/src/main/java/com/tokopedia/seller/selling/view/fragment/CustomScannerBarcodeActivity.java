package com.tokopedia.seller.selling.view.fragment;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.tokopedia.seller.R;

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
