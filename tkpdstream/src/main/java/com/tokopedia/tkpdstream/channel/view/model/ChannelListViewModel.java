package com.tokopedia.tkpdstream.channel.view.model;

import java.util.List;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelListViewModel {

    List<ChannelViewModel> channelViewModelList;
    boolean hasNextPage;

    public ChannelListViewModel(List<ChannelViewModel> list, boolean hasNextPage) {
        this.channelViewModelList = list;
        this.hasNextPage = hasNextPage;
    }

    public List<ChannelViewModel> getChannelViewModelList() {
        return channelViewModelList;
    }

    public void setChannelViewModelList(List<ChannelViewModel> channelViewModelList) {
        this.channelViewModelList = channelViewModelList;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
