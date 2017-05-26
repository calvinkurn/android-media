package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.cloud.GenerateHostCloud;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHost;

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
