package com.tokopedia.ride.base.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.base.data.entity.ProductEntity;
import com.tokopedia.ride.base.domain.BookingRideRepository;
import com.tokopedia.ride.base.domain.model.Product;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 3/14/17.
 */

public class BookingRideRepositoryData implements BookingRideRepository {
    private final BookingRideDataStoreFactory mBookingRideDataStoreFactory;
    private final ProductEntityMapper mProductEntityMapper;

    public BookingRideRepositoryData(BookingRideDataStoreFactory bookingRideDataStoreFactory, ProductEntityMapper productEntityMapper) {
        mBookingRideDataStoreFactory = bookingRideDataStoreFactory;
        mProductEntityMapper = productEntityMapper;
    }

    @Override
    public Observable<List<Product>> getProducts(TKPDMapParam<String, Object> productParams) {
        return mBookingRideDataStoreFactory.createCloudDataStore()
                .getProducts(productParams).map(new Func1<List<ProductEntity>, List<Product>>() {
                    @Override
                    public List<Product> call(List<ProductEntity> entities) {
                        return mProductEntityMapper.transform(entities);
                    }
                });
    }


}
