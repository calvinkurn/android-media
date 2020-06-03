package com.tokopedia.profilecompletion.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 6/19/17.
 */
class GetUserInfoData {
    @SerializedName("user_id")
    @Expose
    var userId = 0

    @SerializedName("full_name")
    @Expose
    var fullName: String = ""

    @SerializedName("first_name")
    @Expose
    var firstName: String = ""

    @SerializedName("name")
    @Expose
    var name: String = ""

    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("gender")
    @Expose
    var gender = 0

    @SerializedName("bday")
    @Expose
    var bday: String = ""

    @SerializedName("age")
    @Expose
    var age = 0

    @SerializedName("phone")
    @Expose
    var phone: String = ""

    @SerializedName("phone_masked")
    @Expose
    var phoneMasked: String = ""

    @SerializedName("register_date")
    @Expose
    var registerDate: String = ""

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("lang")
    @Expose
    var lang: String = ""

    @SerializedName("created_password")
    @Expose
    var isCreatedPassword = false

    @SerializedName("phone_verified")
    @Expose
    var isPhoneVerified = false

    @SerializedName("roles")
    @Expose
    var roles: List<Int> = arrayListOf()

    @SerializedName("profile_picture")
    @Expose
    var profilePicture: String = ""

    @SerializedName("client_id")
    @Expose
    var clientId: String = ""

    @SerializedName("completion")
    @Expose
    var completion = 0

    @SerializedName("create_password_list")
    @Expose
    val createPasswordList: List<String> = arrayListOf()

}