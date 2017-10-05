package com.tokopedia.inbox.rescenter.product.view.subscriber;

import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductItemDomainData;
import com.tokopedia.inbox.rescenter.product.view.model.ListProductViewItem;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentView;

import java.io.IOException;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

public class ListProductSubsriber extends Subscriber<ListProductDomainData> {

    private final ListProductFragmentView fragmentView;

    public ListProductSubsriber(ListProductFragmentView fragmentView) {
        this.fragmentView = fragmentView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            fragmentView.onGetHistoryAwbTimeOut();
        } else {
            fragmentView.onGetHistoryAwbFailed(null);
        }
    }

    @Override
    public void onNext(ListProductDomainData domainData) {
        if (domainData.isSuccess()) {
            fragmentView.setLoadingView(false);
            fragmentView.setViewData(mappingDomainView(domainData));
            fragmentView.renderData();
        } else {
            fragmentView.onGetHistoryAwbFailed(domainData.getMessageError());
        }
    }

    private ArrayList<ListProductViewItem> mappingDomainView(ListProductDomainData domainData) {
        ArrayList<ListProductViewItem> listProductViewItems = new ArrayList<>();
        for (ListProductItemDomainData item : domainData.getListHistoryAddress()) {
            ListProductViewItem data = new ListProductViewItem();
            data.setResCenterProductID(item.getResCenterProductID());
            data.setProductImageUrl(item.getProductImageUrl());
            data.setProductName(item.getProductName());
            listProductViewItems.add(data);
        }
        return listProductViewItems;
    }
}
