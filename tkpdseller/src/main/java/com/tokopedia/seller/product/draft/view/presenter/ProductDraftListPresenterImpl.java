package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.myproduct.ManageProductSeller;
import com.tokopedia.seller.product.draft.domain.interactor.DeleteSingleDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveBulkDraftProductUseCase;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.view.mapper.ProductDraftListMapper;
import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.edit.view.holder.ProductImageViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftListPresenterImpl extends ProductDraftListPresenter {
    private FetchAllDraftProductUseCase fetchAllDraftProductUseCase;
    private DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase;
    private UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;
    private SaveBulkDraftProductUseCase saveBulkDraftProductUseCase;

    public ProductDraftListPresenterImpl (FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                          DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                          UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                          SaveBulkDraftProductUseCase saveBulkDraftProductUseCase){
        this.fetchAllDraftProductUseCase = fetchAllDraftProductUseCase;
        this.deleteSingleDraftProductUseCase = deleteSingleDraftProductUseCase;
        this.saveBulkDraftProductUseCase = saveBulkDraftProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
    }

    @Override
    public void fetchAllDraftData() {
        fetchAllDraftProductUseCase.execute(FetchAllDraftProductUseCase.createRequestParams(),
                getSubscriber());
    }

    @Override
    public void fetchAllDraftDataWithUpdateUploading() {
        updateUploadingDraftProductUseCase.execute(UpdateUploadingDraftProductUseCase.createRequestParamsUpdateAll(false),
                getUpdateUploadingSubscriber());
    }

    @Override
    public void deleteProductDraft(long draftId) {
        deleteSingleDraftProductUseCase.execute(DeleteSingleDraftProductUseCase.createRequestParams(draftId),
                getDeleteSubscriber());
    }

    @Override
    public void saveInstagramToDraft( Context context,
                                      @NonNull ArrayList<String> localPathList,
                                      @NonNull ArrayList<String> instagramDescList) {
        ArrayList<String> correctResolutionLocalPathList = new ArrayList<>();
        ArrayList<String> correctResolutionInstagramDescList = new ArrayList<>();
        for (int i=0, sizei = localPathList.size(); i < sizei ; i++) {
            String localPath = localPathList.get(i);
            if (!isResolutionCorrect(localPath)) {
                getView().onSaveInstagramResolutionError(i + 1, localPath);
                continue;
            }
            correctResolutionLocalPathList.add(localPath);
            correctResolutionInstagramDescList.add(instagramDescList.get(i));
        }
        if (correctResolutionLocalPathList.size() == 0) {
            return;
        }
        saveBulkDraftProductUseCase.execute(
                SaveBulkDraftProductUseCase.generateUploadProductParam(
                        correctResolutionLocalPathList, correctResolutionInstagramDescList),
                getSaveInstagramToDraftSubscriber());
    }

    private Subscriber<List<Long>> getSaveInstagramToDraftSubscriber() {
        return new Subscriber<List<Long>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onSaveBulkDraftError(e);
                }
            }

            @Override
            public void onNext(List<Long> productIds) {
                if(isViewAttached()) {
                    getView().onSaveBulkDraftSuccess(productIds);
                }
            }
        };
    }

    private boolean isResolutionCorrect(String localPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(localPath).getAbsolutePath(), options);
        if (Math.min(options.outWidth, options.outHeight) >= ProductImageViewHolder.MIN_IMG_RESOLUTION){
            return true;
        }
        return false;
    }

    private Subscriber<List<UploadProductInputDomainModel>> getSubscriber(){
        return new Subscriber<List<UploadProductInputDomainModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                if(isViewAttached()) {
                    getView().onLoadSearchError(t);
                }
            }

            @Override
            public void onNext(List<UploadProductInputDomainModel> uploadProductInputDomainModels) {
                if (uploadProductInputDomainModels == null || uploadProductInputDomainModels.size() == 0 ) {
                    getView().onSearchLoaded(new ArrayList<ProductDraftViewModel>(), 0);
                } else {
                    // map to View Model
                    List<ProductDraftViewModel> viewModelList = new ArrayList<>();
                    for (int i=0, sizei = uploadProductInputDomainModels.size(); i<sizei; i++) {
                        UploadProductInputDomainModel domainModel = uploadProductInputDomainModels.get(i);
                        viewModelList.add(ProductDraftListMapper.mapDomainToView(domainModel));
                    }
                    getView().onSearchLoaded(viewModelList, viewModelList.size());
                }

            }
        };
    }

    public Subscriber<Boolean> getDeleteSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                // no op
            }

            @Override
            public void onError(Throwable e) {
                // no op
            }

            @Override
            public void onNext(Boolean aBoolean) {
                // no op
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchAllDraftProductUseCase.unsubscribe();
        deleteSingleDraftProductUseCase.unsubscribe();
        updateUploadingDraftProductUseCase.unsubscribe();
        saveBulkDraftProductUseCase.unsubscribe();
    }

    public Subscriber<Boolean> getUpdateUploadingSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                fetchAllDraftData();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                fetchAllDraftData();
            }
        };
    }
}