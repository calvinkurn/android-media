
package com.tokopedia.tkpdstream.channel.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperatorPojo {

    @SerializedName("participant_id")
    @Expose
    private int participantId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("image")
    @Expose
    private String image;

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
