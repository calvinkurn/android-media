package com.tokopedia.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 28/02/18.
 */

public class ProfileInfo {

    @SerializedName("email_verified")
    @Expose
    private boolean emailVerified;
    @SerializedName("gender_name")
    @Expose
    private String genderName;
    @SerializedName("phone_verified")
    @Expose
    private boolean phoneVerified;
    @SerializedName("completion")
    @Expose
    private int completion;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("bday")
    @Expose
    private String bday;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("phone_masked")
    @Expose
    private String phoneMasked;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("created_password")
    @Expose
    private boolean createdPassword;

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneMasked() {
        return phoneMasked;
    }

    public void setPhoneMasked(String phoneMasked) {
        this.phoneMasked = phoneMasked;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isCreatedPassword() {
        return createdPassword;
    }

    public void setCreatedPassword(boolean createdPassword) {
        this.createdPassword = createdPassword;
    }
}
