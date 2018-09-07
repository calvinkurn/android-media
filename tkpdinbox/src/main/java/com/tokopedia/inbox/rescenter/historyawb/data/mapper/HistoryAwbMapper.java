package com.tokopedia.inbox.rescenter.historyawb.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.historyawb.data.pojo.HistoryAwbEntity;
import com.tokopedia.inbox.rescenter.historyawb.data.pojo.ListHistoryAwb;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.AttachmentAwbDomainData;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbItemDomainData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAwbMapper implements Func1<Response<TkpdResponse>, HistoryAwbData> {

    @Override
    public HistoryAwbData call(Response<TkpdResponse> response) {
        HistoryAwbData domainData = new HistoryAwbData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                HistoryAwbEntity entity = response.body().convertDataObj(HistoryAwbEntity.class);
                domainData.setSuccess(true);
                domainData.setResolutionStatus(entity.getResolutionStatus());
                domainData.setListHistoryAwb(mappingEntityDomain(entity.getListHistoryAwb()));
            } else {
                domainData.setSuccess(false);
                domainData.setMessageError(generateMessageError(response));
            }
        } else {
            domainData.setSuccess(false);
            domainData.setErrorCode(response.code());
        }
        return domainData;
    }

    private List<HistoryAwbItemDomainData> mappingEntityDomain(List<ListHistoryAwb> listHistoryAwb) {
        List<HistoryAwbItemDomainData> list = new ArrayList<>();
        for (ListHistoryAwb item : listHistoryAwb) {
            HistoryAwbItemDomainData data = new HistoryAwbItemDomainData();
            data.setActionBy(item.getActionBy());
            data.setActionByText(item.getActionByStr());
            data.setAttachmentList(item.getAttachments() != null && !item.getAttachments().isEmpty() ?
                    mappingAttachment(item.getAttachments()) : null);
            data.setDate(item.getCreateTimeStr());
            data.setRemark(item.getRemark());
            data.setConversationID(item.getResConvId());
            data.setShipmentID(item.getShippingId());
            data.setShippingRefNumber(item.getShippingRefNum());
            data.setCreateTimestamp(item.getCreateTimeFullStr());
            data.setButtonEdit(item.getButton() != null && item.getButton().getEditResi() == 1);
            data.setButtonTrack(item.getButton() != null && item.getButton().getTrackResi() == 1);
            list.add(data);
        }
        return list;
    }

    private List<AttachmentAwbDomainData> mappingAttachment(List<ListHistoryAwb.Attachments> attachments) {
        List<AttachmentAwbDomainData> list = new ArrayList<>();
        for (ListHistoryAwb.Attachments item : attachments) {
            AttachmentAwbDomainData data = new AttachmentAwbDomainData();
            data.setThumbnailUrl(item.getImageThumb());
            data.setUrl(item.getUrl());
            list.add(data);
        }
        return list;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }
}
