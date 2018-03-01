package com.tokopedia.profile.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.profile.data.mapper.ProfileDataMapper;
import com.tokopedia.profile.data.network.ProfileApi;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

import static com.tokopedia.profile.domain.usecase.GetProfileContentDataUseCase.PARAM_USER_ID;

/**
 * Created by alvinatin on 20/02/18.
 */

public class GetProfileContentDataSourceCloud {
    private final Context context;
    private final ProfileApi profileApi;
    private final ProfileDataMapper getProfileDataMapper;

    @Inject
    public GetProfileContentDataSourceCloud(@ApplicationContext Context context,
                                            ProfileApi profileApi,
                                            ProfileDataMapper getProfileDataMapper){
        this.context = context;
        this.profileApi = profileApi;
        this.getProfileDataMapper = getProfileDataMapper;
    }

    public Observable<TopProfileViewModel> getProfileContentData(RequestParams requestParams){
        return profileApi.getProfileContent(getRequestPayload(requestParams))
                .map(getProfileDataMapper);
    }

    private GraphqlRequest getRequestPayload(RequestParams requestParams) {
        HashMap<String, Object> requestVariable = new HashMap<>();
        requestVariable.put(PARAM_USER_ID, requestParams.getInt(PARAM_USER_ID, 0));

        return new GraphqlRequest(
                loadRawString(context.getResources(), R.raw.query_get_profile_content_data),
                requestVariable);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
