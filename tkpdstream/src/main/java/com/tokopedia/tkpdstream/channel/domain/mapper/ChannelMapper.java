package com.tokopedia.tkpdstream.channel.domain.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdstream.channel.domain.pojo.Channel;
import com.tokopedia.tkpdstream.channel.domain.pojo.ChannelListPojo;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerChildViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelMapper implements Func1<Response<DataResponse<ChannelListPojo>>, ChannelListViewModel> {

    private static final String LAST_PAGE = "last";

    @Inject
    public ChannelMapper() {
    }

    @Override
    public ChannelListViewModel call(Response<DataResponse<ChannelListPojo>> dataResponseResponse) {
        ChannelListPojo pojo = dataResponseResponse.body().getData();

        return new ChannelListViewModel(getListChannel(pojo),
                hasNextPage(pojo.getCursor()));
    }

    private boolean hasNextPage(String cursor) {
        return !cursor.equals(LAST_PAGE);
    }

    private List<ChannelViewModel> getListChannel(ChannelListPojo pojo) {
        List<ChannelViewModel> list = new ArrayList<>();
        for (Channel channelPojo : pojo.getChannels()) {
            list.add(convertToViewModel(channelPojo));
        }
        return list;
    }

    private ChannelViewModel convertToViewModel(Channel channelPojo) {
        //TODO milhamj set channel mapper from API
        return new ChannelViewModel(
                String.valueOf(channelPojo.getChannelId()),
                channelPojo.getModeratorName()!= null ? channelPojo.getModeratorName() : "",
                channelPojo.getCoverUrl()!= null ? channelPojo.getCoverUrl() : "",
                channelPojo.getModeratorThumbUrl() != null ? channelPojo.getModeratorThumbUrl() : "",
                channelPojo.getTitle() != null? channelPojo.getTitle() : "",
                channelPojo.getDescription() != null ? channelPojo.getDescription() : "",
                channelPojo.getTotalViews(),
                channelPojo.getChannelUrl(),
                convertChannelPartner(channelPojo)
        );
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
