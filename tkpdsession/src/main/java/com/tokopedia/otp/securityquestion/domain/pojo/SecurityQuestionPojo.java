package com.tokopedia.otp.securityquestion.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionPojo {
    @SerializedName("question")
    @Expose
    String question;
    @SerializedName("title")
    @Expose
    String title;

    public String getQuestion() {
        return question;
    }

    public String getTitle() {
        return title;
    }
}
