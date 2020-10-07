package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.tokopedia.seller.product.draft.domain.interactor.SaveInstagramToProductDraftUseCase;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftSaveBulkPresenterImpl extends ProductDraftSaveBulkPresenter {
    public static final int MIN_IMG_RESOLUTION = 300;
    private SaveInstagramToProductDraftUseCase saveInstagramToProductDraftUseCase;

    public ProductDraftSaveBulkPresenterImpl(SaveInstagramToProductDraftUseCase saveInstagramToProductDraftUseCase){
        this.saveInstagramToProductDraftUseCase = saveInstagramToProductDraftUseCase;
    }

    @Override
    public void saveInstagramToDraft( Context context,
                                     @NonNull ArrayList<String> localPathList,
                                     @NonNull ArrayList<String> instagramDescList) {
        ArrayList<String> correctResolutionLocalPathList = new ArrayList<>();
        ArrayList<String> correctResolutionInstagramDescList = new ArrayList<>();
        ArrayList<Integer> failedPositionArrayList = new ArrayList<>();
        for (int i=0, sizei = localPathList.size(); i < sizei ; i++) {
            String localPath = localPathList.get(i);
            if (!isResolutionCorrect(localPath)) {
                failedPositionArrayList.add(i+1);
                continue;
            }
            correctResolutionLocalPathList.add(localPath);
            try {
                correctResolutionInstagramDescList.add(instagramDescList.get(i));
            }catch (Exception e) {
                correctResolutionInstagramDescList.add("");
            }
        }
        if (failedPositionArrayList.size() > 0) {
            getView().onErrorSaveBulkDraft(new ResolutionImageException(failedPositionArrayList));
        }
        if (correctResolutionLocalPathList.size() == 0 ) {
            getView().hideDraftLoading();
            return;
        }

        saveInstagramToProductDraftUseCase.execute(
                SaveInstagramToProductDraftUseCase.Companion.generateUploadProductParam(
                        correctResolutionLocalPathList, correctResolutionInstagramDescList),
                getSaveInstagramToProductDraftSubscriber());
    }

    private boolean isResolutionCorrect(String localPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(localPath).getAbsolutePath(), options);
        if (Math.min(options.outWidth, options.outHeight) >= MIN_IMG_RESOLUTION){
            return true;
        }
        return false;
    }

    private Subscriber<List<Long>> getSaveInstagramToDraftSubscriber() {
        return new Subscriber<List<Long>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onErrorSaveBulkDraft(e);
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

    // this is for seller app
    private Subscriber<List<? extends Long>> getSaveInstagramToProductDraftSubscriber() {
        return new Subscriber<List<? extends Long>>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onErrorSaveBulkDraft(e);
                }
            }

            @Override
            public void onNext(List<? extends Long> productIds) {
                if(isViewAttached()) {
                    if(productIds != null) {
                        List<Long> productIdList = new LinkedList<Long>(productIds);
                        getView().onSaveBulkDraftSuccess(productIdList);
                    }
                }
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        saveInstagramToProductDraftUseCase.unsubscribe();
    }

}
