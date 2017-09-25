
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner implements Parcelable{

    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("user_img")
    @Expose
    private String userImg;

    protected Owner(Parcel in) {
        userLabel = in.readString();
        userId = in.readInt();
        userLabelId = in.readInt();
        fullName = in.readString();
        userImg = in.readString();
    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };

    /**
     * 
     * @return
     *     The userLabel
     */
    public String getUserLabel() {
        return userLabel;
    }

    /**
     * 
     * @param userLabel
     *     The user_label
     */
    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The userLabelId
     */
    public int getUserLabelId() {
        return userLabelId;
    }

    /**
     * 
     * @param userLabelId
     *     The user_label_id
     */
    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The userImg
     */
    public String getUserImg() {
        return userImg;
    }

    /**
     * 
     * @param userImg
     *     The user_img
     */
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userLabel);
        dest.writeInt(userId);
        dest.writeInt(userLabelId);
        dest.writeString(fullName);
        dest.writeString(userImg);
    }
}
