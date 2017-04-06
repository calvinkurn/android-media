package com.tokopedia.seller.product.view.mapper;

import com.tokopedia.seller.product.domain.model.MyEtalaseDomainModel;
import com.tokopedia.seller.product.view.model.etalase.MyEtalaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseDomainToView {
    public static List<MyEtalaseViewModel> map(List<MyEtalaseDomainModel> etalases) {
        List<MyEtalaseViewModel> viewModels = new ArrayList<>();
        for (MyEtalaseDomainModel domainModel : etalases){
            MyEtalaseViewModel viewModel = new MyEtalaseViewModel();
            viewModel.setEtalaseId(domainModel.getEtalaseId());
            viewModel.setEtalaseName(domainModel.getEtalaseName());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
