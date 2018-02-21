package com.tokopedia.profilecompletion.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by nisie on 6/19/17.
 */


public class GetUserInfoData {

    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("bday")
    @Expose
    private String bday;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("phone_masked")
    @Expose
    private String phoneMasked;
    @SerializedName("register_date")
    @Expose
    private String registerDate;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("created_password")
    @Expose
    private boolean createdPassword;
    @SerializedName("phone_verified")
    @Expose
    private boolean phoneVerified;
    @SerializedName("roles")
    @Expose
    private List<Integer> roles = null;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("completion")
    @Expose
    private int completion;
    @SerializedName("create_password_list")
    @Expose
    private List<String> createPasswordList ;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isCreatedPassword() {
        return createdPassword;
    }

    public void setCreatedPassword(boolean createdPassword) {
        this.createdPassword = createdPassword;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public List<String> getCreatePasswordList() {
        return createPasswordList;
    }
}