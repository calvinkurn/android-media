package com.tokopedia.tkpdstream.vote.view.model;

import java.util.List;

/**
 * @author by nisie on 3/1/18.
 */

public class VoteStatisticViewModel {

    List<VoteViewModel> listOptions;
    String totalParticipants;

    public VoteStatisticViewModel(String totalParticipants, List<VoteViewModel> listOptions) {
        this.totalParticipants = totalParticipants;
        this.listOptions = listOptions;
    }

    public List<VoteViewModel> getListOptions() {
        return listOptions;
    }

    public String getTotalParticipants() {
        return totalParticipants;
    }
}
