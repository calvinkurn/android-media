package com.tokopedia.otp.securityquestion.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionPojo {
    @SerializedName("question")
    @Expose
    int question;
    @SerializedName("example")
    @Expose
    String example;
    @SerializedName("title")
    @Expose
    String title;

    public int getQuestion() {
        return question;
    }

    public String getExample() {
        return example;
    }

    public String getTitle() {
        return title;
    }
}
