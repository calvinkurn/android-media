package com.tokopedia.seller.product.etalase.view.fragment;

import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseItemViewModel;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;

import java.util.ArrayList;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class EtalaseDynamicPickerGeneralFragment extends EtalaseDynamicPickerFragment {

    public static EtalaseDynamicPickerGeneralFragment createInstance(long etalaseId) {
        EtalaseDynamicPickerGeneralFragment fragment = new EtalaseDynamicPickerGeneralFragment();
        Bundle args = new Bundle();
        args.putLong(SELECTED_ETALASE_ID, etalaseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ArrayList<MyEtalaseItemViewModel> populateEtalaseViewModels() {
        ArrayList<MyEtalaseItemViewModel> myEtalaseItemViewModels = new ArrayList<>();
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_SOLD_PRODUK, getString(R.string.product_manage_filter_sold)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_FREE_RETURNS, getString(R.string.product_manage_filter_free_returns)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_PREORDER, getString(R.string.product_manage_filter_preorder)));
        myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_ALL_SHOWCASE, getString(R.string.product_manage_filter_all_showcase)));
        return myEtalaseItemViewModels;
    }
}
