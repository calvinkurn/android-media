package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;

import rx.Subscriber;

/**
 * @author hendry on 4/3/17.
 */

public class CatalogPickerPresenterImpl extends CatalogPickerPresenter {
    private final FetchCatalogDataUseCase fetchCatalogDataUseCase;

    public CatalogPickerPresenterImpl(FetchCatalogDataUseCase fetchCatalogDataUseCase) {
        this.fetchCatalogDataUseCase = fetchCatalogDataUseCase;
    }

    @Override
    public void fetchCatalogData(String keyword, int departmentId, int start, int rows) {
        fetchCatalogDataUseCase.execute(
            FetchCatalogDataUseCase.createRequestParams(keyword, departmentId, start, rows),
            new Subscriber<CatalogDataModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().showError(e);
                }

                @Override
                public void onNext(CatalogDataModel catalogDataModel) {
                    getView().successFetchData(
                            catalogDataModel.getResult().getCatalogs(),
                            catalogDataModel.getResult().getTotalRecord());
                }
            });
    }

}
