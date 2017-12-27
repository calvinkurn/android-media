package com.tokopedia.seller.product.edit.data.mapper;

import com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata.ProductCategoryId;
import com.tokopedia.seller.product.edit.data.source.cloud.model.categoryrecommdata.ProductCategoryPrediction;
import com.tokopedia.seller.product.edit.domain.model.CategoryRecommDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductCategoryIdDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductCategoryPredictionDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryRecommDataToDomainMapper implements Func1<CategoryRecommDataModel, CategoryRecommDomainModel> {

    @Inject
    public CategoryRecommDataToDomainMapper(){

    }

    @Override
    public CategoryRecommDomainModel call(CategoryRecommDataModel dataModel) {
        return mapDomainModel(dataModel);
    }

    public static CategoryRecommDomainModel mapDomainModel(CategoryRecommDataModel dataModel) {
        CategoryRecommDomainModel domainModel = new CategoryRecommDomainModel();

        List<ProductCategoryPredictionDomainModel> productCategoryPredictionDomainModels = new ArrayList<>();

        List<ProductCategoryPrediction> productCategoryPredictions = dataModel.getData().get(0).getProductCategoryPrediction();

        for (int i=0, sizei = productCategoryPredictions.size(); i<sizei; i++) {
            ProductCategoryPrediction p = productCategoryPredictions.get(i);

            ProductCategoryPredictionDomainModel predictionDomainModel = new ProductCategoryPredictionDomainModel();
            predictionDomainModel.setConfidenceScore(p.getConfidenceScore());

            List<ProductCategoryId> productCategoryIds = p.getProductCategoryId();
            List<ProductCategoryIdDomainModel> productCategoryIdDomainModelList = new ArrayList<>();
            for (int j=0, sizej=productCategoryIds.size(); j<sizej; j++) {
                ProductCategoryId pId = productCategoryIds.get(j);
                productCategoryIdDomainModelList.add(new ProductCategoryIdDomainModel(pId.getId(), pId.getName()));
            }
            predictionDomainModel.setProductCategoryId(productCategoryIdDomainModelList);
            productCategoryPredictionDomainModels.add(predictionDomainModel);
        }
        domainModel.setProductCategoryPrediction(productCategoryPredictionDomainModels);
        return domainModel;
    }

}
