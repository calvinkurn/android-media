package com.tokopedia.inbox.rescenter.history.view.subscriber;

import com.tokopedia.inbox.rescenter.history.HistoryShippingFragmentView;
import com.tokopedia.inbox.rescenter.history.domain.model.AttachmentAwbDomainData;
import com.tokopedia.inbox.rescenter.history.domain.model.HistoryAwbData;
import com.tokopedia.inbox.rescenter.history.domain.model.HistoryAwbItemDomainData;
import com.tokopedia.inbox.rescenter.history.view.model.Attachment;
import com.tokopedia.inbox.rescenter.history.view.model.HistoryAwbViewItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAwbSubsriber extends Subscriber<HistoryAwbData> {

    private final HistoryShippingFragmentView fragmentView;

    public HistoryAwbSubsriber(HistoryShippingFragmentView fragmentView) {
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
    public void onNext(HistoryAwbData historyAwbData) {
        if (historyAwbData.isSuccess()) {
            fragmentView.setLoadingView(false);
            fragmentView.showInpuNewShippingAwb(true);
            fragmentView.setViewData(mappingDomainView(historyAwbData));
            fragmentView.renderData();
        } else {
            fragmentView.onGetHistoryAwbFailed(historyAwbData.getMessageError());
        }
    }

    private ArrayList<HistoryAwbViewItem> mappingDomainView(HistoryAwbData historyAwbData) {
        ArrayList<HistoryAwbViewItem> historyAwbViewItems = new ArrayList<>();
        int i = 0;
        for (HistoryAwbItemDomainData item : historyAwbData.getListHistoryAwb()) {
            HistoryAwbViewItem data = new HistoryAwbViewItem();
            data.setActionBy(item.getActionBy());
            data.setActionByText(item.getActionByText());
            data.setAttachment(
                    item.getAttachmentList() != null && !item.getAttachmentList().isEmpty() ?
                            mappingAttacment(item.getAttachmentList()) : null
            );
            data.setConversationID(item.getConversationID());
            data.setDate(item.getDate());
            data.setRemark(item.getRemark());
            data.setShipmentID(item.getShipmentID());
            data.setShippingRefNumber(item.getShippingRefNumber());
            data.setLatest(i == 0);
            historyAwbViewItems.add(data);
            i++;
        }
        return historyAwbViewItems;
    }

    private ArrayList<Attachment> mappingAttacment(List<AttachmentAwbDomainData> attachmentList) {
        ArrayList<Attachment> attachments = new ArrayList<>();
        for (AttachmentAwbDomainData item : attachmentList) {
            Attachment data = new Attachment();
            data.setUrl(item.getUrl());
            data.setThumbnailUrl(item.getThumbnailUrl());
            attachments.add(data);
        }
        return attachments;
    }
}
