package com.tokopedia.seller.shop.setting.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.setting.domain.DistrictDataRepository;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class GetRecomendationLocationDistrictUseCase extends UseCase<List<RecomendationDistrictDomainModel>>{
    public static final String STRING_TYPED = "STRING_TYPED";
    private final DistrictDataRepository districtDataRepository;

    public GetRecomendationLocationDistrictUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, DistrictDataRepository districtDataRepository) {
        super(threadExecutor, postExecutionThread);
        this.districtDataRepository = districtDataRepository;
    }

    @Override
    public Observable<List<RecomendationDistrictDomainModel>> createObservable(RequestParams requestParams) {
        String stringTyped = requestParams.getString(STRING_TYPED, "");
        if (stringTyped.isEmpty()){
            throw new RuntimeException("String typed cannot be empty");
        }
        return districtDataRepository.getRecommendationLocationDistrict(stringTyped);
    }

    public static RequestParams generateParams(String stringTyped) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putString(STRING_TYPED, stringTyped);
        return requestParam;
    }
}
