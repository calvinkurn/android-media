package com.tokopedia.discovery.intermediary.data.mapper;

import android.preference.PreferenceActivity;

import com.tokopedia.core.network.entity.categoriesHades.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

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

        return  intermediaryCategoryDomainModel;
    }

    private HeaderModel mapHeaderModel(CategoryHadesModel categoryHadesModel) {

        HeaderModel  headerModel = new HeaderModel(categoryHadesModel.getData().getName(),
                categoryHadesModel.getData().getHeaderImage());

        return headerModel;
    }

}
