package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class DataEtalase {

    @SerializedName("list_other")
    @Expose
    private List<EtalaseOther> listOther = null;
    @SerializedName("list")
    @Expose
    private java.util.List<Etalase> list = null;

//    @SerializedName("paging")
//    @Expose
//    private Paging paging;

    public java.util.List<EtalaseOther> getListOther() {
        return listOther;
    }

    public void setListOther(java.util.List<EtalaseOther> listOther) {
        this.listOther = listOther;
    }

    public java.util.List<Etalase> getList() {
        return list;
    }

    public void setList(java.util.List<Etalase> list) {
        this.list = list;
    }

//    public Paging getPaging() {
//        return paging;
//    }
//
//    public void setPaging(Paging paging) {
//        this.paging = paging;
//    }
}
