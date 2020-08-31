package com.tokopedia.seller.manageitem.domain.repository;


import com.tokopedia.seller.manageitem.common.mapper.GenerateHostMapper;
import com.tokopedia.seller.manageitem.data.model.GenerateHostDomainModel;
import com.tokopedia.seller.manageitem.data.source.GenerateHostDataSource;

import rx.Observable;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class GenerateHostRepositoryImpl implements GenerateHostRepository {
    private final GenerateHostDataSource generateHostDataSource;

    public GenerateHostRepositoryImpl(GenerateHostDataSource generateHostDataSource) {
        this.generateHostDataSource = generateHostDataSource;
    }

    @Override
    public Observable<GenerateHostDomainModel> generateHost() {
        return generateHostDataSource.generateHost()
                .map(new GenerateHostMapper());
    }
}
