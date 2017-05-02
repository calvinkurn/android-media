package com.tokopedia.seller.shopscore.view.mapper;

import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailItemDomainModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 2/27/17.
 */
public class ShopScoreDetailItemsViewModelMapper {
    public static List<ShopScoreDetailItemViewModel> map(ShopScoreDetailDomainModel domainModels) {
        List<ShopScoreDetailItemViewModel> viewModels = new ArrayList<>();
        for (ShopScoreDetailItemDomainModel domainModel : domainModels.getItemModels()) {
            ShopScoreDetailItemViewModel viewModel = new ShopScoreDetailItemViewModel();
            viewModel.setTitle(domainModel.getTitle());
            viewModel.setValue(domainModel.getValue());
            viewModel.setDescription(domainModel.getDescription());
            viewModel.setProgressBarColor(domainModel.getProgressBarColor());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
