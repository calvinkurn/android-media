package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.loadmore.LoadMoreAttachmentEntity;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.loadmore.LoadMoreEntity;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.loadmore.LoadMoreItemEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreAttachment;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreItemData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.loadmore.LoadMoreModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreMapper implements Func1<Response<TkpdResponse>, LoadMoreModel> {

    @Override
    public LoadMoreModel call(Response<TkpdResponse> response) {
        LoadMoreModel domainData = new LoadMoreModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                LoadMoreEntity entity = response.body().convertDataObj(LoadMoreEntity.class);
                domainData.setSuccess(true);
                domainData.setListDiscussionData(mappingEntityDomain(entity.getListDiscussion()));
                domainData.setCanLoadMore(entity.canLoadMore() == 1);
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

    private List<LoadMoreItemData> mappingEntityDomain(List<LoadMoreItemEntity> listDiscussions) {
        List<LoadMoreItemData> list = new ArrayList<>();
        for (LoadMoreItemEntity item : listDiscussions) {
            LoadMoreItemData data = new LoadMoreItemData();
            data.setActionBy(item.getActionBy());
            data.setAttachment(mappingAttachment(item.getAttachment()));
            data.setCreateTime(item.getCreateTime());
            data.setCreateTimeStr(item.getCreateTimeStr());
            data.setResConvId(item.getResConvId());
            data.setSolutionRemark(item.getSolutionRemark());
            data.setActionByStr(item.getActionByStr());
            data.setCreateBy(String.valueOf(item.getCreateBy()));
            list.add(data);
        }
        return list;
    }

    private List<LoadMoreAttachment> mappingAttachment(List<LoadMoreAttachmentEntity> listAttachments) {
        List<LoadMoreAttachment> list = new ArrayList<>();
        for (LoadMoreAttachmentEntity item : listAttachments) {
            LoadMoreAttachment attachment = new LoadMoreAttachment();
            attachment.setUrl(item.getUrl());
            attachment.setImageThumb(item.getImageThumb());
            list.add(attachment);
        }
        return list;
    }
}