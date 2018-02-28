package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.edit.domain.mapper.ProductUploadMapper;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
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

public class UploadProductUseCase extends UseCase<AddProductDomainModel> {

    private static final String UPLOAD_PRODUCT_ID = "UPLOAD_PRODUCT_ID";
    private static final long UNSELECTED_PRODUCT_ID = Long.MIN_VALUE;

    private final FetchDraftProductUseCase fetchDraftProductUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final ProductDraftRepository productDraftRepository;
    private final ProductRepository productRepository;
    private ProductViewModel productViewModel;
    private ProductUploadMapper productUploadMapper;

    private AddProductNotificationListener listener;
    private UploadImageUseCase<UploadImageModel> uploadImageUseCase;

    @Inject
    public UploadProductUseCase(
            FetchDraftProductUseCase fetchDraftProductUseCase,
            GetProductDetailUseCase getProductDetailUseCase,
            ProductDraftRepository productDraftRepository,
            ProductRepository productRepository,
            UploadImageUseCase<UploadImageModel> uploadImageUseCase,
            ProductUploadMapper productUploadMapper) {
        this.fetchDraftProductUseCase = fetchDraftProductUseCase;
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.productDraftRepository = productDraftRepository;
        this.productRepository = productRepository;
        this.uploadImageUseCase = uploadImageUseCase;
        this.productUploadMapper = productUploadMapper;
    }

    public void setListener(AddProductNotificationListener listener) {
        this.listener = listener;
    }

    public static RequestParams generateUploadProductParam(long productId) {
        RequestParams params = RequestParams.create();
        params.putLong(UPLOAD_PRODUCT_ID, productId);
        return params;
    }

    @Override
    public Observable<AddProductDomainModel> createObservable(RequestParams requestParams) {
        long draftProductId = requestParams.getLong(UPLOAD_PRODUCT_ID, UNSELECTED_PRODUCT_ID);
        return fetchDraftProductUseCase.createObservable(FetchDraftProductUseCase.createRequestParams(draftProductId))
                .map(new Func1<ProductViewModel, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(ProductViewModel productViewModel) {
                        if (productViewModel == null) {
                            Observable.error(new RuntimeException("Draft is already deleted"));
                        }
                        UploadProductUseCase.this.productViewModel = productViewModel;
                        return productViewModel;
                    }
                })
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
                .flatMap(new UploadProduct(draftProductId, listener, productRepository, uploadImageUseCase))
                .doOnNext(new DeleteProductDraft(draftProductId, productDraftRepository))
                .doOnNext(new DeleteImageCacheDraftFile());
    }

    private Func1<ProductViewModel, ProductViewModel> removeUnusedParam(final ProductViewModel productFromDraft) {
        return new Func1<ProductViewModel, ProductViewModel>() {
            @Override
            public ProductViewModel call(ProductViewModel productFromServer) {
                return productUploadMapper.convertUnusedParamToNull(productFromServer, productFromDraft);
            }
        };
    }

    private class DeleteImageCacheDraftFile implements Action1<AddProductDomainModel> {
        @Override
        public void call(AddProductDomainModel addProductDomainModel) {
            List<ProductPictureViewModel> productPictureViewModels = productViewModel.getProductPictureViewModelList();
            if (productPictureViewModels == null || productPictureViewModels.size() == 0) {
                return;
            }
            ArrayList<String> pathToDelete = new ArrayList<>();
            for (int i = 0, sizei = productPictureViewModels.size(); i < sizei; i++) {
                ProductPictureViewModel productPictureViewModel = productPictureViewModels.get(i);
                if (productPictureViewModel == null) {
                    continue;
                }
                String imagePath = productPictureViewModel.getFilePath();
                if (!TextUtils.isEmpty(imagePath)) {
                    pathToDelete.add(imagePath);
                }
            }
            if (pathToDelete.size() > 0) {
                FileUtils.deleteAllCacheTkpdFiles(pathToDelete);
            }
        }
    }

    private static class DeleteProductDraft implements Action1<AddProductDomainModel> {
        private final long productId;
        private final ProductDraftRepository productDraftRepository;

        public DeleteProductDraft(long productId, ProductDraftRepository productDraftRepository) {
            this.productId = productId;
            this.productDraftRepository = productDraftRepository;
        }

        @Override
        public void call(AddProductDomainModel addProductDomainModel) {
            productDraftRepository.deleteDraft(productId);
        }
    }

}
