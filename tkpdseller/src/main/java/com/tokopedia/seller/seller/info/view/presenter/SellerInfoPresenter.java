package com.tokopedia.seller.seller.info.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel;
import com.tokopedia.seller.seller.info.domain.interactor.SellerInfoUseCase;
import com.tokopedia.seller.seller.info.view.SellerInfoView;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoPresenter extends BaseDaggerPresenter<BaseListViewListener<SellerInfoModel>> {
    SellerInfoUseCase sellerInfoUseCase;

    @Inject
    public SellerInfoPresenter(SellerInfoUseCase sellerInfoUseCase) {
        this.sellerInfoUseCase = sellerInfoUseCase;
    }

    public void getSellerInfoList(int page){
        final RequestParams requestParams = RequestParams.create();
        requestParams.putString("page",Integer.toString(page));

        sellerInfoUseCase.execute(requestParams, new Subscriber<ResponseSellerInfoModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper(e);
            }

            @Override
            public void onNext(ResponseSellerInfoModel response ) {
                if(isViewAttached()){
                    getView().onSearchLoaded(conv(response), 1);
                }
            }
        });
    }

    private List<SellerInfoModel> conv(ResponseSellerInfoModel response){
        List<SellerInfoModel> res = new ArrayList<>();
        for (ResponseSellerInfoModel.List list : response.getData().getList()) {
            res.add(conv(list));
        }
        return res;
    }

    private SellerInfoModel conv(ResponseSellerInfoModel.List list){
        SellerInfoModel sellerInfoModel = new SellerInfoModel();
        sellerInfoModel.setContent(list.getContent());
        sellerInfoModel.setCreateTimeUnix(list.getCreateTimeUnix());
        sellerInfoModel.setTitle(list.getTitle());
        sellerInfoModel.setInfoThumbnailUrl(list.getInfoThumbnailUrl());
        sellerInfoModel.setExternalLink(list.getExternalLink());
        return sellerInfoModel;
    }
}
