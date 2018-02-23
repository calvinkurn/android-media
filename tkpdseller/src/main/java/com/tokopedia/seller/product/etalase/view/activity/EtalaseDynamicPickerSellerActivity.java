package com.tokopedia.seller.product.etalase.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.product.etalase.view.fragment.EtalaseDynamicPickerFragment;
import com.tokopedia.seller.product.etalase.view.fragment.EtalaseDynamicPickerSellerFragment;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseItemViewModel;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;

import java.util.ArrayList;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class EtalaseDynamicPickerSellerActivity extends EtalaseDynamicPickerActivity {
    public static Intent createSellerInstance(Context context, long etalaseId) {
        if(etalaseId == Integer.MAX_VALUE){
            etalaseId = ProductManageConstant.FILTER_ALL_PRODUK;
        }

        Intent intent = new Intent(context, EtalaseDynamicPickerGeneralActivity.class);
        intent.putExtra(SELECTED_ETALASE_ID, etalaseId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        long etalaseId = getIntent().getLongExtra(SELECTED_ETALASE_ID, UNSELECTED_ETALASE_ID);
        return EtalaseDynamicPickerSellerFragment.createInstance(etalaseId);
    }
}
