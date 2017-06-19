package com.tokopedia.profilecompletion.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.profilecompletion.data.pojo.GetUserInfoData;
import com.tokopedia.profilecompletion.domain.model.GetUserInfoDomainData;
import com.tokopedia.profilecompletion.domain.model.GetUserInfoDomainModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoMapper implements Func1<Response<String>, GetUserInfoDomainModel> {
    @Override
    public GetUserInfoDomainModel call(Response<String> response) {
        return mappingResponse(response);
    }

    private GetUserInfoDomainModel mappingResponse(Response<String> response) {
        GetUserInfoDomainModel model = new GetUserInfoDomainModel();
        if (response.isSuccessful()) {

            model.setResponseCode(response.code());
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
