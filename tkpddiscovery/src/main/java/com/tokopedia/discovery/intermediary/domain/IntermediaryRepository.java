package com.tokopedia.discovery.intermediary.domain;

import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

import rx.Observable;

/**
 * Created by alifa on 3/24/17.
 */

public interface IntermediaryRepository {

    Observable<IntermediaryCategoryDomainModel> getCategoryIntermediary(String categoryId);
}
