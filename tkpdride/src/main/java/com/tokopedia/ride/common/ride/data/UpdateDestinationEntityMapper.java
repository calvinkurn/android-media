package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.UpdateDestinationEntity;
import com.tokopedia.ride.common.ride.domain.model.UpdateDestination;

/**
 * Created by alvarisi on 3/31/17.
 */

public class UpdateDestinationEntityMapper {
    private PendingPaymentEntityMapper pendingPaymentEntityMapper;

    public UpdateDestinationEntityMapper() {
        pendingPaymentEntityMapper = new PendingPaymentEntityMapper();
    }

    public UpdateDestination transform(UpdateDestinationEntity entity) {
        UpdateDestination updateDestination = null;
        if (entity != null) {
            updateDestination = new UpdateDestination();
            updateDestination.setPendingPayment(pendingPaymentEntityMapper.transform(entity.getPendingPayment()));
        }
        return updateDestination;
    }
}
