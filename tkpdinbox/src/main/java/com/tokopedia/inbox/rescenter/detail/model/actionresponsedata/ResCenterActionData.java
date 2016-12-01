package com.tokopedia.inbox.rescenter.detail.model.actionresponsedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by hangnadi on 2/25/16.
 */

public class ResCenterActionData implements Parcelable {

    @SerializedName("is_success")
    @Expose
    private Integer success;
    @SerializedName("post_key")
    @Expose
    private String postKey;
    @SerializedName("file_uploaded")
    @Expose
    private String fileUploaded;
    @SerializedName("file_path")
    @Expose
    private String fileUrl;
    @SerializedName("file_th")
    @Expose
    private String fileThumbnailUrl;

    public boolean byPassFlag;
    public Map<String, String> params;

    /**
     *
     * @return
     *     The success
     */
    public Boolean isSuccess() {
        return success == 1;
    }

    /**
     *
     * @param success
     *     The is_success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     *
     * @return
     *      The post_key
     */
    public String getPostKey() {
        return postKey;
    }

    /**
     *
     * @param postKey
     *      The postKey
     */
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    /**
     *
     * @return
     */
    public String getFileUploaded() {
        return fileUploaded;
    }

    /**
     *
     * @param fileUploaded
     */
    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileThumbnailUrl() {
        return fileThumbnailUrl;
    }

    public void setFileThumbnailUrl(String fileThumbnailUrl) {
        this.fileThumbnailUrl = fileThumbnailUrl;
    }

    public boolean isByPassFlag() {
        return byPassFlag;
    }

    public void setByPassFlag(boolean byPassFlag) {
        this.byPassFlag = byPassFlag;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.success);
        dest.writeString(this.postKey);
        dest.writeString(this.fileUploaded);
        dest.writeByte(byPassFlag ? (byte) 1 : (byte) 0);
    }

    public ResCenterActionData() {
    }

    protected ResCenterActionData(Parcel in) {
        this.success = (Integer) in.readValue(Integer.class.getClassLoader());
        this.postKey = in.readString();
        this.fileUploaded = in.readString();
        this.byPassFlag = in.readByte() != 0;
    }

    public static final Creator<ResCenterActionData> CREATOR = new Creator<ResCenterActionData>() {
        public ResCenterActionData createFromParcel(Parcel source) {
            return new ResCenterActionData(source);
        }

        public ResCenterActionData[] newArray(int size) {
            return new ResCenterActionData[size];
        }
    };

}
