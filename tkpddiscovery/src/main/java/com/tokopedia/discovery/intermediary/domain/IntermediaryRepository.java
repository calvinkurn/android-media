package com.tokopedia.discovery.intermediary.domain;

import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by alifa on 3/24/17.
 */

public interface IntermediaryRepository {

    Observable<Response<CategoryHadesModel>> getCategoryHeader(String categoryId);

    Observable<IntermediaryCategoryDomainModel> getCategoryIntermediary(String categoryId, CategoryHadesModel categoryHeaderModel);
}
