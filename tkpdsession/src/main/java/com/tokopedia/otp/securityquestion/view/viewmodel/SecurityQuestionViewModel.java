package com.tokopedia.otp.securityquestion.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.otp.securityquestion.data.model.securityquestion.QuestionViewModel;
import com.tokopedia.session.data.viewmodel.SecurityDomain;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionViewModel implements Parcelable {

    private String name;
    private String email;
    private SecurityDomain securityDomain;
    private QuestionViewModel questionViewModel;
    private String phone;

    public SecurityQuestionViewModel(SecurityDomain securityDomain, String name, String email,
                                     String phone) {
        this.securityDomain = securityDomain;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    protected SecurityQuestionViewModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        securityDomain = in.readParcelable(SecurityDomain.class.getClassLoader());
        questionViewModel = in.readParcelable(QuestionViewModel.class.getClassLoader());
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

    public QuestionViewModel getQuestionViewModel() {
        return questionViewModel;
    }

    public void setQuestionViewModel(QuestionViewModel questionViewModel) {
        this.questionViewModel = questionViewModel;
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
        dest.writeString(phone);
        dest.writeParcelable(securityDomain, flags);
        dest.writeParcelable(questionViewModel, flags);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
