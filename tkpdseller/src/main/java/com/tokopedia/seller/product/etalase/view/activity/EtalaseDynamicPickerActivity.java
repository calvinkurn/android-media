package com.tokopedia.seller.product.etalase.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.seller.product.etalase.view.fragment.EtalaseDynamicPickerFragment;
import com.tokopedia.product.manage.item.etalase.view.model.MyEtalaseItemViewModel;

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
