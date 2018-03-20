package com.tokopedia.tkpdtrain.search.data.typedef;

import android.support.annotation.IntDef;

import static com.tokopedia.tkpdtrain.search.data.typedef.TrainScheduleTypeDef.DEPARTURE_SCHEDULE;
import static com.tokopedia.tkpdtrain.search.data.typedef.TrainScheduleTypeDef.RETURN_SCHEDULE;

/**
 * Created by nabillasabbaha on 3/14/18.
 */
@IntDef({DEPARTURE_SCHEDULE, RETURN_SCHEDULE})
public @interface TrainScheduleTypeDef {
    int DEPARTURE_SCHEDULE = 1;
    int RETURN_SCHEDULE = 2;
}
