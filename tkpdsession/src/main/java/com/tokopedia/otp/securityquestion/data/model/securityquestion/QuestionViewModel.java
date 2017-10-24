package com.tokopedia.otp.securityquestion.data.model.securityquestion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 10/19/17.
 */

public class QuestionViewModel implements Parcelable {

    String question;
    String title;

    public QuestionViewModel(String question, String title) {
        this.question = question;
        this.title = title;
    }

    protected QuestionViewModel(Parcel in) {
        question = in.readString();
        title = in.readString();
    }

    public static final Creator<QuestionViewModel> CREATOR = new Creator<QuestionViewModel>() {
        @Override
        public QuestionViewModel createFromParcel(Parcel in) {
            return new QuestionViewModel(in);
        }

        @Override
        public QuestionViewModel[] newArray(int size) {
            return new QuestionViewModel[size];
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
