package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class ImagePickerCatalogPresenter extends BaseDaggerPresenter<ImagePickerCatalogContract.View>
        implements ImagePickerCatalogContract.Presenter {

    private GetCatalogImageUseCase getCatalogImageUseCase;

    public ImagePickerCatalogPresenter(GetCatalogImageUseCase getCatalogImageUseCase) {
        this.getCatalogImageUseCase = getCatalogImageUseCase;
    }

    @Override
    public void getCatalogImage(String catalogId) {
        getCatalogImageUseCase.execute(getCatalogImageUseCase.createRequestParams(catalogId), getSubscriberCatalogImage());
    }

    private Subscriber<List<CatalogModelView>> getSubscriberCatalogImage() {
        return new Subscriber<List<CatalogModelView>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<CatalogModelView> catalogModelViews) {
                getView().renderList(catalogModelViews);
            }
        };
    }

    @Override
    public void detachView() {
        getCatalogImageUseCase.unsubscribe();
        super.detachView();
    }
}
