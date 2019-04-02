package com.tokopedia.session.register.view.viewmodel.createpassword;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by nisie on 10/13/17.
 */

public class CreatePasswordViewModel implements Parcelable {

    private String email;
    private int bdayDay;
    private int bdayMonth;
    private int bdayYear;
    private String fullName;
    private int gender;
    private String msisdn;
    private String newPass;
    private String confirmPass;
    private String registerTos;
    private String dateText;
    private String loginId;
    private List<String> allowedFieldList;

    public CreatePasswordViewModel(String email, String fullName,
                                   int bdayYear, int bdayMonth, int bdayDay,
                                   List<String> allowedFieldList, String loginId) {
        this.email = email;
        this.bdayDay = bdayDay;
        this.bdayMonth = bdayMonth;
        this.bdayYear = bdayYear;
        this.fullName = fullName;
        this.allowedFieldList = allowedFieldList;
        this.loginId = loginId;
    }

    public String getEmail() {
        return email;
    }

    public int getBdayDay() {
        return bdayDay;
    }

    public int getBdayMonth() {
        return bdayMonth;
    }

    public int getBdayYear() {
        return bdayYear;
    }

    public String getFullName() {
        return fullName;
    }

    public int getGender() {
        return gender;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getNewPass() {
        return newPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public String getRegisterTos() {
        return registerTos;
    }

    public String getDateText() {
        return dateText;
    }

    public List<String> getAllowedFieldList() {
        return allowedFieldList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBdayDay(int bdayDay) {
        this.bdayDay = bdayDay;
    }

    public void setBdayMonth(int bdayMonth) {
        this.bdayMonth = bdayMonth;
    }

    public void setBdayYear(int bdayYear) {
        this.bdayYear = bdayYear;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public void setRegisterTos(String registerTos) {
        this.registerTos = registerTos;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public void setAllowedFieldList(List<String> allowedFieldList) {
        this.allowedFieldList = allowedFieldList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreatePasswordViewModel> CREATOR = new Creator<CreatePasswordViewModel>() {
        @Override
        public CreatePasswordViewModel createFromParcel(Parcel in) {
            return new CreatePasswordViewModel(in);
        }

        @Override
        public CreatePasswordViewModel[] newArray(int size) {
            return new CreatePasswordViewModel[size];
        }
    };


    protected CreatePasswordViewModel(Parcel in) {
        email = in.readString();
        bdayDay = in.readInt();
        bdayMonth = in.readInt();
        bdayYear = in.readInt();
        fullName = in.readString();
        gender = in.readInt();
        msisdn = in.readString();
        newPass = in.readString();
        confirmPass = in.readString();
        registerTos = in.readString();
        dateText = in.readString();
        allowedFieldList = in.createStringArrayList();
        loginId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeInt(bdayDay);
        dest.writeInt(bdayMonth);
        dest.writeInt(bdayYear);
        dest.writeString(fullName);
        dest.writeInt(gender);
        dest.writeString(msisdn);
        dest.writeString(newPass);
        dest.writeString(confirmPass);
        dest.writeString(registerTos);
        dest.writeString(dateText);
        dest.writeStringList(allowedFieldList);
        dest.writeString(loginId);
    }

    public String getLoginId() {
        return loginId;
    }
}
