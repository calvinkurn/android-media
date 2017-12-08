
package com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

public class ConversationBetween implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;

    protected ConversationBetween(Parcel in) {
        userId = in.readString();
        userName = in.readString();
    }

    public static final Creator<ConversationBetween> CREATOR = new Creator<ConversationBetween>() {
        @Override
        public ConversationBetween createFromParcel(Parcel in) {
            return new ConversationBetween(in);
        }

        @Override
        public ConversationBetween[] newArray(int size) {
            return new ConversationBetween[size];
        }
    };

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
        return MethodChecker.fromHtml(userName).toString();
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
    }
}
