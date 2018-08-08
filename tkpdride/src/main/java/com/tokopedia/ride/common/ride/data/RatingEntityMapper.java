package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.RatingEntity;
import com.tokopedia.ride.common.ride.domain.model.Rating;

/**
 * Created by alvarisi on 6/5/17.
 */

public class RatingEntityMapper {
    public Rating transform(RatingEntity entity) {
        Rating rating = null;
        if (entity != null) {
            rating = new Rating();
            rating.setStar(entity.getStars());
            rating.setComment(entity.getComment());
        }
        return rating;
    }
}
