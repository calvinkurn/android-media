package com.tokopedia.tkpdpdp.presenter;

import android.content.Context;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpdpdp.domain.gql.ImageReviewGqlResponse;
import com.tokopedia.tkpdpdp.helper.GqlHelper;
import com.tokopedia.tkpdpdp.viewmodel.ImageReviewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class ReviewGalleryPresenterImpl implements ReviewGalleryPresenter {

    private static final int DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE = 18;

    private GraphqlUseCase graphqlUseCase;

    public ReviewGalleryPresenterImpl() {
        graphqlUseCase = new GraphqlUseCase();
    }

    @Override
    public void cancelLoadDataRequest() {
        graphqlUseCase.unsubscribe();
    }

    @Override
    public void loadData(Context context, int productId, int startRow, LoadDataListener loadDataListener) {
        int page = startRow / DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE + 1;
        GqlHelper.requestImageReview(context,
                page,
                DEFAULT_IMAGE_REVIEW_ROW_PER_PAGE,
                productId, graphqlUseCase, new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loadDataListener.onFailed();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ImageReviewGqlResponse gqlResponse = graphqlResponse.getData(ImageReviewGqlResponse.class);
                loadDataListener.onSuccess(GqlHelper.convertToImageReviewItemList(gqlResponse));
            }
        });
    }
}
