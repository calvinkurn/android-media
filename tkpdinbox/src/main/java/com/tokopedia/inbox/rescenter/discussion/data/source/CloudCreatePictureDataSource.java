package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.CreatePictureMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.model.CreatePictureModel;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class CloudCreatePictureDataSource {

    private Context context;
    private final ResCenterActApi resCenterActApi;
    private final CreatePictureMapper createPictureMapper;

    public CloudCreatePictureDataSource(Context context,
                                        ResCenterActApi resCenterActApi,
                                        CreatePictureMapper createPictureMapper) {
        this.context = context;
        this.resCenterActApi = resCenterActApi;
        this.createPictureMapper = createPictureMapper;
    }

    public Observable<CreatePictureModel> createImage(String url,
                                                      Map<String, RequestBody> params) {
        return resCenterActApi
                .createImage(url, params)
                .map(createPictureMapper);
    }
}
