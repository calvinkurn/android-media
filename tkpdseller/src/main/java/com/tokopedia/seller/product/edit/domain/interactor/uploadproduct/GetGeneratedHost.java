package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.product.edit.domain.model.GenerateHostDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

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