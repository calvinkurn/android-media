package com.tokopedia.seller.shopscore.view.mapper;

import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 2/27/17.
 */
public class ShopScoreViewModelMapper {
    public static List<ShopScoreDetailViewModel> map(List<ShopScoreDetailDomainModel> domainModels) {
        List<ShopScoreDetailViewModel> viewModels = new ArrayList<>();
        for (ShopScoreDetailDomainModel domainModel : domainModels) {
            ShopScoreDetailViewModel viewModel = new ShopScoreDetailViewModel();
            viewModel.setTitle(domainModel.getTitle());
            viewModel.setValue(domainModel.getValue());
            viewModel.setDescription(domainModel.getDescription());
            viewModel.setProgressBarColor(domainModel.getProgressBarColor());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
