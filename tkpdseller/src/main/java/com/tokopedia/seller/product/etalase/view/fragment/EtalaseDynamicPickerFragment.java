package com.tokopedia.seller.product.etalase.view.fragment;

import android.os.Bundle;

import com.tokopedia.product.manage.item.etalase.view.model.MyEtalaseItemViewModel;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/9/17.
 */

public class EtalaseDynamicPickerFragment extends EtalasePickerFragment {

    public static final String ADDITIONAL_OPTION = "additional_optional";

    private ArrayList<MyEtalaseItemViewModel> myEtalaseItemViewModels;

    public static EtalaseDynamicPickerFragment createInstance(long etalaseId, ArrayList<MyEtalaseItemViewModel> myEtalaseItemViewModelList) {
        EtalaseDynamicPickerFragment fragment = new EtalaseDynamicPickerFragment();
        Bundle args = new Bundle();
        args.putLong(SELECTED_ETALASE_ID, etalaseId);
        args.putParcelableArrayList(ADDITIONAL_OPTION, myEtalaseItemViewModelList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        myEtalaseItemViewModels = getArguments().getParcelableArrayList(ADDITIONAL_OPTION);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void renderEtalaseList(MyEtalaseViewModel etalases) {
        if(adapter.getPage() == 1){
            List<MyEtalaseItemViewModel> myEtalaseItemViewModelList = new ArrayList<>();
            if(myEtalaseItemViewModels != null) {
                myEtalaseItemViewModelList.addAll(myEtalaseItemViewModels);
            }
            myEtalaseItemViewModelList.addAll(etalases.getEtalaseList());
            etalases.setEtalaseList(myEtalaseItemViewModelList);
        }
        super.renderEtalaseList(etalases);
    }
}
