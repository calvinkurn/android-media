package com.tokopedia.otp.securityquestion.domain.model.securityquestion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 10/19/17.
 */

public class QuestionDomain implements Parcelable {

    String question;
    String title;

    public QuestionDomain(String question, String title) {
        this.question = question;
        this.title = title;
    }

    protected QuestionDomain(Parcel in) {
        question = in.readString();
        title = in.readString();
    }

    public static final Creator<QuestionDomain> CREATOR = new Creator<QuestionDomain>() {
        @Override
        public QuestionDomain createFromParcel(Parcel in) {
            return new QuestionDomain(in);
        }

        @Override
        public QuestionDomain[] newArray(int size) {
            return new QuestionDomain[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(title);
    }
}
