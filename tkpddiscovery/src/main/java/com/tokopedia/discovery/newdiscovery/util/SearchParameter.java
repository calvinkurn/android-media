package com.tokopedia.discovery.newdiscovery.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 10/3/17.
 */

public class SearchParameter implements Parcelable {
    private String queryKey="";
    private String source;
    private String uniqueID;
    private String userID;
    private String departmentId;
    private int startRow;

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartRow() {
        return startRow;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.queryKey);
        dest.writeString(this.source);
        dest.writeString(this.uniqueID);
        dest.writeString(this.userID);
        dest.writeString(this.departmentId);
        dest.writeInt(this.startRow);
    }

    public SearchParameter() {
    }

    protected SearchParameter(Parcel in) {
        this.queryKey = in.readString();
        this.source = in.readString();
        this.uniqueID = in.readString();
        this.userID = in.readString();
        this.departmentId = in.readString();
        this.startRow = in.readInt();
    }

    public static final Parcelable.Creator<SearchParameter> CREATOR = new Parcelable.Creator<SearchParameter>() {
        @Override
        public SearchParameter createFromParcel(Parcel source) {
            return new SearchParameter(source);
        }

        @Override
        public SearchParameter[] newArray(int size) {
            return new SearchParameter[size];
        }
    };
}
