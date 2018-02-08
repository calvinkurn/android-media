
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_status")
    @Expose
    private int userStatus;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("UserLabel")
    @Expose
    private String userLabel;
    @SerializedName("user_profile_pict")
    @Expose
    private String userProfilePict;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getUserProfilePict() {
        return userProfilePict;
    }

    public void setUserProfilePict(String userProfilePict) {
        this.userProfilePict = userProfilePict;
    }

    public UserReputation getUserReputation() {
        return userReputation;
    }

    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
    }

}
