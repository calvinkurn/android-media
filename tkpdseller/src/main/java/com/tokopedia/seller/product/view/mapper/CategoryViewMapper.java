package com.tokopedia.seller.product.view.mapper;

import com.tokopedia.seller.product.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryViewMapper {
    public static List<CategoryViewModel> map(List<CategoryDomainModel> domainModels) {
        List<CategoryViewModel> viewModels = new ArrayList<>();
        for (CategoryDomainModel domainModel : domainModels){
            CategoryViewModel viewModel = new CategoryViewModel();
            viewModel.setName(domainModel.getName());
            viewModel.setId(domainModel.getId());
            viewModel.setHasChild(domainModel.isHasChild());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
