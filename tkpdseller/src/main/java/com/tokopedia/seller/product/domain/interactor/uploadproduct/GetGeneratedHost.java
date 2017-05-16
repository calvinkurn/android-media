package com.tokopedia.seller.product.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.domain.GenerateHostRepository;
import com.tokopedia.seller.product.domain.model.GenerateHostDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class GetGeneratedHost implements Func1<UploadProductInputDomainModel, Observable<GenerateHostDomainModel>> {
    private final GenerateHostRepository generateHostRepository;

    public GetGeneratedHost(GenerateHostRepository generateHostRepository) {
        this.generateHostRepository = generateHostRepository;
    }

    @Override
    public Observable<GenerateHostDomainModel> call(UploadProductInputDomainModel domainModel) {
        return generateHostRepository.generateHost();
    }
}