package com.tokopedia.profile.data.source;

import com.tokopedia.profile.data.network.ProfileApi;

/**
 * Created by alvinatin on 20/02/18.
 */

public class ProfileCloudSource {
    private final ProfileApi profileApi;

    public ProfileCloudSource(ProfileApi profileApi){
        this.profileApi = profileApi;
    }

//    public Observable<something> getProfile(){
//        return profileApi.getProfile()
//    }
//
//    private String getRequestProfile(){
//        return String.format(
//
//        )
//    }
}
