package com.tokopedia.seller.product.edit.view.mapper;

import com.tokopedia.seller.product.edit.domain.model.MyEtalaseDomainModel;
import com.tokopedia.seller.product.edit.domain.model.MyEtalaseItemDomainModel;
import com.tokopedia.seller.product.edit.view.model.etalase.MyEtalaseItemViewModel;
import com.tokopedia.seller.product.edit.view.model.etalase.MyEtalaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseDomainToView {
    public static MyEtalaseViewModel map(MyEtalaseDomainModel etalases) {
        MyEtalaseViewModel viewModel = new MyEtalaseViewModel();
        viewModel.setHasNextPage(etalases.isHasNext());
        viewModel.setEtalaseList(mapList(etalases.getEtalaseItems()));
        return viewModel;
    }

    public static List<MyEtalaseItemViewModel> mapList(List<MyEtalaseItemDomainModel> etalases) {
        List<MyEtalaseItemViewModel> viewModels = new ArrayList<>();
        for (MyEtalaseItemDomainModel domainModel : etalases){
            MyEtalaseItemViewModel viewModel = new MyEtalaseItemViewModel();
            viewModel.setEtalaseId(domainModel.getEtalaseId());
            viewModel.setEtalaseName(domainModel.getEtalaseName());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
