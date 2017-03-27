package com.tokopedia.seller.shop.setting.view.mapper;

import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;
import com.tokopedia.seller.shop.setting.domain.model.RecommendationDistrictItemDomainModel;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictItemViewModel;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class RecommendationDistrictViewMapper {
    public static RecommendationDistrictViewModel map(RecomendationDistrictDomainModel domainModel) {
        RecommendationDistrictViewModel viewModel = new RecommendationDistrictViewModel();

        viewModel.setItems(mapItems(domainModel.getItems()));
        viewModel.setStringTyped(domainModel.getStringTyped());

        return viewModel;
    }

    private static List<RecommendationDistrictItemViewModel>mapItems(List<RecommendationDistrictItemDomainModel> items) {
        List<RecommendationDistrictItemViewModel> viewModelList = new ArrayList<>();

        for(RecommendationDistrictItemDomainModel itemDomainModel : items){
            RecommendationDistrictItemViewModel viewModel = mapViewModel(itemDomainModel);
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }

    private static RecommendationDistrictItemViewModel mapViewModel(RecommendationDistrictItemDomainModel domainModel) {
        RecommendationDistrictItemViewModel viewModel = new RecommendationDistrictItemViewModel();
        viewModel.setDistrictId(domainModel.getDistrictId());
        viewModel.setDistrictString(domainModel.getDistrictString());
        return viewModel;
    }
}
