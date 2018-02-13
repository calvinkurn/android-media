package com.tokopedia.tkpdstream.channel.view.model;

import java.util.List;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelListViewModel {

    List<ChannelViewModel> channelViewModelList;

    public ChannelListViewModel(List<ChannelViewModel> list) {
        this.channelViewModelList = list;
    }

    public List<ChannelViewModel> getChannelViewModelList() {
        return channelViewModelList;
    }

    public void setChannelViewModelList(List<ChannelViewModel> channelViewModelList) {
        this.channelViewModelList = channelViewModelList;
    }
}
