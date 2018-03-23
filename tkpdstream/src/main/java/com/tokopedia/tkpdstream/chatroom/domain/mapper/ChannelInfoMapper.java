package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ActivePollPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.Channel;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ChannelInfoPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.Option;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.StatisticOption;
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

    private static final String OPTION_TEXT = "Text";
    private static final String OPTION_IMAGE = "Image";

    private static final int DEFAULT_NO_POLL = 0;

    @Inject
    public ChannelInfoMapper() {
    }

    @Override
    public ChannelInfoViewModel call(Response<DataResponse<ChannelInfoPojo>> response) {
        ChannelInfoPojo pojo = response.body().getData();
        //TODO milhamj get sponsor url from pojo
        return new ChannelInfoViewModel(
                pojo.getChannel().getChannelUrl(),
                pojo.getChannel().getCoverUrl(),
                pojo.getChannel().getTitle(),
                hasPoll(pojo.getChannel().getActivePolls()),
                "http://givekidstheworld.org/run/tools/Run%20GKTW%20Signature%20Banner%20Marathon.jpg",
                mapToVoteViewModel(pojo.getChannel().getActivePolls()),
                mapToChannelDesc(pojo.getChannel()));
    }


    private boolean hasPoll(ActivePollPojo activePoll) {
        return activePoll != null
                && activePoll.getStatistic() != null
                && activePoll.getPollId() != DEFAULT_NO_POLL;
    }

    private VoteInfoViewModel mapToVoteViewModel(ActivePollPojo activePollPojo) {
        if (hasPoll(activePollPojo)) {

            return new VoteInfoViewModel(
                    String.valueOf(activePollPojo.getPollId()),
                    activePollPojo.getTitle(),
                    activePollPojo.getQuestion(),
                    mapToListOptions(activePollPojo.isIsAnswered(),
                            activePollPojo.getOptionType(),
                            activePollPojo.getStatistic().getStatisticOptions(),
                            activePollPojo.getOptions()),
                    String.valueOf(activePollPojo.getStatistic().getTotalVoter()),
                    activePollPojo.getPollType(),
                    getVoteOptionType(activePollPojo.getOptionType()),
                    activePollPojo.getStatus(),
                    activePollPojo.getStatusId(),
                    activePollPojo.isIsAnswered(),
                    VoteInfoViewModel.getStringVoteInfo(activePollPojo.getPollTypeId()),
                    activePollPojo.getWinnerUrl().trim(),
                    activePollPojo.getStartTime(),
                    activePollPojo.getEndTime()
            );
        } else {
            return null;
        }
    }

    private List<Visitable> mapToListOptions(boolean isAnswered, String optionType,
                                             List<StatisticOption> statisticOptions,
                                             List<Option> options) {
        List<Visitable> list = new ArrayList<>();
        for (int i = 0; i < statisticOptions.size(); i++) {

            StatisticOption statisticOptionPojo = statisticOptions.get(i);
            Option optionPojo = options.get(i);

            if (optionType.equalsIgnoreCase(OPTION_TEXT)) {
                list.add(new VoteViewModel(
                        String.valueOf(statisticOptionPojo.getOptionId()),
                        statisticOptionPojo.getOption(),
                        String.valueOf(Math.round(statisticOptionPojo.getPercentage() * 100.0) / 100.0),
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected())
                ));
            } else if (optionType.equalsIgnoreCase(OPTION_IMAGE)) {
                list.add(new VoteViewModel(
                        String.valueOf(statisticOptionPojo.getOptionId()),
                        statisticOptionPojo.getOption(),
                        optionPojo.getImageOption().trim(),
                        String.valueOf(Math.round(statisticOptionPojo.getPercentage() * 100.0) / 100.0),
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected())
                ));
            }

        }

        return list;
    }


    private ChannelViewModel mapToChannelDesc(Channel pojo) {
        //TODO milhamj
        return new ChannelViewModel(String.valueOf(pojo.getChannelId()),
                pojo.getModeratorName(),
                pojo.getCoverUrl(),
                pojo.getModeratorProfileUrl(),
                pojo.getTitle(),
                pojo.getDescription(),
                pojo.getTotalViews(),
                pojo.getChannelUrl(),
                "Net TV milik kita bersama sama di dunia ini hanya sementara",
                "https://cdn-image.hipwee.com/wp-content/uploads/2017/07/hipwee-net_blue.png");
    }

    private int checkIfSelected(boolean isAnswered, boolean isSelected) {
        if (isAnswered && isSelected) {
            return VoteViewModel.SELECTED;
        } else if (isAnswered) {
            return VoteViewModel.UNSELECTED;
        } else {
            return VoteViewModel.DEFAULT;
        }
    }

    private String getVoteOptionType(String type) {
        if (type.equalsIgnoreCase(OPTION_IMAGE)) {
            return VoteViewModel.IMAGE_TYPE;
        }
        return VoteViewModel.BAR_TYPE;
    }
}
