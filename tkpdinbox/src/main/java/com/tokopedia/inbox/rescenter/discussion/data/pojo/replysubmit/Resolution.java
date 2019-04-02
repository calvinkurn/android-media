package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 7/5/17.
 */

public class Resolution {
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private Status status;
    @SerializedName("createBy")
    private CreateBy createBy;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("updateBy")
    private UpdateBy updateBy;
    @SerializedName("updateTime")
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CreateBy getCreateBy() {
        return createBy;
    }

    public void setCreateBy(CreateBy createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UpdateBy getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(UpdateBy updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
