package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveBulkDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.edit.view.holder.ProductImageViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftSaveBulkPresenterImpl extends ProductDraftSaveBulkPresenter {
    private SaveBulkDraftProductUseCase saveBulkDraftProductUseCase;

    public ProductDraftSaveBulkPresenterImpl(SaveBulkDraftProductUseCase saveBulkDraftProductUseCase){
        this.saveBulkDraftProductUseCase = saveBulkDraftProductUseCase;
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
            getView().onSaveBulkDraftSuccess(null);
            return;
        }
        saveBulkDraftProductUseCase.execute(
                SaveBulkDraftProductUseCase.generateUploadProductParam(
                        correctResolutionLocalPathList, correctResolutionInstagramDescList),
                getSaveInstagramToDraftSubscriber());
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

    @Override
    public void detachView() {
        super.detachView();
        saveBulkDraftProductUseCase.unsubscribe();
    }

}
