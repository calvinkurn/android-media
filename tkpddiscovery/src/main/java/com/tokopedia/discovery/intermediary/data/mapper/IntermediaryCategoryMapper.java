package com.tokopedia.discovery.intermediary.data.mapper;

import android.preference.PreferenceActivity;

import com.tokopedia.core.network.entity.categoriesHades.Badge;
import com.tokopedia.core.network.entity.categoriesHades.CategoryHadesModel;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.core.network.entity.categoriesHades.Label;
import com.tokopedia.core.network.entity.categoriesHades.Product;
import com.tokopedia.core.network.entity.categoriesHades.Section;
import com.tokopedia.discovery.intermediary.domain.model.BadgeModel;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;
import com.tokopedia.discovery.intermediary.domain.model.LabelModel;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryCategoryMapper implements Func1<Response<CategoryHadesModel>,IntermediaryCategoryDomainModel> {

    @Override
    public IntermediaryCategoryDomainModel call(Response<CategoryHadesModel> categoryHadesModelResponse) {
        IntermediaryCategoryDomainModel intermediaryCategoryDomainModel = new IntermediaryCategoryDomainModel();

        intermediaryCategoryDomainModel.setHeaderModel(mapHeaderModel(categoryHadesModelResponse.body()));
        intermediaryCategoryDomainModel.setChildCategoryModelList(mapCategoryChildren(categoryHadesModelResponse.body()));
        intermediaryCategoryDomainModel.setCuratedSectionModelList(mapCuration(categoryHadesModelResponse.body()));

        return  intermediaryCategoryDomainModel;
    }

    private HeaderModel mapHeaderModel(CategoryHadesModel categoryHadesModel) {

        HeaderModel headerModel = new HeaderModel();
        if (categoryHadesModel.getData()!=null) {
            headerModel = new HeaderModel(categoryHadesModel.getData().getName(),
                    categoryHadesModel.getData().getHeaderImage());
        }
        return headerModel;
    }

    private List<ChildCategoryModel> mapCategoryChildren(CategoryHadesModel categoryHadesModel) {

        List<ChildCategoryModel> categoryModelList = new ArrayList<>();
        if (categoryHadesModel.getData()!=null && categoryHadesModel.getData().getChild()!=null) {
            for (Child child: categoryHadesModel.getData().getChild()) {
                ChildCategoryModel childCategoryModel = new ChildCategoryModel();
                childCategoryModel.setCategoryId(child.getId());
                childCategoryModel.setCategoryImageUrl(child.getThumbnailImage());
                childCategoryModel.setCategoryName(child.getName());
                categoryModelList.add(childCategoryModel);
            }
        }
        return categoryModelList;
    }

    private List<CuratedSectionModel> mapCuration(CategoryHadesModel categoryHadesModel) {

        List<CuratedSectionModel> curatedSectionModels = new ArrayList<>();
        if (categoryHadesModel.getData() !=null && categoryHadesModel.getData().getCuratedProduct()
                !=null && categoryHadesModel.getData().getCuratedProduct().getSections() !=null) {
            for (Section section: categoryHadesModel.getData().getCuratedProduct().getSections()) {
                CuratedSectionModel curatedSectionModel = new CuratedSectionModel();
                curatedSectionModel.setTitle(section.getTitle());


                List<ProductModel> productModels = new ArrayList<>();

                if (section.getProducts()!=null) {
                    for (Product product: section.getProducts()) {
                        ProductModel productModel = new ProductModel();
                        productModel.setName(product.getName());
                        productModel.setId(product.getId());
                        productModel.setImageUrl(product.getImageUrl());
                        productModel.setPrice(product.getPrice());
                        productModel.setShopName(product.getShop().getName());
                        productModel.setShopLocation(product.getShop().getLocation());
                        List<BadgeModel> badgeModels = new ArrayList<>();
                        for (Badge badge: product.getBadges()) {
                            badgeModels.add(new BadgeModel(badge.getImageUrl(),badge.getTitle()));
                        }
                        productModel.setBadges(badgeModels);
                        List<LabelModel> labelModels = new ArrayList<>();
                        for (Label label: product.getLabels()) {
                            labelModels.add(new LabelModel(label.getColor(),label.getTitle()));
                        }
                        productModel.setLabels(labelModels);
                        productModels.add(productModel);

                        if (productModels.size()==4)
                            break;
                    }
                }
                curatedSectionModel.setProducts(productModels);

                curatedSectionModels.add(curatedSectionModel);
            }
        }
        return curatedSectionModels;
    }

}
