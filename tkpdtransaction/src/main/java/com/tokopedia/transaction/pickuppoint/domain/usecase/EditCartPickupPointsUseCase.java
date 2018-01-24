package com.tokopedia.transaction.pickuppoint.domain.usecase;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.transaction.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.transaction.pickuppoint.data.repository.CartPickupPointRepository;
import com.tokopedia.transaction.pickuppoint.data.repository.PickupPointRepository;
import com.tokopedia.transaction.pickuppoint.domain.model.PickupPointResponse;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 5/1/18.
 */

public class EditCartPickupPointsUseCase extends UseCase<Response<TkpdResponse>> {

    private static final String PARAM_CART_ID = "cart_id";
    private static final String PARAM_OLD_STORE_ID = "old_store_id";
    private static final String PARAM_NEW_STORE_ID = "new_store_id";

    private final CartPickupPointRepository repository;

    @Inject
    public EditCartPickupPointsUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       CartPickupPointRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<Response<TkpdResponse>> createObservable(RequestParams requestParams) {
        return repository.editPickupPoints(requestParams.getParamsAllValueInString());
    }

    public static RequestParams generateParams(Context context, String cartId, String oldStoreId, String newStoreId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork(context));
        requestParams.putString(PARAM_CART_ID, cartId);
        requestParams.putString(PARAM_OLD_STORE_ID, oldStoreId);
        requestParams.putString(PARAM_NEW_STORE_ID, newStoreId);

        return requestParams;
    }

}
