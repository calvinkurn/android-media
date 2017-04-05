package com.tokopedia.inbox.rescenter.discussion.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
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
    private final ResCenterActService resCenterActService;
    private final CreatePictureMapper createPictureMapper;

    public CloudCreatePictureDataSource(Context context,
                                        ResCenterActService resCenterActService,
                                        CreatePictureMapper createPictureMapper) {
        this.context = context;
        this.resCenterActService = resCenterActService;
        this.createPictureMapper = createPictureMapper;
    }

    public Observable<CreatePictureModel> createImage(String url,
                                                      Map<String, RequestBody> params) {
        return resCenterActService.getApi().createImage(
                url,
                params)
                .map(createPictureMapper);
    }
}
