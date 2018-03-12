package com.tokopedia.tkpdstream.vote.domain.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.vote.domain.pojo.SendVotePojo;
import com.tokopedia.tkpdstream.vote.domain.pojo.Statistic;
import com.tokopedia.tkpdstream.vote.domain.pojo.StatisticOption;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class SendVoteMapper implements Func1<Response<DataResponse<SendVotePojo>>, VoteStatisticViewModel> {

    @Inject
    public SendVoteMapper() {
    }

    @Override
    public VoteStatisticViewModel call(Response<DataResponse<SendVotePojo>> dataResponseResponse) {
        SendVotePojo pojo = dataResponseResponse.body().getData();
        return mappingToViewModel(pojo.getStatistic());
    }

    private VoteStatisticViewModel mappingToViewModel(Statistic statistic) {
        return new VoteStatisticViewModel(
                String.valueOf(statistic.getTotalVoter()),
                mappingToListOption(statistic.getStatisticOptions())
        );
    }

    private List<VoteViewModel> mappingToListOption(List<StatisticOption> statisticOptions) {
        List<VoteViewModel> list = new ArrayList<>();
        for (StatisticOption option : statisticOptions) {
            list.add(new VoteViewModel(
                    option.getOptionId() != null ? option.getOptionId() : "",
                    option.getOption() != null ? option.getOption() : "",
                    String.valueOf(Math.floor(option.getPercentage())),
                    0));
        }
        return list;
    }
}
