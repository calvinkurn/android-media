package com.tokopedia.seller.product.edit.view.mapper;

import com.tokopedia.seller.product.edit.domain.model.CategoryRecommDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductCategoryIdDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductCategoryPredictionDomainModel;
import com.tokopedia.seller.product.edit.view.model.categoryrecomm.CategoryRecommViewModel;
import com.tokopedia.seller.product.edit.view.model.categoryrecomm.ProductCategoryIdViewModel;
import com.tokopedia.seller.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryRecommDomainToViewMapper implements Func1<CategoryRecommDomainModel, CategoryRecommViewModel> {

    @Inject
    public CategoryRecommDomainToViewMapper(){

    }

    @Override
    public CategoryRecommViewModel call(CategoryRecommDomainModel domainModel) {
        return mapDomainView(domainModel);
    }

    public static CategoryRecommViewModel mapDomainView(CategoryRecommDomainModel domainModel) {
        CategoryRecommViewModel viewModel = new CategoryRecommViewModel();

        List<ProductCategoryPredictionViewModel> productCategoryPredictionViewModels = new ArrayList<>();

        List<ProductCategoryPredictionDomainModel> productCategoryPredictions = domainModel.getProductCategoryPrediction();

        for (int i=0, sizei = productCategoryPredictions.size(); i<sizei; i++) {
            ProductCategoryPredictionDomainModel p = productCategoryPredictions.get(i);

            ProductCategoryPredictionViewModel predictionViewModel = new ProductCategoryPredictionViewModel();
            predictionViewModel.setConfidenceScore(p.getConfidenceScore());

            List<ProductCategoryIdDomainModel> productCategoryIds = p.getProductCategoryId();
            List<ProductCategoryIdViewModel> productCategoryIdViewModelList = new ArrayList<>();
            for (int j=0, sizej=productCategoryIds.size(); j<sizej; j++) {
                ProductCategoryIdDomainModel pId = productCategoryIds.get(j);
                productCategoryIdViewModelList.add(new ProductCategoryIdViewModel(pId.getId(), pId.getName()));
            }
            predictionViewModel.setProductCategoryId(productCategoryIdViewModelList);
            productCategoryPredictionViewModels.add(predictionViewModel);
        }
        viewModel.setProductCategoryPrediction(productCategoryPredictionViewModels);
        return viewModel;
    }

}
