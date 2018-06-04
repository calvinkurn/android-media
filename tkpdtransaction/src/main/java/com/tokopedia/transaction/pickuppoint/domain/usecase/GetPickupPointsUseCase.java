package com.tokopedia.transaction.pickuppoint.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.addtocart.model.OrderData;
import com.tokopedia.transaction.pickuppoint.data.repository.PickupPointRepository;
import com.tokopedia.transaction.common.data.pickuppoint.PickupPointResponse;
import com.tokopedia.transaction.common.data.pickuppoint.Store;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

import com.tokopedia.transaction.common.constant.PickupPointParamConstant;


/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class GetPickupPointsUseCase extends UseCase<PickupPointResponse> {

    private final PickupPointRepository repository;

    @Inject
    public GetPickupPointsUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  PickupPointRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<PickupPointResponse> createObservable(RequestParams requestParams) {
        return repository.getPickupPoints(requestParams.getParamsAllValueInString());
    }

    public static HashMap<String, String> generateParams(OrderData orderData) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PickupPointParamConstant.PARAM_DISTRICT_ID,
                String.valueOf(orderData.getAddress().getDistrictId()));
        params.put(PickupPointParamConstant.PARAM_PAGE, PickupPointParamConstant.DEFAULT_PAGE);
        params.put(PickupPointParamConstant.PARAM_TOKEN,
                orderData.getShop().getTokenPickup() != null ? orderData.getShop().getTokenPickup() : "");
        params.put(PickupPointParamConstant.PARAM_UT, String.valueOf(orderData.getShop().getUt()));

        return params;
    }

    public static HashMap<String, String> generateParams(Store store) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PickupPointParamConstant.PARAM_DISTRICT_ID, String.valueOf(store.getDistrictId()));
        params.put(PickupPointParamConstant.PARAM_PAGE, PickupPointParamConstant.DEFAULT_PAGE);
//        params.put(GetPickupPointsUseCase.PARAM_TOKEN,
//                atcFormData.getShop().getTokenPickup() != null ? atcFormData.getShop().getTokenPickup() : "");
//        params.put(GetPickupPointsUseCase.PARAM_UT, String.valueOf(atcFormData.getShop().getUt()));

        return params;
    }

/*  TODO : Next implementation
    public static HashMap<String, String> generateParams(MultipleAddressShipmentAdapterData data) {
        HashMap<String, String> params = new HashMap<>();
        params.put(GetPickupPointsUseCase.PARAM_DISTRICT_ID,
                String.valueOf(data.getDestinationDistrictId()));
        params.put(GetPickupPointsUseCase.PARAM_PAGE, GetPickupPointsUseCase.DEFAULT_PAGE);
        params.put(GetPickupPointsUseCase.PARAM_TOKEN,
                data.getTokenPickup() != null ? data.getTokenPickup() : "");
        params.put(GetPickupPointsUseCase.PARAM_UT, data.getUnixTime());

        return params;
    }

    public static HashMap<String, String> generateParams(RecipientAddressModel data) {
        HashMap<String, String> params = new HashMap<>();
        params.put(GetPickupPointsUseCase.PARAM_DISTRICT_ID,
                String.valueOf(data.getDestinationDistrictId()));
        params.put(GetPickupPointsUseCase.PARAM_PAGE, GetPickupPointsUseCase.DEFAULT_PAGE);
        params.put(GetPickupPointsUseCase.PARAM_TOKEN,
                data.getTokenPickup() != null ? data.getTokenPickup() : "");
        params.put(GetPickupPointsUseCase.PARAM_UT, data.getUnixTime());

        return params;
    }
*/

}
