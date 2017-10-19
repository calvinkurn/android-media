package com.tokopedia.otp.securityquestion.domain.model.securityquestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 10/19/17.
 */

public class QuestionDomain {

    int question;
    String example;
    String title;

    public QuestionDomain(int question, String example, String title) {
        this.question = question;
        this.example = example;
        this.title = title;
    }

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
