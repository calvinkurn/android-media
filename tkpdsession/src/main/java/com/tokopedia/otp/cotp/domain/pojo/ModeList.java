
package com.tokopedia.otp.cotp.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModeList {

    @SerializedName("mode_code")
    @Expose
    private int modeCode;
    @SerializedName("mode_text")
    @Expose
    private String modeText;
    @SerializedName("otp_list_text")
    @Expose
    private String otpListText;
    @SerializedName("after_otp_list_text")
    @Expose
    private String afterOtpListText;
    @SerializedName("otp_list_img_url")
    @Expose
    private String otpListImgUrl;

    public int getModeCode() {
        return modeCode;
    }

    public void setModeCode(int modeCode) {
        this.modeCode = modeCode;
    }

    public String getModeText() {
        return modeText;
    }

    public void setModeText(String modeText) {
        this.modeText = modeText;
    }

    public String getOtpListText() {
        return otpListText;
    }

    public void setOtpListText(String otpListText) {
        this.otpListText = otpListText;
    }

    public String getAfterOtpListText() {
        return afterOtpListText;
    }

    public void setAfterOtpListText(String afterOtpListText) {
        this.afterOtpListText = afterOtpListText;
    }

    public String getOtpListImgUrl() {
        return otpListImgUrl;
    }

    public void setOtpListImgUrl(String otpListImgUrl) {
        this.otpListImgUrl = otpListImgUrl;
    }

}
