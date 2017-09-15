package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class GroupAdBulkAction extends BulkAction {

    @SerializedName("groups")
    @Expose
    private List<GroupAdAction> adList;

    public List<GroupAdAction> getAdList() {
        return adList;
    }

    public void setAdList(List<GroupAdAction> adList) {
        this.adList = adList;
    }
}
