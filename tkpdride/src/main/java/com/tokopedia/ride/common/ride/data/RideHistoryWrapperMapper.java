package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.bookingride.data.entity.PeopleAddressPagingEntity;
import com.tokopedia.ride.bookingride.domain.model.Paging;
import com.tokopedia.ride.common.ride.data.entity.RideHistoryResponse;
import com.tokopedia.ride.common.ride.domain.model.RideHistoryWrapper;

/**
 * Created by alvarisi on 7/19/17.
 */

public class RideHistoryWrapperMapper {
    private RideHistoryEntityMapper rideHistoryEntityMapper;
    public RideHistoryWrapperMapper() {
        rideHistoryEntityMapper = new RideHistoryEntityMapper();
    }

    public RideHistoryWrapper transform(RideHistoryResponse response){
        RideHistoryWrapper wrapper = null;
        if (response != null){
            wrapper = new RideHistoryWrapper();
            wrapper.setHistories(rideHistoryEntityMapper.transform(response.getHistories()));
            wrapper.setPaging(transform(response.getPaging()));
        }
        return wrapper;
    }

    public Paging transform(PeopleAddressPagingEntity entity){
        Paging paging = null;
        if (entity != null){
            paging = new Paging();
            paging.setNextUrl(entity.getUriNext());
            paging.setPage(1);
        }
        return paging;
    }
}
