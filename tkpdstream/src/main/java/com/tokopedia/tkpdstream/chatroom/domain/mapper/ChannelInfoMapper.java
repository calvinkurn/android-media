package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ActivePollPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.Channel;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ChannelInfoPojo;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoMapper implements Func1<Response<DataResponse<ChannelInfoPojo>>, ChannelInfoViewModel> {

    @Inject
    public ChannelInfoMapper() {
    }

    @Override
    public ChannelInfoViewModel call(Response<DataResponse<ChannelInfoPojo>> response) {
        ChannelInfoPojo pojo = response.body().getData();
        return new ChannelInfoViewModel(
                pojo.getChannel().getChannelUrl(),
                pojo.getChannel().getCoverUrl(),
                pojo.getChannel().getTitle(),
                pojo.getChannel().getTotalParticipantsOnline(),
                hasPoll(pojo.getChannel().getActivePolls()),
                mapToVoteViewModel(pojo.getChannel().getActivePolls()),
                mapToChannelDesc(pojo.getChannel()));
    }


    private boolean hasPoll(ActivePollPojo activePolls) {
//        return !activePolls.isEmpty();
        return true;
    }

    private VoteInfoViewModel mapToVoteViewModel(ActivePollPojo activePollPojo) {
        activePollPojo.setStartTime(1519873200);
        activePollPojo.setEndTime(1519880400);

        return new VoteInfoViewModel(
                String.valueOf(activePollPojo.getPollId()),
                activePollPojo.getQuestion(),
                mapToListOptions(activePollPojo.getOptions()),
                "10",
                activePollPojo.getPollType(),
                activePollPojo.getStatus(),
                false,
                "INFO YG PERLU DI UPDATE",
                "Belon ada",
                activePollPojo.getStartTime(),
                activePollPojo.getEndTime()
        );
    }

    private List<Visitable> mapToListOptions(List<Object> options) {
        List<Visitable> list = new ArrayList<>();
        return list;
    }


    private ChannelViewModel mapToChannelDesc(Channel pojo) {

        return new ChannelViewModel(String.valueOf(pojo.getChannelId()), pojo.getModeratorName()
                , pojo.getCoverUrl(), pojo.getModeratorProfileUrl(), pojo.getTitle(), pojo.getDescription(), pojo.getTotalParticipantsOnline());
    }
}
