package com.tokopedia.discovery.intermediary.data.mapper;

import android.preference.PreferenceActivity;

import com.tokopedia.core.network.entity.categoriesHades.CategoryHadesModel;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

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

        return  intermediaryCategoryDomainModel;
    }

    private HeaderModel mapHeaderModel(CategoryHadesModel categoryHadesModel) {

        HeaderModel  headerModel = new HeaderModel(categoryHadesModel.getData().getName(),
                categoryHadesModel.getData().getHeaderImage());

        return headerModel;
    }

    private List<ChildCategoryModel> mapCategoryChildren(CategoryHadesModel categoryHadesModel) {

        List<ChildCategoryModel> categoryModelList = new ArrayList<>();
        for (Child child: categoryHadesModel.getData().getChild()) {
            ChildCategoryModel childCategoryModel = new ChildCategoryModel();
            childCategoryModel.setCategoryId(child.getId());
            childCategoryModel.setCategoryImageUrl(child.getThumbnailImage());
            childCategoryModel.setCategoryName(child.getName());
            categoryModelList.add(childCategoryModel);
        }
        return categoryModelList;
    }

}
