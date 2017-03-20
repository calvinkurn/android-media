package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;

import rx.Observable;

/**
 * Created by hangnadi on 3/16/17.
 */

public class TrackAwbReturProductUseCase extends UseCase<TrackingAwbReturProduct> {

    public static final String PARAM_SHIPMENT_ID = "shipment_id";
    public static final String PARAM_SHIPPING_REFENCE = "shipping_ref";

    private final ResCenterRepository rescCenterRepository;

    public TrackAwbReturProductUseCase(JobExecutor jobExecutor,
                                       UIThread uiThread,
                                       ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.rescCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<TrackingAwbReturProduct> createObservable(RequestParams requestParams) {
        return rescCenterRepository.getTrackingAwbReturProduct(requestParams.getParameters());
    }
}
