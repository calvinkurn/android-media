
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxReputationDetailPojo {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("app")
    @Expose
    private String app;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

}
