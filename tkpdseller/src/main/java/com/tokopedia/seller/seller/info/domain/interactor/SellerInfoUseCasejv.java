package com.tokopedia.seller.seller.info.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModelA;
import com.tokopedia.seller.seller.info.domain.SellerInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoUseCasejv extends UseCase<ResponseSellerInfoModelA> {

    private SellerInfoRepository sellerInfoRepository;

    @Inject
    public SellerInfoUseCasejv(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, SellerInfoRepository sellerInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.sellerInfoRepository = sellerInfoRepository;
    }

    @Override
    public Observable<ResponseSellerInfoModelA> createObservable(RequestParams requestParams) {
        return sellerInfoRepository.getSellerInfoList(requestParams);
    }
}
