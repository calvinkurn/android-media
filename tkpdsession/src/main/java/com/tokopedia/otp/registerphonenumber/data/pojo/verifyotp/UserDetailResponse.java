
package com.tokopedia.otp.registerphonenumber.data.pojo.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 5/3/18.
 */

public class UserDetailResponse {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("tkpd_user_id")
    @Expose
    private int tkpdUserId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTkpdUserId() {
        return tkpdUserId;
    }

    public void setTkpdUserId(int tkpdUserId) {
        this.tkpdUserId = tkpdUserId;
    }

}
