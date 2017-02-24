package com.tokopedia.seller.topads.domain.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.database.DbFlowDatabase;

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
