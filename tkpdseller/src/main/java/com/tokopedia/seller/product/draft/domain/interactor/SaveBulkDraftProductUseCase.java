package com.tokopedia.seller.product.draft.domain.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
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

public class SaveBulkDraftProductUseCase extends CompositeUseCase<List<Long>> {
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

    public static RequestParams generateUploadProductParam(Context context,
                                                           @NonNull ArrayList<String> localPathList,
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

            uploadProductInputDomainModel.setProductPriceCurrency(
                    Integer.parseInt(context.getString(R.string.product_currency_value_default)));
            uploadProductInputDomainModel.setProductWeightUnit(
                    Integer.parseInt(context.getString(R.string.product_weight_value_default))
            );
            uploadProductInputDomainModel.setProductUploadTo(UploadToTypeDef.TYPE_NOT_ACTIVE);
            uploadProductInputDomainModel.setProductReturnable(
                    Integer.parseInt(context.getString(R.string.product_free_return_values_default))
            );
            uploadProductInputDomainModel.setProductInvenageSwitch(
                    InvenageSwitchTypeDef.TYPE_NOT_ACTIVE);
            uploadProductInputDomainModel.setProductCondition(
                    Integer.parseInt(context.getString(R.string.product_condition_value_default))
            );
            uploadProductInputDomainModel.setProductMustInsurance(
                    Integer.parseInt(context.getString(R.string.product_insurance_value_default))
            );
            uploadProductInputDomainModelList.add(uploadProductInputDomainModel);
        }
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST, uploadProductInputDomainModelList);
        return params;
    }

    public static RequestParams generateUploadProductParam(ArrayList<UploadProductInputDomainModel> domainModel){
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST, domainModel);
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
