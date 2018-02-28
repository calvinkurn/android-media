package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.view.View;

import com.tokopedia.abstraction.base.view.dialog.BaseValidationDialog;

/**
 * Created by kris on 1/26/18. Tokopedia
 */

public class CancelShipmentDialogFragment extends BaseValidationDialog {


    @Override
    protected String setTitle() {
        return "Buang Perubahan?";
    }

    @Override
    protected String setContent() {
        return "Jumlah barang yang sudah dikurangi akan digabungkan jadi satu invoice kembali";
    }

    @Override
    protected String setNegativeButtonText() {
        return "Batal";
    }

    @Override
    protected String setPositiveButtonText() {
        return "Buang";
    }

    @Override
    protected View.OnClickListener setNegativeButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    @Override
    protected View.OnClickListener setPositiveButtonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        };
    }
}
