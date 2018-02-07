package com.tokopedia.inbox.rescenter.historyaddress.view.subscriber;

import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressItemDomainData;
import com.tokopedia.inbox.rescenter.historyaddress.view.model.HistoryAddressViewItem;
import com.tokopedia.inbox.rescenter.historyaddress.view.presenter.HistoryAddressFragmentView;

import java.io.IOException;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAddressSubsriber extends Subscriber<HistoryAddressData> {

    private final HistoryAddressFragmentView fragmentView;

    public HistoryAddressSubsriber(HistoryAddressFragmentView fragmentView) {
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
    public void onNext(HistoryAddressData domainData) {
        if (domainData.isSuccess()) {
            fragmentView.setLoadingView(false);
            fragmentView.setViewData(mappingDomainView(domainData));
            fragmentView.setResolutionStatus(domainData.getResolutionStatus());
            fragmentView.renderData();
        } else {
            fragmentView.onGetHistoryAwbFailed(domainData.getMessageError());
        }
    }

    private ArrayList<HistoryAddressViewItem> mappingDomainView(HistoryAddressData domainData) {
        ArrayList<HistoryAddressViewItem> historyAddressViewItems = new ArrayList<>();
        int i = 0;
        for (HistoryAddressItemDomainData item : domainData.getListHistoryAddress()) {
            HistoryAddressViewItem data = new HistoryAddressViewItem();
            data.setActionBy(item.getActionBy());
            data.setActionByText(item.getActionByText() != null ? item.getActionByText() : "");
            data.setConversationID(item.getConversationID());
            data.setDate(item.getDate());
            data.setCreateTimestamp(item.getCreateTimestamp());
            data.setLatest(i == 0);
            data.setAddress(getAddressFormat(item));
            data.setReceiver(item.getReceiver());
            data.setPhoneNumber(item.getPhoneNumber());
            historyAddressViewItems.add(data);
            i++;
        }
        return historyAddressViewItems;
    }

    private String getAddressFormat(HistoryAddressItemDomainData domainModel) {
        return "<b>" + domainModel.getReceiver() + "</b>" + "<br>" + "<br>" +
                domainModel.getStreet() + "<br>" +
                domainModel.getDistrict() + ", " + domainModel.getCity()  + " - " +
                domainModel.getPostalCode() + "<br>" +
                domainModel.getProvince() + "<br>" +
                "Telp: " + domainModel.getPhoneNumber();
    }
}
