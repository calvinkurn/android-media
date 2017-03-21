package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by alvarisi on 3/20/17.
 */

public class GetProductAndEstimatedUseCase extends UseCase<List<ProductEstimate>> {
    private final BookingRideRepository mBookingRideRepository;
    private GetEstimatedTimesUseCase getEstimatedTimesUseCase;
    private GetUberProductsUseCase getUberProductsUseCase;
    public GetProductAndEstimatedUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         BookingRideRepository mBookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.mBookingRideRepository = mBookingRideRepository;
        getEstimatedTimesUseCase = new GetEstimatedTimesUseCase(threadExecutor, postExecutionThread, mBookingRideRepository);
        getUberProductsUseCase = new GetUberProductsUseCase(threadExecutor, postExecutionThread, mBookingRideRepository);
    }

    @Override
    public Observable<List<ProductEstimate>> createObservable(RequestParams requestParams) {
        return Observable.zip(getUberProductsUseCase.createObservable(requestParams),
                getEstimatedTimesUseCase.createObservable(requestParams), new Func2<List<Product>, List<TimesEstimate>, List<ProductEstimate>>() {
                    @Override
                    public List<ProductEstimate> call(List<Product> products,
                                                      List<TimesEstimate> timesEstimates) {
                        List<ProductEstimate> productEstimates = new ArrayList<>();
                        ProductEstimate productEstimate = null;
                        for (Product product : products){
                            productEstimate = new ProductEstimate();
                            productEstimate.setProduct(product);
                            for (TimesEstimate estimate : timesEstimates){
                                if (estimate.getProductId().equals(product.getProductId())){
                                    productEstimate.setTimesEstimate(estimate);
                                    break;
                                }
                            }
                            productEstimates.add(productEstimate);
                        }
                        return productEstimates;
                    }
                });
    }
}
