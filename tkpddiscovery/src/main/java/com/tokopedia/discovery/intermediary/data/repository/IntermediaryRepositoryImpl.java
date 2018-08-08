package com.tokopedia.discovery.intermediary.data.repository;

import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.data.source.IntermediaryDataSource;
import com.tokopedia.discovery.intermediary.domain.IntermediaryRepository;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryRepositoryImpl implements IntermediaryRepository {

    private final IntermediaryDataSource intermediaryDataSource;

    public IntermediaryRepositoryImpl(IntermediaryDataSource intermediaryDataSource) {
        this.intermediaryDataSource = intermediaryDataSource;
    }

    @Override
    public Observable<Response<CategoryHadesModel>> getCategoryHeader(String categoryId) {
        return intermediaryDataSource.getCategoryHeader(categoryId);
    }

    @Override
    public Observable<IntermediaryCategoryDomainModel> getCategoryIntermediary(String categoryId, CategoryHadesModel categoryHadesModel) {
        return intermediaryDataSource.getintermediaryCategory(categoryId,categoryHadesModel);
    }
}
