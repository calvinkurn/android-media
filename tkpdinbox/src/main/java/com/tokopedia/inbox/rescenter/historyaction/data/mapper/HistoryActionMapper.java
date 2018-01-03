package com.tokopedia.inbox.rescenter.historyaction.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.historyaction.data.pojo.HistoryActionEntity;
import com.tokopedia.inbox.rescenter.historyaction.data.pojo.ListHistoryAction;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionItemDomainData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/27/17.
 */

public class HistoryActionMapper implements Func1<Response<TkpdResponse>, HistoryActionData> {

    @Override
    public HistoryActionData call(Response<TkpdResponse> response) {
        HistoryActionData domainData = new HistoryActionData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                HistoryActionEntity entity = response.body().convertDataObj(HistoryActionEntity.class);
                domainData.setSuccess(true);
                domainData.setListHistoryAddress(mappingEntityDomain(entity.getListHistoryAction()));
                domainData.setResolutionStatus(entity.getResolutionStatus());
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

    private List<HistoryActionItemDomainData> mappingEntityDomain(List<ListHistoryAction> listHistoryActions) {
        List<HistoryActionItemDomainData> list = new ArrayList<>();
        for (ListHistoryAction item : listHistoryActions) {
            HistoryActionItemDomainData data = new HistoryActionItemDomainData();
            data.setActionBy(item.getActionBy());
            if (item.getCreateTime() != null) {
                data.setDateTimestamp(item.getCreateTime().getCreateTimestamp());
                data.setDate(item.getCreateTime().getCreateTimeStr());
                data.setMonth(item.getCreateTime().getMonth());
                data.setDateNumber(item.getCreateTime().getDateNumber());
                data.setCreateTimestampStr(item.getCreateTime().getCreateTimestamp());
                data.setTimeNumber(item.getCreateTime().getTimeNumber());
            }
            data.setActionByText(item.getActionByStr());
            data.setHistoryStr(item.getRemark());
            data.setConversationID(item.getResConvId());
            list.add(data);
        }
        return list;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }
}
