package com.tokopedia.seller.shop.setting.view.mapper;

import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class RecommendationDistrictViewMapper {
    public static List<RecommendationDistrictViewModel> map(List<RecomendationDistrictDomainModel> domainModels) {
        List<RecommendationDistrictViewModel> viewModelList = new ArrayList<>();
        for(RecomendationDistrictDomainModel domainModel : domainModels){
            RecommendationDistrictViewModel viewModel = mapViewModel(domainModel);
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }

    private static RecommendationDistrictViewModel mapViewModel(RecomendationDistrictDomainModel domainModel) {
        RecommendationDistrictViewModel viewModel = new RecommendationDistrictViewModel();
        viewModel.setDistrictId(domainModel.getDistrictId());
        viewModel.setDistrictString(domainModel.getDistrictString());
        return viewModel;
    }
}
