package com.tokopedia.otp.securityquestion.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.session.data.viewmodel.SecurityDomain;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionViewModel implements Parcelable {

    private String name;
    private String email;
    private SecurityDomain securityDomain;
    private QuestionDomain questionDomain;

    public SecurityQuestionViewModel(SecurityDomain securityDomain, String name, String email) {
        this.securityDomain = securityDomain;
        this.name = name;
        this.email = email;
    }

    protected SecurityQuestionViewModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        securityDomain = in.readParcelable(SecurityDomain.class.getClassLoader());
        questionDomain = in.readParcelable(QuestionDomain.class.getClassLoader());
    }

    public static final Creator<SecurityQuestionViewModel> CREATOR = new Creator<SecurityQuestionViewModel>() {
        @Override
        public SecurityQuestionViewModel createFromParcel(Parcel in) {
            return new SecurityQuestionViewModel(in);
        }

        @Override
        public SecurityQuestionViewModel[] newArray(int size) {
            return new SecurityQuestionViewModel[size];
        }
    };

    public SecurityDomain getSecurityDomain() {
        return securityDomain;
    }

    public void setSecurityDomain(SecurityDomain securityDomain) {
        this.securityDomain = securityDomain;
    }

    public QuestionDomain getQuestionDomain() {
        return questionDomain;
    }

    public void setQuestionDomain(QuestionDomain questionDomain) {
        this.questionDomain = questionDomain;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeParcelable(securityDomain, flags);
        dest.writeParcelable(questionDomain, flags);
    }
}
