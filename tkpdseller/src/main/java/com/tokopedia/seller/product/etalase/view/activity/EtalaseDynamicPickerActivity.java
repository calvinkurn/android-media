package com.tokopedia.seller.product.etalase.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.etalase.view.fragment.EtalaseDynamicPickerFragment;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseItemViewModel;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 10/9/17.
 */

public class EtalaseDynamicPickerActivity extends EtalasePickerActivity {

    public static final String ADDITIONAL_OPTION = "additional_option";

    public static Intent createInstance(Context context, long etalaseId, ArrayList<MyEtalaseItemViewModel> myEtalaseItemViewModelList) {
        Intent intent = new Intent(context, EtalaseDynamicPickerActivity.class);
        intent.putExtra(SELECTED_ETALASE_ID, etalaseId);
        intent.putParcelableArrayListExtra(ADDITIONAL_OPTION, myEtalaseItemViewModelList);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        long etalaseId = getIntent().getLongExtra(SELECTED_ETALASE_ID, UNSELECTED_ETALASE_ID);
        ArrayList<MyEtalaseItemViewModel> myEtalaseItemViewModels = getIntent().getParcelableArrayListExtra(ADDITIONAL_OPTION);
        return EtalaseDynamicPickerFragment.createInstance(etalaseId, myEtalaseItemViewModels);
    }
}
