package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.Channel;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.ChannelInfoPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.Flashsale;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.ListBrand;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo.ListOfficial;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.ActivePollPojo;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.Option;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.poll.StatisticOption;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerChildViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
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
                pojo.getChannel().getTitle(),
                pojo.getChannel().getChannelUrl(),
                pojo.getChannel().getCoverUrl(),
                pojo.getChannel().getBannerBlurredUrl(),
                pojo.getChannel().getAdsImageUrl(),
                pojo.getChannel().getAdsLink(),
                pojo.getChannel().getBannerName(),
                pojo.getChannel().getSendBirdToken(),
                pojo.getChannel().getModeratorName(),
                pojo.getChannel().getCoverUrl(),
                pojo.getChannel().getModeratorProfileUrl(),
                pojo.getChannel().getDescription(),
                pojo.getChannel().getTotalViews(),
                convertChannelPartner(pojo.getChannel()),
                mapToVoteViewModel(pojo.getChannel().getActivePolls()),
                mapToSprintSaleViewModel(pojo.getChannel().getFlashsale())
        );
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
        ArrayList<ChannelPartnerViewModel> channelPartnerViewModelList = new ArrayList<>();

        for (ListOfficial official : channel.getListOfficials()) {

            ChannelPartnerViewModel channelPartnerViewModel = new ChannelPartnerViewModel(
                    official.getTitle(),
                    convertChannelPartnerChild(official)
            );

            channelPartnerViewModelList.add(channelPartnerViewModel);
        }
        return channelPartnerViewModelList;
    }

    private List<ChannelPartnerChildViewModel> convertChannelPartnerChild(ListOfficial official) {
        ArrayList<ChannelPartnerChildViewModel> childViewModelList = new ArrayList<>();

        for (ListBrand brand : official.getListBrands()) {
            ChannelPartnerChildViewModel childViewModel = new ChannelPartnerChildViewModel(
                    brand.getImageUrl(),
                    brand.getTitle(),
                    brand.getBrandUrl()
            );
            childViewModelList.add(childViewModel);
        }

        return childViewModelList;
    }
}
