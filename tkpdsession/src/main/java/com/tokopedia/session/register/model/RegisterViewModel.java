package com.tokopedia.session.register.model;

/**
 * Created by nisie on 1/30/17.
 */

public class RegisterViewModel {

    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    private RegisterStep1ViewModel registerStep1ViewModel;

    private int dateYear;
    private int dateMonth;
    private int dateDay;
    private String dateText;

    private boolean isAgreedTermCondition;
    private String phone;
    private int gender;
    private String confirmPassword;

    private long maxDate;
    private long minDate;

    public RegisterStep1ViewModel getRegisterStep1ViewModel() {
        return registerStep1ViewModel;
    }

    public void setRegisterStep1ViewModel(RegisterStep1ViewModel registerStep1ViewModel) {
        this.registerStep1ViewModel = registerStep1ViewModel;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public boolean isAgreedTermCondition() {
        return isAgreedTermCondition;
    }

    public void setAgreedTermCondition(boolean agreedTermCondition) {
        isAgreedTermCondition = agreedTermCondition;
    }

    public long getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public long getMinDate() {
        return minDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}

