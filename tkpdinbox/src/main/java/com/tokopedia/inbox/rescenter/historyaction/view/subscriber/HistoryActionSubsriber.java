package com.tokopedia.inbox.rescenter.historyaction.view.subscriber;

import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionItemDomainData;
import com.tokopedia.inbox.rescenter.historyaction.view.model.HistoryActionViewItem;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentView;

import java.io.IOException;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryActionSubsriber extends Subscriber<HistoryActionData> {

    private final HistoryActionFragmentView fragmentView;

    public HistoryActionSubsriber(HistoryActionFragmentView fragmentView) {
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
    public void onNext(HistoryActionData domainData) {
        if (domainData.isSuccess()) {
            fragmentView.setLoadingView(false);
            fragmentView.setViewData(mappingDomainView(domainData));
            fragmentView.setResolutionStatus(domainData.getResolutionStatus());
            fragmentView.renderData();
        } else {
            fragmentView.onGetHistoryAwbFailed(domainData.getMessageError());
        }
    }

    private ArrayList<HistoryActionViewItem> mappingDomainView(HistoryActionData domainData) {
        ArrayList<HistoryActionViewItem> historyActionViewItems = new ArrayList<>();
        int i = 0;
        for (HistoryActionItemDomainData item : domainData.getListHistoryAddress()) {
            HistoryActionViewItem data = new HistoryActionViewItem();
            data.setActionBy(item.getActionBy());
            data.setActionByText(item.getActionByText() != null ? item.getActionByText() : "");
            data.setConversationID(item.getConversationID());
            data.setDate(item.getDate());
            data.setDateTimestamp(item.getDateTimestamp());
            data.setLatest(i == domainData.getListHistoryAddress().size() - 1);
            data.setHistoryText(item.getHistoryStr());
            data.setDateNumber(item.getDateNumber());
            data.setMonth(item.getMonth());
            data.setTimeNumber(item.getTimeNumber());
            data.setCreateTimestampStr(item.getCreateTimestampStr());
            historyActionViewItems.add(data);
            i++;
        }
        return historyActionViewItems;
    }
}
