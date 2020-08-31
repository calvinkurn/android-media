package com.tokopedia.seller.manageitem.data.source;


import com.tokopedia.seller.manageitem.data.cloud.GenerateHostCloud;
import com.tokopedia.seller.manageitem.data.cloud.model.generatehost.GenerateHost;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class GenerateHostDataSource {

    private final GenerateHostCloud generateHostCloud;

    @Inject
    public GenerateHostDataSource(GenerateHostCloud generateHostCloud) {
        this.generateHostCloud = generateHostCloud;
    }

    public Observable<GenerateHost> generateHost() {
        return generateHostCloud.generateHost();
    }
}
