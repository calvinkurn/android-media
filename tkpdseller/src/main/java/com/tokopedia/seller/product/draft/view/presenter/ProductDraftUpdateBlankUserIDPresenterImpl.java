package com.tokopedia.seller.product.draft.view.presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.tokopedia.seller.product.draft.domain.interactor.DeleteSingleDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveBulkDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateBlankUserIdDraftUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.view.mapper.ProductDraftListMapper;
import com.tokopedia.seller.product.draft.view.model.ProductDraftViewModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.holder.ProductImageViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftUpdateBlankUserIDPresenterImpl extends ProductDraftUpdateBlankUserIDPresenter {
    private UpdateBlankUserIdDraftUseCase updateBlankUserIdDraftUseCase;
    public ProductDraftUpdateBlankUserIDPresenterImpl(UpdateBlankUserIdDraftUseCase updateBlankUserIdDraftUseCase){
        this.updateBlankUserIdDraftUseCase = updateBlankUserIdDraftUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        updateBlankUserIdDraftUseCase.unsubscribe();
    }

    @Override
    public void updateBlankUserId() {
        updateBlankUserIdDraftUseCase.execute(UpdateBlankUserIdDraftUseCase.createRequestParams(),
                getSubscriber());
    }

    private Subscriber<Boolean> getSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

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
}