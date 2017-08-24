package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.TipListEntity;
import com.tokopedia.ride.completetrip.domain.model.TipList;

/**
 * Created by alvarisi on 6/5/17.
 */

public class TipListEntityMapper {
    public TipList transform(TipListEntity entity) {
        TipList tipList = null;
        if (tipList != null) {
            tipList = new TipList();
            tipList.setEnabled(entity.getEnabled());
            tipList.setList(entity.getList());
        }
        return tipList;
    }
}
