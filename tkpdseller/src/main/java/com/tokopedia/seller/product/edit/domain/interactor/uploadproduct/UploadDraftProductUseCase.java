package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.seller.product.draft.domain.interactor.DeleteSingleDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.seller.product.edit.domain.listener.NotificationCountListener;
import com.tokopedia.seller.product.edit.domain.mapper.ProductUploadMapper;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class UploadDraftProductUseCase extends UseCase<ProductViewModel> {

    private static final String NOTIFICATION_COUNT_LISTENER = "NOTIFICATION_COUNT_LISTENER";
    private static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";

    private static final long UNSELECTED_PRODUCT_ID = Long.MIN_VALUE;

    private final FetchDraftProductUseCase fetchDraftProductUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final UploadProductImageUseCase uploadProductImageUseCase;
    private final SubmitProductUseCase submitProductUseCase;
    private final DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase;
    private final ProductUploadMapper productUploadMapper;

    private NotificationCountListener notificationCountListener;

    @Inject
    public UploadDraftProductUseCase(
            FetchDraftProductUseCase fetchDraftProductUseCase,
            GetProductDetailUseCase getProductDetailUseCase,
            UploadProductImageUseCase uploadProductImageUseCase,
            SubmitProductUseCase submitProductUseCase,
            DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
            ProductUploadMapper productUploadMapper) {
        this.fetchDraftProductUseCase = fetchDraftProductUseCase;
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.uploadProductImageUseCase = uploadProductImageUseCase;
        this.submitProductUseCase = submitProductUseCase;
        this.deleteSingleDraftProductUseCase = deleteSingleDraftProductUseCase;
        this.productUploadMapper = productUploadMapper;
    }

    public static RequestParams createParams(long draftProductId) {
        return createParams(draftProductId, null);
    }

    public static RequestParams createParams(long draftProductId, NotificationCountListener notificationCountListener) {
        RequestParams params = RequestParams.create();
        params.putLong(DRAFT_PRODUCT_ID, draftProductId);
        params.putObject(NOTIFICATION_COUNT_LISTENER, notificationCountListener);
        return params;
    }

    @Override
    public Observable<ProductViewModel> createObservable(RequestParams requestParams) {
        final long draftProductId = requestParams.getLong(DRAFT_PRODUCT_ID, UNSELECTED_PRODUCT_ID);
        notificationCountListener = (NotificationCountListener) requestParams.getObject(NOTIFICATION_COUNT_LISTENER);
        return fetchDraftProductUseCase.createObservable(FetchDraftProductUseCase.createRequestParams(draftProductId))
                .map(new Func1<ProductViewModel, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(ProductViewModel productViewModel) {
                        if (productViewModel == null) {
                            Observable.error(new RuntimeException("Draft is already deleted"));
                        }
                        return productViewModel;
                    }
                })
                .doOnNext(new Action1<ProductViewModel>() {
                    @Override
                    public void call(ProductViewModel productViewModel) {
                        notificationCountListener.setProductName(productViewModel.getProductName());
                    }
                })
                .doOnNext(new UpdateCountListener(notificationCountListener))
                .flatMap(new Func1<ProductViewModel, Observable<ProductViewModel>>() {
                    @Override
                    public Observable<ProductViewModel> call(ProductViewModel productFromDraft) {
                        if (TextUtils.isEmpty(productFromDraft.getProductId())) {
                            return Observable.just(productFromDraft);
                        } else {
                            return getProductDetailUseCase.createObservable(GetProductDetailUseCase.createParams(productFromDraft.getProductId()))
                                    .map(removeUnusedParam(productFromDraft));
                        }
                    }
                })
                .doOnNext(new UpdateCountListener(notificationCountListener))
                .flatMap(new Func1<ProductViewModel, Observable<ProductViewModel>>() {
                    @Override
                    public Observable<ProductViewModel> call(final ProductViewModel productViewModel) {
                        return uploadProductImageUseCase.createObservable(UploadProductImageUseCase.createParams(productViewModel))
                                .map(new Func1<List<ProductPictureViewModel>, ProductViewModel>() {
                                    @Override
                                    public ProductViewModel call(List<ProductPictureViewModel> productPictureViewModelList) {
                                        productViewModel.setProductPictureViewModelList(productPictureViewModelList);
                                        return productViewModel;
                                    }
                                });
                    }
                })
                .doOnNext(new UpdateCountListener(notificationCountListener))
                .flatMap(new Func1<ProductViewModel, Observable<ProductViewModel>>() {
                    @Override
                    public Observable<ProductViewModel> call(ProductViewModel productViewModel) {
                        return submitProductUseCase.createObservable(SubmitProductUseCase.createParams(productViewModel));
                    }
                })
                .doOnNext(new UpdateCountListener(notificationCountListener))
                .doOnNext(new Action1<ProductViewModel>() {
                    @Override
                    public void call(ProductViewModel addProductDomainModel) {
                        deleteSingleDraftProductUseCase.executeSync(DeleteSingleDraftProductUseCase.createRequestParams(draftProductId));
                    }
                })
                .doOnNext(new UpdateCountListener(notificationCountListener))
                .doOnNext(new DeleteImageCacheDraftFile())
                .doOnNext(new UpdateCountListener(notificationCountListener));
    }

    private Func1<ProductViewModel, ProductViewModel> removeUnusedParam(final ProductViewModel productFromDraft) {
        return new Func1<ProductViewModel, ProductViewModel>() {
            @Override
            public ProductViewModel call(ProductViewModel productFromServer) {
                return productUploadMapper.convertUnusedParamToNull(productFromServer, productFromDraft);
            }
        };
    }

    private class DeleteImageCacheDraftFile implements Action1<ProductViewModel> {
        @Override
        public void call(ProductViewModel productViewModel) {
            List<ProductPictureViewModel> productPictureViewModelList = productViewModel.getProductPictureViewModelList();
            if (productPictureViewModelList == null || productPictureViewModelList.size() == 0) {
                return;
            }
            ArrayList<String> pathToDeleteList = new ArrayList<>();
            for (ProductPictureViewModel productPictureViewModel : productPictureViewModelList) {
                if (productPictureViewModel == null) {
                    continue;
                }
                String imagePath = productPictureViewModel.getFilePath();
                if (!TextUtils.isEmpty(imagePath)) {
                    pathToDeleteList.add(imagePath);
                }
            }
            if (pathToDeleteList.size() > 0) {
                FileUtils.deleteAllCacheTkpdFiles(pathToDeleteList);
            }
        }
    }

    private class UpdateCountListener implements Action1<ProductViewModel> {

        private final NotificationCountListener notificationCountListener;

        public UpdateCountListener(NotificationCountListener notificationCountListener) {
            this.notificationCountListener = notificationCountListener;
        }

        @Override
        public void call(ProductViewModel productViewModel) {
            notificationCountListener.addProgress();
        }
    }
}