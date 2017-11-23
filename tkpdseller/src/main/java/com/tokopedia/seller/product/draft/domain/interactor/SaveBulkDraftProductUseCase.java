package com.tokopedia.seller.product.draft.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.FreeReturnTypeDef;
import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.constant.ProductConditionTypeDef;
import com.tokopedia.seller.product.edit.constant.ProductInsuranceValueTypeDef;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.constant.WeightUnitTypeDef;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

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
        ArrayList<UploadProductInputDomainModel> inputModelList =
                (ArrayList<UploadProductInputDomainModel>) requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST);
        return Observable.from(inputModelList)
                .flatMap(new SaveDraft(0, false))
                .toList();
    }

    public static RequestParams generateUploadProductParam(@NonNull ArrayList<String> localPathList,
                                                           @NonNull ArrayList<String> instagramDescList){

        ArrayList<UploadProductInputDomainModel> uploadProductInputDomainModelList = new ArrayList<>();

        for (int i=0, sizei = localPathList.size(); i < sizei ; i++) {
            String localPath = localPathList.get(i);

            UploadProductInputDomainModel uploadProductInputDomainModel = new UploadProductInputDomainModel();

            uploadProductInputDomainModel.setProductDescription(instagramDescList.get(i) == null? "": instagramDescList.get(i));

            ProductPhotoListDomainModel productPhotoListDomainModel = new ProductPhotoListDomainModel();
            productPhotoListDomainModel.setProductDefaultPicture(0);
            ArrayList<ImageProductInputDomainModel> imageProductInputDomainModelArrayList = new ArrayList<>();
            ImageProductInputDomainModel imageProductInputDomainModel = new ImageProductInputDomainModel();
            imageProductInputDomainModel.setImagePath(localPath);
            imageProductInputDomainModelArrayList.add(imageProductInputDomainModel);
            productPhotoListDomainModel.setPhotos(imageProductInputDomainModelArrayList);
            uploadProductInputDomainModel.setProductPhotos(productPhotoListDomainModel);

            uploadProductInputDomainModel.setProductPriceCurrency( CurrencyTypeDef.TYPE_IDR);
            uploadProductInputDomainModel.setProductWeightUnit(WeightUnitTypeDef.TYPE_GRAM);

            uploadProductInputDomainModel.setProductUploadTo(UploadToTypeDef.TYPE_NOT_ACTIVE);
            uploadProductInputDomainModel.setProductReturnable(FreeReturnTypeDef.TYPE_ACTIVE);

            uploadProductInputDomainModel.setProductInvenageSwitch(
                    InvenageSwitchTypeDef.TYPE_NOT_ACTIVE);
            uploadProductInputDomainModel.setProductCondition(ProductConditionTypeDef.TYPE_NEW);

            uploadProductInputDomainModel.setProductMustInsurance(ProductInsuranceValueTypeDef.TYPE_OPTIONAL);
            uploadProductInputDomainModelList.add(uploadProductInputDomainModel);
        }
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST, uploadProductInputDomainModelList);
        return params;
    }

    private class SaveDraft implements Func1<UploadProductInputDomainModel, Observable<Long>> {
        boolean isUploading;
        long previousDraftId;
        SaveDraft(long previousDraftId, boolean isUploading){
            this.previousDraftId = previousDraftId;
            this.isUploading = isUploading;
        }
        @Override
        public Observable<Long> call(UploadProductInputDomainModel inputModel) {
            if (previousDraftId <= 0) {
                return productDraftRepository.saveDraft(inputModel, isUploading);
            } else {
                return productDraftRepository.updateDraftToUpload(previousDraftId, inputModel, isUploading);
            }
        }
    }
}
