package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;

/**
 * Created by alvarisi on 3/31/17.
 */

public class PromoEntityMapper {
    
    public PromoEntityMapper() {
    }

    public Promo transform(PromoEntity entity) {
        return new Promo();
    }
}
