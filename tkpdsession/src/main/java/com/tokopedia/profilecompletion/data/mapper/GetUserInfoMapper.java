package com.tokopedia.profilecompletion.data.mapper;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.profilecompletion.data.pojo.GetUserInfoData;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoMapper implements Func1<Response<String>, GetUserInfoDomainModel> {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Override
    public GetUserInfoDomainModel call(Response<String> response) {
        return mappingResponse(response);
    }

    private GetUserInfoDomainModel mappingResponse(Response<String> response) {
        GetUserInfoDomainModel model = new GetUserInfoDomainModel();
        if (response.isSuccessful()) {
            String responseJson = String.valueOf(response.body());
            try {
                JSONObject json = new JSONObject(responseJson);
                if (!json.getString(ERROR).equals("")
                        && !json.getString(ERROR_DESCRIPTION).equals(""))
                    throw new ErrorMessageException(json.getString(ERROR_DESCRIPTION));
            } catch (JSONException e) {
                GetUserInfoData data = new GsonBuilder().create().fromJson(responseJson, GetUserInfoData.class);
                model.setGetUserInfoDomainData(mappingToDomainData(data));
                model.setSuccess(true);
            }


        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }

    private GetUserInfoDomainData mappingToDomainData(GetUserInfoData data) {
        GetUserInfoDomainData domainData = new GetUserInfoDomainData();
        domainData.setAge(data.getAge());
        domainData.setBday(data.getBday());
        domainData.setClientId(data.getClientId());
        domainData.setCompletion(data.getCompletion());
        domainData.setCreatedPassword(data.isCreatedPassword());
        domainData.setEmail(data.getEmail());
        domainData.setFirstName(data.getFirstName());
        domainData.setFullName(data.getFullName());
        domainData.setGender(data.getGender());
        domainData.setLang(data.getLang());
        domainData.setName(data.getName());
        domainData.setPhone(data.getPhone());
        domainData.setPhoneMasked(data.getPhoneMasked());
        domainData.setPhoneVerified(data.isPhoneVerified());
        domainData.setProfilePicture(data.getProfilePicture());
        domainData.setRegisterDate(data.getRegisterDate());
        domainData.setRoles(data.getRoles());
        domainData.setStatus(data.getStatus());
        domainData.setUserId(data.getUserId());
        return domainData;
    }
}
