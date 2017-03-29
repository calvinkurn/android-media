package com.tokopedia.seller.shop.setting.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;
import com.tokopedia.seller.shop.setting.domain.model.LogisticAvailableDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by sebastianuskh on 3/27/17.
 */

public class GetLogisticAvailableUseCase extends UseCase<LogisticAvailableDomainModel> {

    public static final String DISTRICT_CODE = "DISTRICT_CODE";
    public static final int UNSELECTED_DISTRICT = -1;
    private final DistrictLogisticDataRepository districLogisticDataRepository;

    @Inject
    public GetLogisticAvailableUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, DistrictLogisticDataRepository districLogisticDataRepository) {
        super(threadExecutor, postExecutionThread);
        this.districLogisticDataRepository = districLogisticDataRepository;
    }

    public static RequestParams generateParams(int districtCode) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putInt(DISTRICT_CODE, districtCode);
        return requestParam;
    }

    @Override
    public Observable<LogisticAvailableDomainModel> createObservable(RequestParams requestParams) {
        int districtCode = requestParams.getInt(DISTRICT_CODE, UNSELECTED_DISTRICT);
        if (districtCode == UNSELECTED_DISTRICT) {
            throw new RuntimeException("District Must Be Selected");
        }
        return districLogisticDataRepository.getLogisticAvailable(districtCode);
    }
}
