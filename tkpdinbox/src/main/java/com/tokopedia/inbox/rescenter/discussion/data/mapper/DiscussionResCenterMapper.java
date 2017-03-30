package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.DiscussionEntity;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.DiscussionItemEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.DiscussionData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.DiscussionItemData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/30/17.
 */

public class DiscussionResCenterMapper implements Func1<Response<TkpdResponse>, DiscussionData> {

    @Override
    public DiscussionData call(Response<TkpdResponse> response) {
        DiscussionData domainData = new DiscussionData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                DiscussionEntity entity = response.body().convertDataObj(DiscussionEntity.class);
                domainData.setSuccess(true);
                domainData.setListDiscussionData(mappingEntityDomain(entity.getListDiscussion()));
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    domainData.setSuccess(false);
                } else {
                    throw new MessageErrorException(response.body().getErrorMessageJoined());
                }
            }
            domainData.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return domainData;
    }

    private List<DiscussionItemData> mappingEntityDomain(List<DiscussionItemEntity> listDiscussions) {
        List<DiscussionItemData> list = new ArrayList<>();
        for (DiscussionItemEntity item : listDiscussions) {
            DiscussionItemData data = new DiscussionItemData();
            data.setActionBy(item.getActionBy());
            data.setAttachment(item.getAttachment());
            data.setCreateTime(item.getCreateTime());
            data.setCreateTimeStr(item.getCreateTimeStr());
            data.setResConvId(item.getResConvId());
            data.setSolutionRemark(item.getSolutionRemark());
            list.add(data);
        }
        return list;
    }

}