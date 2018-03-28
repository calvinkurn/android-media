package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.Channel;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.ChannelInfoPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.Flashsale;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.ActivePollPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.Option;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.StatisticOption;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerChildViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
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
        return new ChannelInfoViewModel(
                pojo.getChannel().getChannelUrl(),
                pojo.getChannel().getCoverUrl(),
                pojo.getChannel().getBannerBlurredUrl(),
                pojo.getChannel().getTitle(),
                pojo.getChannel().getAdsImageUrl(),
                pojo.getChannel().getAdsLink(),
                pojo.getChannel().getBannerName(),
                mapToVoteViewModel(pojo.getChannel().getActivePolls()),
                mapToChannelDesc(pojo.getChannel()),
                mapToSprintSaleViewModel(pojo.getChannel().getFlashsale()),
                pojo.getChannel().getSendBirdToken());
    }

    private SprintSaleViewModel mapToSprintSaleViewModel(Flashsale flashsale) {
        if (hasSprintSale()) {
            return new SprintSaleViewModel(
                    mapToListFlashSaleProducts(flashsale.getProducts()),
                    flashsale.getCampaignName(),
                    flashsale.getStartDate(),
                    flashsale.getEndDate(),
                    "REDIRECT_URL",
                    SprintSaleViewModel.TYPE_UPCOMING
            );
        } else {
            return null;
        }
    }

    private boolean hasSprintSale() {
        return true;
    }

    private ArrayList<SprintSaleProductViewModel> mapToListFlashSaleProducts(List<Object> products) {
        ArrayList<SprintSaleProductViewModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(mapToFlashSaleProduct());
        }
        return list;
    }

    private SprintSaleProductViewModel mapToFlashSaleProduct() {
        return new SprintSaleProductViewModel(
                "Produk ampas",
                "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/11/22/0/0_cdf5e667-e237-46c1-a9a2-6fada186b0ae_780_1040.jpg",
                "50% OFF",
                "Rp 300.000",
                "Rp.500.000",
                80,
                "Sudah mau habis",
                "tokopedia://product/29379650");
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
        //TODO milhamj set channel mapper from API
        return new ChannelViewModel(String.valueOf(pojo.getChannelId()),
                pojo.getModeratorName(),
                pojo.getCoverUrl(),
                pojo.getModeratorProfileUrl(),
                pojo.getTitle(),
                pojo.getDescription(),
                pojo.getTotalViews(),
                pojo.getChannelUrl(),
                convertChannelPartner(pojo));
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

    private List<ChannelPartnerViewModel> convertChannelPartner(Channel channel) {
        ArrayList<ChannelPartnerChildViewModel> childViewModels1 = new ArrayList<>();
        ChannelPartnerChildViewModel childViewModel1 =
                new ChannelPartnerChildViewModel(
                        "https://ecs7.tokopedia.net/img/cache/100-square/usr-1/2015/10/29/5700655/pic_5700655_E67E4D68-7E5C-11E5-AEAE-10B4EA20ADBC.jpg",
                        "Agung Hertanto",
                        "tokopedia://people/5700655"
                );
        childViewModels1.add(childViewModel1);
        ChannelPartnerChildViewModel childViewModel2 =
                new ChannelPartnerChildViewModel(
                        "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2017/4/18/18419943/18419943_be10a950-76a7-4efe-8183-44b5f3548e61.jpg",
                        "Hengky Salamah",
                        "tokopedia://people/18419943"
                );
        childViewModels1.add(childViewModel2);

        ArrayList<ChannelPartnerChildViewModel> childViewModels2 = new ArrayList<>();
        ChannelPartnerChildViewModel childViewModel3 =
                new ChannelPartnerChildViewModel(
                        "https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2016/10/28/1557608/1557608_b15a6886-f5d2-48c3-baa0-474add36a396.jpg",
                        "Agen Resmi Mega 6",
                        "https://www.tokopedia.com/agenresmimega6"
                );
        childViewModels2.add(childViewModel3);

        ArrayList<ChannelPartnerViewModel> channelPartnerViewModels = new ArrayList<>();
        ChannelPartnerViewModel channelPartnerViewModel1 = new ChannelPartnerViewModel("Official banget nih?", childViewModels1);
        channelPartnerViewModels.add(channelPartnerViewModel1);

        ChannelPartnerViewModel channelPartnerViewModel2 = new ChannelPartnerViewModel("Official partner dong", childViewModels2);
        channelPartnerViewModels.add(channelPartnerViewModel2);
        channelPartnerViewModels.add(channelPartnerViewModel2);
        channelPartnerViewModels.add(channelPartnerViewModel1);
        channelPartnerViewModels.add(channelPartnerViewModel2);
        channelPartnerViewModels.add(channelPartnerViewModel1);
        channelPartnerViewModels.add(channelPartnerViewModel2);
        channelPartnerViewModels.add(channelPartnerViewModel2);
        channelPartnerViewModels.add(channelPartnerViewModel1);

        return channelPartnerViewModels;
    }
}
