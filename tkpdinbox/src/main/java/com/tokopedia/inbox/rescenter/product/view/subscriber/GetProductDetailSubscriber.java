package com.tokopedia.inbox.rescenter.product.view.subscriber;

import com.tokopedia.inbox.rescenter.product.domain.model.AttachmentProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ProductDetailData;
import com.tokopedia.inbox.rescenter.product.view.model.Attachment;
import com.tokopedia.inbox.rescenter.product.view.model.ProductDetailViewData;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailFragmentContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/28/17.
 */

public class GetProductDetailSubscriber extends Subscriber<ProductDetailData> {
    private final ProductDetailFragmentContract.ViewListener viewListener;

    public GetProductDetailSubscriber(ProductDetailFragmentContract.ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            viewListener.onGetProductDetailTimeOut();
        } else {
            viewListener.onGetProductDetailFailed(null);
        }
    }

    @Override
    public void onNext(ProductDetailData domainData) {
        if (domainData.isSuccess()) {
            viewListener.setLoadingView(false);
            viewListener.setMainView(true);
            viewListener.setViewData(mappingDomainView(domainData));
            viewListener.renderData();
        } else {
            viewListener.onGetProductDetailFailed(domainData.getMessageError());
        }
    }

    private ProductDetailViewData mappingDomainView(ProductDetailData domainData) {
        ProductDetailViewData viewData = new ProductDetailViewData();
        viewData.setProductName(domainData.getProductName());
        viewData.setProductThumbUrl(domainData.getProductThumbUrl());
        viewData.setProductPrice(domainData.getProductPrice());
        viewData.setTrouble(domainData.getTrouble());
        viewData.setTroubleReason(domainData.getTroubleReason());
        viewData.setAttachment(
                domainData.getAttachment() != null && domainData.getAttachment().isEmpty() ?
                        mappingAttachment(domainData.getAttachment()) : null
        );
        return viewData;
    }

    private ArrayList<Attachment> mappingAttachment(List<AttachmentProductDomainData> domainDataList) {
        ArrayList<Attachment> list = new ArrayList<>();
        for (AttachmentProductDomainData item : domainDataList) {
            Attachment data = new Attachment();
            data.setThumbUrl(item.getThumbUrl());
            data.setUrl(item.getUrl());
            list.add(data);
        }
        return list;
    }
}
