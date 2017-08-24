package com.tokopedia.ride.completetrip.domain.model;

import java.util.ArrayList;

/**
 * Created by vishal.gupta on 8/22/17.
 */

public class TipList {

    private Boolean enabled;
    private ArrayList<Integer> list = null;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ArrayList<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }
}
