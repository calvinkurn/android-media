package com.tokopedia.home.beranda.domain.model.toppicks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class TopPicksDataModel {
    @SerializedName("groups")
    private List<TopPicksGroupsModel> groups;

    public List<TopPicksGroupsModel> getGroups() {
        return groups;
    }

    public void setGroups(List<TopPicksGroupsModel> groups) {
        this.groups = groups;
    }

}
