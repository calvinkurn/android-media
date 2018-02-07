package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.source;

import android.content.Context;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;

import java.util.Map;

import rx.Observable;

/**
 * Created by yoasfs on 18/07/17.
 */

public class CloudActResultDataSource {

    private Context context;
    private ReviewActService reviewActService;
    private ActResultMapper actResultMapper;

    public CloudActResultDataSource(Context context,
                                    ReviewActService reviewActService,
                                    ActResultMapper actResultMapper) {
        this.context = context;
        this.reviewActService = reviewActService;
        this.actResultMapper = actResultMapper;
    }

    public Observable<ActResultDomain> likeDislikeReviewCloudSource(Map<String, String> parameters) {
        return reviewActService.getApi().likeDislikeReview(AuthUtil.generateParams
                (MainApplication.getAppContext(), parameters))
                .map(actResultMapper);
    }
}
