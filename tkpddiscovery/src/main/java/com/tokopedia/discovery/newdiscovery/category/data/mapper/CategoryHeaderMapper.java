package com.tokopedia.discovery.newdiscovery.category.data.mapper;

import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.core.network.entity.intermediary.Child;
import com.tokopedia.core.network.entity.intermediary.Image;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.BannerModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.HeaderModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by alifa on 10/30/17.
 */

public class CategoryHeaderMapper implements Func1<Response<CategoryHadesModel>, CategoryHeaderModel> {

    @Override
    public CategoryHeaderModel call(Response<CategoryHadesModel> categoryHadesModelResponse) {

        CategoryHeaderModel categoryHeaderModel = new CategoryHeaderModel();
        if (categoryHadesModelResponse.body().getData()!=null) {
            categoryHeaderModel.setRevamp(categoryHadesModelResponse.body().getData().getRevamp());
            categoryHeaderModel.setTemplate(categoryHadesModelResponse.body().getData().getTemplate());
            categoryHeaderModel.setRootCategoryId(String.valueOf(categoryHadesModelResponse.body().getData().getRootCategoryId()));
            categoryHeaderModel.setHeaderImage(categoryHadesModelResponse.body().getData().getHeaderImage());
            categoryHeaderModel.setHeaderImageHexColor(categoryHadesModelResponse.body().getData().getHeaderHexColor());
            categoryHeaderModel.setDepartementId(categoryHadesModelResponse.body().getData().getId());
            categoryHeaderModel.setHeaderModel(mapHeaderModel(categoryHadesModelResponse.body()));
            categoryHeaderModel.setChildCategoryModelList(mapCategoryChildren(categoryHadesModelResponse.body()));
            categoryHeaderModel.setBannerModelList(mapBanner(categoryHadesModelResponse.body()));
        }

        return categoryHeaderModel;
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
                childCategoryModel.setCategoryUrl(child.getUrl());
                categoryModelList.add(childCategoryModel);
            }
        }
        return categoryModelList;
    }

    private List<BannerModel> mapBanner(CategoryHadesModel categoryHadesModel) {

        List<BannerModel> bannerModels = new ArrayList<>();
        if (categoryHadesModel.getData().getBanner()!=null && categoryHadesModel.getData().getBanner().getImages()!=null) {
            for (Image image: categoryHadesModel.getData().getBanner().getImages()) {
                BannerModel bannerModel = new BannerModel();
                bannerModel.setUrl(image.getUrl());
                bannerModel.setImageUrl(image.getImageUrl());
                bannerModel.setPosition(image.getPosition());
                bannerModels.add(bannerModel);
            }
        }
        return  bannerModels;
    }
}
