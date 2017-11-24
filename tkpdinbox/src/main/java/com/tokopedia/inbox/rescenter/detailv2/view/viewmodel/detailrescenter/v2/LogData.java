package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class LogData implements Parcelable {

    public static final Parcelable.Creator<LogData> CREATOR = new Parcelable.Creator<LogData>() {
        @Override
        public LogData createFromParcel(Parcel source) {
            return new LogData(source);
        }

        @Override
        public LogData[] newArray(int size) {
            return new LogData[size];
        }
    };
    private int id;
    private String action;
    private LastSolutionData solution;
    private CreateByData actionBy;
    private CreateByData createBy;
    private String createTime;
    private String createTimeStr;

    public LogData(int id, String action, LastSolutionData solution, CreateByData actionBy, CreateByData createBy, String createTime, String createTimeStr) {
        this.id = id;
        this.action = action;
        this.solution = solution;
        this.actionBy = actionBy;
        this.createBy = createBy;
        this.createTime = createTime;
        this.createTimeStr = createTimeStr;
    }

    protected LogData(Parcel in) {
        this.id = in.readInt();
        this.action = in.readString();
        this.solution = in.readParcelable(LastSolutionData.class.getClassLoader());
        this.actionBy = in.readParcelable(CreateByData.class.getClassLoader());
        this.createBy = in.readParcelable(CreateByData.class.getClassLoader());
        this.createTime = in.readString();
        this.createTimeStr = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LastSolutionData getSolution() {
        return solution;
    }

    public void setSolution(LastSolutionData solution) {
        this.solution = solution;
    }

    public CreateByData getActionBy() {
        return actionBy;
    }

    public void setActionBy(CreateByData actionBy) {
        this.actionBy = actionBy;
    }

    public CreateByData getCreateBy() {
        return createBy;
    }

    public void setCreateBy(CreateByData createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.action);
        dest.writeParcelable(this.solution, flags);
        dest.writeParcelable(this.actionBy, flags);
        dest.writeParcelable(this.createBy, flags);
        dest.writeString(this.createTime);
        dest.writeString(this.createTimeStr);
    }
}
