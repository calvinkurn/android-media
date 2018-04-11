package com.tokopedia.seller.product.draft.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.ProductConditionTypeDef;
import com.tokopedia.seller.product.edit.constant.WeightUnitTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class SaveBulkDraftProductUseCase extends UseCase<List<Long>> {
    private static final String UPLOAD_PRODUCT_INPUT_MODEL_LIST = "UPLOAD_PRODUCT_INPUT_MODEL_LIST";
    private final ProductDraftRepository productDraftRepository;

    @Inject
    public SaveBulkDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ProductDraftRepository productDraftRepository) {
        super(threadExecutor, postExecutionThread);
        this.productDraftRepository = productDraftRepository;
    }

    @Override
    public Observable<List<Long>> createObservable(RequestParams requestParams) {
        ArrayList<ProductViewModel> inputModelList =
                (ArrayList<ProductViewModel>) requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST);
        return Observable.from(inputModelList)
                .flatMap(new Func1<ProductViewModel, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(ProductViewModel productViewModel) {
                        return productDraftRepository.saveDraft(productViewModel, false);
                    }
                })
                .toList();
    }

    public static RequestParams generateUploadProductParam(@NonNull ArrayList<String> localPathList,
                                                           @NonNull ArrayList<String> instagramDescList){

        ArrayList<ProductViewModel> productViewModels = new ArrayList<>();

        for (int i=0, sizei = localPathList.size(); i < sizei ; i++) {
            String localPath = localPathList.get(i);

            ProductViewModel productViewModel = new ProductViewModel();

            productViewModel.setProductDescription(instagramDescList.get(i) == null? "": instagramDescList.get(i));

            List<ProductPictureViewModel> productPictureViewModelList = new ArrayList<>();
            ProductPictureViewModel productPictureViewModel = new ProductPictureViewModel();
            productPictureViewModel.setFilePath(localPath);
            productPictureViewModel.setId(0);

            productPictureViewModelList.add(productPictureViewModel);
            productViewModel.setProductPictureViewModelList(productPictureViewModelList);

            productViewModel.setProductPriceCurrency( CurrencyTypeDef.TYPE_IDR);
            productViewModel.setProductWeightUnit(WeightUnitTypeDef.TYPE_GRAM);

            productViewModel.setProductCondition(ProductConditionTypeDef.TYPE_NEW);
            productViewModels.add(productViewModel);
        }
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST, productViewModels);
        return params;
    }

}
