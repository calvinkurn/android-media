package com.tokopedia.seller.product.edit.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddWholesaleFragment;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;

import java.util.ArrayList;

/**
 * Created by yoshua on 02/05/18.
 */

public class ProductAddWholesaleActivity extends BaseSimpleActivity {

    public static final String EXTRA_PRODUCT_WHOLESALE_LIST = "EXTRA_PRODUCT_WHOLESALE_LIST";

    @Override
    protected Fragment getNewFragment() {
        return ProductAddWholesaleFragment.newInstance();
    }

    public static Intent getIntent(Context context, ArrayList<ProductWholesaleViewModel> productWholesaleViewModelList) {
        Intent intent = new Intent(context, ProductAddWholesaleActivity.class);
        intent.putExtra(EXTRA_PRODUCT_WHOLESALE_LIST, productWholesaleViewModelList);
        return intent;
    }

    @Override
    public void onBackPressed() {
//        if (hasVariantChangedFromResult) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.product_dialog_cancel_title))
                    .setMessage(getString(R.string.product_dialog_cancel_message))
                    .setPositiveButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ProductAddWholesaleActivity.super.onBackPressed();
                        }
                    }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        }
//        else {
//            super.onBackPressed();
//        }

}
