package com.tokopedia.inbox.rescenter.historyaddress.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.historyaddress.data.pojo.HistoryAddressEntity;
import com.tokopedia.inbox.rescenter.historyaddress.data.pojo.ListHistoryAddress;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressItemDomainData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/27/17.
 */

public class HistoryAddressMapper implements Func1<Response<TkpdResponse>, HistoryAddressData> {

    @Override
    public HistoryAddressData call(Response<TkpdResponse> response) {
        HistoryAddressData domainData = new HistoryAddressData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                HistoryAddressEntity entity = response.body().convertDataObj(HistoryAddressEntity.class);
                domainData.setSuccess(true);
                domainData.setListHistoryAddress(mappingEntityDomain(entity.getListHistoryAddress()));
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

    private List<HistoryAddressItemDomainData> mappingEntityDomain(List<ListHistoryAddress> listHistoryAwb) {
        List<HistoryAddressItemDomainData> list = new ArrayList<>();
        for (ListHistoryAddress item : listHistoryAwb) {
            HistoryAddressItemDomainData data = new HistoryAddressItemDomainData();
            data.setActionBy(item.getDetail().getAction().getBy());
            data.setActionByText(item.getDetail().getAction().getByStr());
            data.setDate(item.getDetail().getAction().getCreateTimeStr());
            data.setCreateTimestamp(item.getDetail().getAction().getCreateTimestamp());
            data.setCity(item.getAddress().getCity().getName());
            data.setDistrict(item.getAddress().getDistrict().getName());
            data.setProvince(item.getAddress().getProvince().getName());
            data.setStreet(item.getAddress().getStreet());
            data.setPostalCode(item.getAddress().getPostalCode());
            data.setConversationID(item.getDetail().getId());
            data.setReceiver(item.getReceiver().getName());
            data.setPhoneNumber(item.getReceiver().getPhone());
            list.add(data);
        }
        return list;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }
}
