package com.tokopedia.tkpdtrain.common.data;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;

/**
 * @author  by alvarisi on 3/5/18.
 */

public class TrainRepositoryImpl implements TrainRepository {
    private TrainDataStoreFactory dataStoreFactory;

    public TrainRepositoryImpl(TrainDataStoreFactory dataStoreFactory){
        this.dataStoreFactory = dataStoreFactory;
    }


}
