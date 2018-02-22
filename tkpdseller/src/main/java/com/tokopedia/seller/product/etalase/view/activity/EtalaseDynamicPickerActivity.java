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

    public static Intent createInstance(Context context, String shopId, long etalaseId) {
        ArrayList<MyEtalaseItemViewModel> myEtalaseItemViewModels = new ArrayList<>();
        // if it's not her/his shop
        if(!SessionHandler.getShopID(context).equals(shopId)){
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_SOLD_PRODUK, context.getString(R.string.product_manage_filter_sold)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_FREE_RETURNS, context.getString(R.string.product_manage_filter_free_returns)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_PREORDER, context.getString(R.string.product_manage_filter_preorder)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_ALL_SHOWCASE, context.getString(R.string.product_manage_filter_all_showcase)));
            if(etalaseId == Integer.MAX_VALUE){
                etalaseId = ProductManageConstant.FILTER_ALL_SHOWCASE;
            }
        }else {
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_ALL_PRODUK, context.getString(R.string.product_manage_filter_menu_etalase_all)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_SOLD_PRODUK, context.getString(R.string.product_manage_filter_sold)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_EMPTY_STOK, context.getString(R.string.product_manage_filter_empty_stok)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_PENDING, context.getString(R.string.product_manage_filter_pending)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_FREE_RETURNS, context.getString(R.string.product_manage_filter_free_returns)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_PREORDER, context.getString(R.string.product_manage_filter_preorder)));
            myEtalaseItemViewModels.add(new MyEtalaseItemViewModel(ProductManageConstant.FILTER_ALL_SHOWCASE, context.getString(R.string.product_manage_filter_all_showcase)));
            if(etalaseId == Integer.MAX_VALUE){
                etalaseId = ProductManageConstant.FILTER_ALL_PRODUK;
            }
        }

        Intent intent = new Intent(context, EtalaseDynamicPickerActivity.class);
        intent.putExtra(SELECTED_ETALASE_ID, etalaseId);
        intent.putParcelableArrayListExtra(ADDITIONAL_OPTION, myEtalaseItemViewModels);
        return intent;
    }

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
